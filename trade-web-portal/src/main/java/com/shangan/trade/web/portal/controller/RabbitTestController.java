package com.shangan.trade.web.portal.controller;

import com.shangan.trade.order.mq.OrderMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;


@Controller
public class RabbitTestController {

    @Autowired
    private OrderMessageSender orderMessageSender;

    @ResponseBody
    @RequestMapping("/delayTest")
    public String deadTest() {
        orderMessageSender.sendPayStatusCheckDelayMessage("发送的时间:" + LocalDateTime.now() + "内容：延迟队列测试");
        return "send ok";
    }
}

