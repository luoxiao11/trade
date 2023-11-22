package com.shangan.trade.goods.service.impl;

import com.shangan.trade.goods.db.dao.GoodsDao;
import com.shangan.trade.goods.db.model.Goods;
import com.shangan.trade.goods.service.GoodsService;
import com.shangan.trade.goods.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private SearchService searchService;
    @Override
    public boolean insertGoods(Goods goods) {
        boolean res = goodsDao.insertGoods(goods);
        //添加商品到ES中
        searchService.addGoodsToES(goods);
        return res;
    }

    @Override
    public Goods queryGoodsById(long id) {
        return goodsDao.queryGoodsById(id);
    }

    @Override
    public boolean lockStock(long id) {
        return goodsDao.lockStock(id);
    }
    @Override
    public boolean deductStock(long id) {
        return goodsDao.deductStock(id);
    }

    @Override
    public boolean revertStock(long id) {
        return goodsDao.revertStock(id);
    }

}
