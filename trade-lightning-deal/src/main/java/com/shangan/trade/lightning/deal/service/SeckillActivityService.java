package com.shangan.trade.lightning.deal.service;

import com.shangan.trade.lightning.deal.db.model.SeckillActivity;
import com.shangan.trade.order.db.model.Order;

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

}
