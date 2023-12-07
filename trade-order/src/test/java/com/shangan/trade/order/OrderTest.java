package com.shangan.trade.order;

import com.shangan.trade.order.db.dao.OrderDao;
import com.shangan.trade.order.db.model.Order;
import com.shangan.trade.order.utils.SnowflakeIdWorker;
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
    private SnowflakeIdWorker snowFlake = new SnowflakeIdWorker(6, 8);

    @Test
    public void insertOrderTest(){
        for (int i = 0; i < 100; i++) {
            System.out.println("Hello");
            Order order = new Order();
            order.setId(snowFlake.nextId() + 1);
            order.setUserId(123456788L + i);
            order.setGoodsId(12378L);
            order.setPayTime(new Date());
            order.setPayPrice(1999);
            order.setStatus(1);
            order.setActivityType(1);
            order.setCreateTime(new Date());
            boolean insertresult = orderDao.insertOrder(order);
            System.out.println(insertresult);
        }
    }
}
