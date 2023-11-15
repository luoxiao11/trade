package com.shangan.trade.order;

import com.shangan.trade.order.db.dao.OrderDao;
import com.shangan.trade.order.db.model.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {
    @Autowired
    private OrderDao orderDao;

    @Test
    public void insertOrderTest(){
        Order order = new Order();
        order.setUserId(123456L);
        order.setGoodsId(123L);
        order.setPayTime(new Date());
        order.setPayPrice(16500);
        order.setStatus(0);
        boolean insertresult = orderDao.insertOrder(order);
        System.out.println(insertresult);
    }
}
