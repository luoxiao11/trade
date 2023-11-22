package com.shangan.trade.web.portal.client;


import com.shangan.trade.common.model.TradeResultDTO;
import com.shangan.trade.web.portal.client.model.Order;
import com.shangan.trade.web.portal.client.model.SeckillActivity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//FeignClient注解设置服务提供者名字，在consul注册中心注册的服务名字
@FeignClient(name = "trade-lightning-deal", contextId = "trade-web-portal")
public interface SeckillActivityFeignClient {

    /**
     * 插入一个秒杀活动
     *
     * @param seckillActivity
     * @return
     */
    @RequestMapping("/seckill/insertSeckillActivity")
    boolean insertSeckillActivity(@RequestParam("seckillActivity") SeckillActivity seckillActivity);

    /**
     * 查询秒杀活动
     *
     * @param id
     * @return
     */
    @RequestMapping("/seckill/querySeckillActivityById")
    SeckillActivity querySeckillActivityById(@RequestParam("id") long id);

    @RequestMapping("/seckill/queryActivityByStatus")
    List<SeckillActivity> queryActivityByStatus(@RequestParam("status") int status);

    /**
     * 处理秒杀请求
     *
     * @param seckillActivityId
     * @return
     */
    @RequestMapping("/seckill/seckillActivityId")
    boolean processSeckillReqBase(@RequestParam("seckillActivityId") long seckillActivityId);

    /**
     * 处理秒杀请求
     *
     * @param userId
     * @param seckillActivityId
     * @return
     */
    @RequestMapping("/seckill/processSeckill")
    TradeResultDTO<Order> processSeckill(@RequestParam("userId") long userId, @RequestParam("seckillActivityId") long seckillActivityId);

    /**
     * 锁定商品的库存
     *
     * @param id
     * @return
     */
    @RequestMapping("/seckill/lockStock")
    boolean lockStock(@RequestParam("id") long id);

    /**
     * 库存扣减
     *
     * @param id
     * @return
     */
    @RequestMapping("/seckill/deductStock")
    boolean deductStock(@RequestParam("id") long id);

    /**
     * 锁定的库存回补
     *
     * @param id
     * @return
     */
    @RequestMapping("/seckill/revertStock")
    boolean revertStock(@RequestParam("id") long id);

    /**
     * 缓存预热
     * 将秒杀信息写入Redis中
     *
     * @param id
     */
    @RequestMapping("/seckill/pushSeckillActivityInfoToCache")
    void pushSeckillActivityInfoToCache(@RequestParam("id") long id);
}
