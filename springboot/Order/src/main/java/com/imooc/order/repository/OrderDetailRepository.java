package com.imooc.order.repository;

import com.imooc.order.dataobject.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by hunter on 2018/4/16.
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
}