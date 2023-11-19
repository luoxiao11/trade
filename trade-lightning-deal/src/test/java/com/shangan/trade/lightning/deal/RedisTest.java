package com.shangan.trade.lightning.deal;

import com.shangan.trade.lightning.deal.utils.RedisWorker;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    public RedisWorker redisWorker;
    @Test
    public void insert(){
        redisWorker.setValue("name", "hello");
        System.out.println(redisWorker.getValueByKey("name"));
    }
    @Test
    public void get(){
        String val = redisWorker.getValueByKey("name");
        System.out.println(val);
    }
    @Test
    public void setStockTest(){
        //stock:秒杀活动 ID 库存数
        redisWorker.setValue("stock:4", 100L);
        System.out.println(redisWorker.getValueByKey("stock:4"));
    }
    @Test
    public void stockCheckTest(){
        redisWorker.stockDeductCheck("stock:4");
        System.out.println(redisWorker.getValueByKey("stock:4"));
    }

    @Test
    public void getCacheTest(){
        System.out.println(redisWorker.getValueByKey("stock:4"));
        System.out.println(redisWorker.getValueByKey("seckillActivity:4"));
        System.out.println(redisWorker.getValueByKey("seckillActivity_goods:26"));


    }

}
