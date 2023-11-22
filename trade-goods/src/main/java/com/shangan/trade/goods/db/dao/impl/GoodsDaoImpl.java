package com.shangan.trade.goods.db.dao.impl;

import com.shangan.trade.goods.db.dao.GoodsDao;
import com.shangan.trade.goods.db.mappers.GoodsMapper;
import com.shangan.trade.goods.db.model.Goods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 商品数据库操作
 */
@Repository
@Slf4j
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

    @Override
    public boolean lockStock(long id) {
        int result = goodsMapper.lockStock(id);
        //大于0 表示插入成功
        if (result > 0) {
            log.error("锁定库存失败");
            return true;
        }
        return false;
    }

    @Override
    public boolean deductStock(long id) {
        int result = goodsMapper.deductStock(id);
        //大于0 表示插入成功
        if (result < 1) {
            log.error("锁定扣减失败");
            return false;
        }
        return true;
    }

    @Override
    public boolean revertStock(long id) {
        int result = goodsMapper.revertStock(id);
        if (result < 1) {
            log.error("锁定回补失败");
            return false;
        }
        return true;
    }
}
