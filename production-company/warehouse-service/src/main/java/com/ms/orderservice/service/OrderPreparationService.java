package com.ms.orderservice.service;


import com.ms.orderservice.ds.OrderItemDs;

public interface OrderPreparationService {
    public void prepare(OrderItemDs orderItemDs, boolean priorityRealization);
}
