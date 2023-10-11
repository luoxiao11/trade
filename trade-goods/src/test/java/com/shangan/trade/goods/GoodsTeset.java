package com.shangan.trade.goods;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.goods.db.dao.GoodsDao;
import com.shangan.trade.goods.db.model.Goods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsTeset {
    @Autowired
    private GoodsDao goodsDao;
    @Test
    public void insertGoodTest(){

        Goods goods = new Goods();
        goods.setTitle("iphone 15 pro max");
        goods.setBrand("苹果 Apple");
        goods.setCategory("手机");
        goods.setNumber("NO123456");
        goods.setImage("test");
        goods.setDescription("iphone 15 pro max is very good");
        goods.setKeywords("苹果 手机 apple");
        goods.setSaleNum(0);
        goods.setAvailableStock(10000);
        goods.setPrice(999999);
        goods.setStatus(1);
        goods.setCreateTime(new Date());
        boolean insertResult = goodsDao.insertGoods(goods);

    }
    @Test
    public void deleteGoodsTest() {
        boolean deleteResult = goodsDao.deleteGoods(16);
        System.out.println(deleteResult);
    }

    @Test
    public void queryGoodsTest() {
        Goods goods = goodsDao.queryGoodsById(17);
        System.out.println(JSON.toJSONString(goods));
    }

    @Test
    public void updateGoods() {
        Goods goods = goodsDao.queryGoodsById(17);
        goods.setTitle(goods.getTitle() + " update");
        goodsDao.updateGoods(goods);
    }


}
