package com.shangan.trade.web.manager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.shangan"})
@MapperScan({"com.shangan.trade.goods.db.mappers","com.shangan.trade.lightning.deal.db.mappers"})
@SpringBootApplication
public class TradeWebManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeWebManagerApplication.class, args);
    }

}
