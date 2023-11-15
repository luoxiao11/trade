package com.shangan.trade.order.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单相关消息发送
 */
@Component
@Slf4j
public class OrderMessageSender {
    @Autowired
    private AmqpTemplate amqpTemplate;
    public void sendPayStatusCheckDelayMessage(String msg) {
        log.info("发送订单创建完成，支付状态确认消息:{}", msg);
        amqpTemplate.convertAndSend("order-event-exchange", "order.create", msg);
    }
}
