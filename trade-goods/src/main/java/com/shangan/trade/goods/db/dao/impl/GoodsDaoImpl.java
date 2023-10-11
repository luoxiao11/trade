package com.shangan.trade.goods.db.dao.impl;

import com.shangan.trade.goods.db.dao.GoodsDao;
import com.shangan.trade.goods.db.mappers.GoodsMapper;
import com.shangan.trade.goods.db.model.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 商品数据库操作
 */
@Repository
public class GoodsDaoImpl implements GoodsDao {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public boolean insertGoods(Goods goods) {
        int result = goodsMapper.insert(goods);
        //大于0 表示插入成功
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteGoods(long id) {
        int result = goodsMapper.deleteByPrimaryKey(id);
        return result > 0;
    }

    @Override
    public Goods queryGoodsById(long id) {
        Goods goods = goodsMapper.selectByPrimaryKey(id);
        return goods;
    }

    @Override
    public boolean updateGoods(Goods goods) {
        int result = goodsMapper.updateByPrimaryKey(goods);
        return result > 0;
    }
}
