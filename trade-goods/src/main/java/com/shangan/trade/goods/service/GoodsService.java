package com.shangan.trade.goods.service;

import com.shangan.trade.goods.db.model.Goods;

public interface GoodsService {
    /**
     * 插入一个商品
     *
     * @param goods
     * @return
     */
    boolean insertGoods(Goods goods);

    /**
     * 查询商品信息
     *
     * @param id
     * @return
     */
    Goods queryGoodsById(long id);

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


