package com.shangan.trade.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.shangan"})
@MapperScan({"com.shangan.trade.goods.db.mappers"})
@SpringBootApplication
public class TradeGoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeGoodsApplication.class, args);
    }

}
