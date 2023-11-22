package com.shangan.trade.lightning.deal.service;

import com.shangan.trade.lightning.deal.client.model.Order;
import com.shangan.trade.lightning.deal.db.model.SeckillActivity;

import java.util.List;

public interface SeckillActivityService {

    /**
     * 插入一个秒杀活动
     *
     * @param seckillActivity
     * @return
     */
    boolean insertSeckillActivity(SeckillActivity seckillActivity);

    /**
     * 查询秒杀活动
     *
     * @param id
     * @return
     */
    SeckillActivity querySeckillActivityById(long id);

    List<SeckillActivity> queryActivityByStatus(int status);

    /**
     * 处理秒杀请求
     *
     * @param seckillActivityId
     * @return
     */
    Order processSeckillReqBase(long userId, long seckillActivityId);

    /**
     * 锁定商品的库存
     *
     * @param id
     * @return
     */
    boolean lockStock(long id);

    /**
     * 库存扣减
     *
     * @param id
     * @return
     */
    boolean deductStock(long id);

    /**
     * 锁定的库存回补
     * @param id
     * @return
     */
    boolean revertStock(long id);

    /**
     * 缓存预热
     *  将秒杀信息写入Redis中
     * @param id
     */
    void  pushSeckillActivityInfoToCache(long id);

}
