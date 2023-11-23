package com.shangan.trade.order.mq;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.goods.service.GoodsService;
import com.shangan.trade.order.db.dao.OrderDao;
import com.shangan.trade.order.db.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


/**
 * 消息消费类，订单支付状态交易
 */

@Component
@Slf4j
public class OrderPayCheckReceiver {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private GoodsService goodsService;

    /**
     * 消息处理
     *
     * @param message
     */
    @Transactional(rollbackFor = Exception.class)
    @RabbitListener(queues = "order.pay.status.check.queue")
    public void process(String message) {
        log.info("接收到消息内容:{}", message);
        Order order = JSON.parseObject(message, Order.class);
        /*
         * 状态:0,没有可用库存订单创建失败;1,已创建，等待付款;2 已支付,等待发货;99 订单关闭，超时未付款
         */
        //1.查询订单信息
        Order orderInfo = orderDao.queryOrderById(order.getId());
        if (orderInfo.getStatus() == 1) {
            //2.判断是否完成支付
            log.info("订单{}超时支付，关闭订单", orderInfo.getId());
            orderInfo.setStatus(99);
            //3.更新订单状态为关闭
            orderDao.updateOrder(orderInfo);
            //4.将锁定的库存回补
            goodsService.revertStock(orderInfo.getGoodsId());
        }
    }
//    public void process(String msg) {
//        log.info("接受时间："+ LocalDateTime.now() +"接收内容" + msg);
//    }

}