package com.imooc.order.service;

import com.imooc.order.dto.OrderDTO;

/**
 * Created by hunter on 2018/4/16.
 */
public interface OrderService {
    /**
     * 创建订单
     * @param orderDTO
     * @return
     */
    OrderDTO create(OrderDTO orderDTO);
}
