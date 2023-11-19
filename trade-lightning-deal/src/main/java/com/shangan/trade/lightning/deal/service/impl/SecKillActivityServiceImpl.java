package com.shangan.trade.lightning.deal.service.impl;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.goods.db.model.Goods;
import com.shangan.trade.goods.service.GoodsService;
import com.shangan.trade.lightning.deal.db.dao.SeckillActivityDao;
import com.shangan.trade.lightning.deal.db.model.SeckillActivity;
import com.shangan.trade.lightning.deal.service.SeckillActivityService;
import com.shangan.trade.lightning.deal.utils.RedisWorker;
import com.shangan.trade.order.db.model.Order;
import com.shangan.trade.order.mq.OrderMessageSender;
import com.shangan.trade.order.service.LimitBuyService;
import com.shangan.trade.order.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SecKillActivityServiceImpl implements SeckillActivityService {
    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Autowired
    private LimitBuyService limitBuyService;

    @Autowired
    private RedisWorker redisWorker;

    /**
     * datacenterId;  数据中心
     * machineId;     机器标识
     * 在分布式环境中可以从机器配置上读取
     * 单机开发环境中先写死
     */
    private final SnowflakeIdWorker snowFlake = new SnowflakeIdWorker(6, 8);

    @Autowired
    private OrderMessageSender orderMessageSender;
    @Autowired
    private GoodsService goodsService;

    @Override
    public boolean insertSeckillActivity(SeckillActivity seckillActivity) {
        return seckillActivityDao.insertSeckillActivity(seckillActivity);
    }

    @Override
    public SeckillActivity querySeckillActivityById(long id) {
        return seckillActivityDao.querySeckillActivityById(id);
    }

    @Override
    public List<SeckillActivity> queryActivityByStatus(int status) {
        return seckillActivityDao.queryActivityByStatus(status);
    }

    /**
     * 处理秒杀请求
     * 高并发时会出现超卖
     *
     * @param seckillActivityId
     * @return
     */
    @Override
    public Order processSeckillReqBase(long userId, long seckillActivityId) {

        //1.校验用户是否有购买资格
        if (limitBuyService.isInLimitMember(seckillActivityId, userId)) {
            log.error("当前用户已经购买过不能重复购买 seckillActivityId={} userId={}", seckillActivityId, userId);
            throw new RuntimeException("当前用户已经购买过,不能重复购买");
        }

        //使用Redis 进行拦截优化处理多并发，15w-20wQPS
        //2.使用Redis中Lua先进行库存校验
        String key = "stock:" + seckillActivityId;
        boolean checkResult = redisWorker.stockDeductCheck(key);
        if(!checkResult) {
            throw new RuntimeException("库存不足，抢购失败");
        }

        //3.查询对应的秒杀活动信息
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);
        if (seckillActivity == null) {
            log.error("seckillActivityId={} 查询不到对应的秒杀活动", seckillActivityId);
            throw new RuntimeException("查询不到对应的秒杀活动");
        }
        //4.锁定库存
        boolean lockStockRes = seckillActivityDao.lockStock(seckillActivityId);
        if (!lockStockRes) {
            log.info("商品抢购失败，商品已经售完 seckillActivityId={} userId={}", seckillActivityId, userId);
            throw new RuntimeException("商品抢购失败，商品已经售完");
        }
        log.info("商品抢购成功 seckillActivityId={} userId={}", seckillActivityId, userId);

        //5.组装订单模型-发出消息
        Order order = new Order();
        order.setId(snowFlake.nextId());
        order.setActivityId(seckillActivityId);
        //type=1表示秒杀活动
        order.setActivityType(1);
        order.setGoodsId(seckillActivity.getGoodsId());
        order.setPayPrice(seckillActivity.getSeckillPrice());
        order.setUserId(userId);
        /*
         * 状态:0,没有可用库存订单创建失败;1,已创建，等待付款;2 已支付,等待发货;99 订单关闭，超时未付款
         */
        order.setStatus(1);
        order.setCreateTime(new Date());

        //6.创建订单，发送创建订单消息
        orderMessageSender.sendCreateOrderMessage(JSON.toJSONString(order));
        return order;
    }

    @Override
    public boolean lockStock(long id) {
        log.info("秒杀活动锁定库存 seckillActivityId:{}", id);
        return seckillActivityDao.lockStock(id);
    }

    @Override
    public boolean deductStock(long id) {
        log.info("秒杀活动扣减库存 seckillActivityId:{}", id);
        return seckillActivityDao.deductStock(id);
    }

    @Override
    public boolean revertStock(long id) {
        log.info("秒杀活动回补库存 seckillActivityId:{}", id);
        return seckillActivityDao.revertStock(id);
    }
    @Override
    public void pushSeckillActivityInfoToCache(long id) {
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(id);
        //库存信息
        redisWorker.setValue("stock:" + id, Long.valueOf(seckillActivity.getAvailableStock()));

        //活动完整信息
        redisWorker.setValue("seckillActivity:" + seckillActivity.getId(), JSON.toJSONString(seckillActivity));

        //活动对应的商品信息
        Goods goods = goodsService.queryGoodsById(seckillActivity.getGoodsId());
        redisWorker.setValue("seckillActivity_goods:" + seckillActivity.getGoodsId(), JSON.toJSONString(goods));
    }
}
