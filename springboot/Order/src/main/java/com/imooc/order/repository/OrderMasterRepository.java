package com.imooc.order.repository;

import com.imooc.order.dataobject.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by hunter on 2018/4/16.
 */
public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {
}
