package com.shangan.trade.order.db.dao;

import com.shangan.trade.order.db.model.Order;

/**
 * 订单数据库操作接口
 */
public interface OrderDao {
    /**
     * 插入一个订单
     *
     * @param order
     * @return
     */
    boolean insertOrder(Order order);

    boolean deleteOrder(long id);

    /**
     * 根据ID查询对应的订单
     *
     * @param id
     * @return
     */
    Order queryOrderById(long id);

    /**
     * 修改对应的订单信息
     *
     * @param order
     * @return
     */
    boolean updateOrder(Order order);
}
