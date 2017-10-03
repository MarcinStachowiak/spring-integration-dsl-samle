package com.ms.warehouseservice.service;


import com.ms.warehouseservice.ds.OrderItemDs;

public interface OrderPreparationService {
     void prepare(OrderItemDs orderItemDs, boolean priorityRealization);
}
