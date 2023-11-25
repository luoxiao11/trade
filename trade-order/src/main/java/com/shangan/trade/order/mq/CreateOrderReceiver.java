package com.shangan.trade.order.mq;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.order.db.dao.OrderDao;
import com.shangan.trade.order.db.model.Order;
import com.shangan.trade.order.service.LimitBuyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateOrderReceiver {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderMessageSender orderMessageSender;

    @Autowired
    private LimitBuyService limitBuyService;

    /**
     * 创建订单消息处理
     *
     * @param message
     */
    @RabbitListener(queues = "create.order.queue")
    public void process(String message) {
        log.info("创建订单消息处理，接收到消息内容:{}", message);
        Order order = JSON.parseObject(message, Order.class);
        //1.生成订单
        boolean insertResult = orderDao.insertOrder(order);
        if (!insertResult) {
            log.error("order insert error order={}", JSON.toJSONString(order));
            throw new RuntimeException("订单生成失败");
        }

        //2.发送订单支付状态检查消息
        orderMessageSender.sendPayStatusCheckDelayMessage(JSON.toJSONString(order));

        //3.判断如果是秒杀活动，加入限购名单
        if (order.getActivityType() == 1) {
            limitBuyService.addLimitMember(order.getActivityId(), order.getUserId());
        }
    }
}

