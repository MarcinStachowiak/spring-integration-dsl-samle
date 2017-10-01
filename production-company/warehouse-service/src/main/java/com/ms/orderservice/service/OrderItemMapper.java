package com.ms.orderservice.service;

import com.ms.orderservice.ds.OrderItemDs;
import com.ms.orderservice.integration.xsd.NewOrderEvent;

public interface OrderItemMapper {

    public OrderItemDs toOrderItemDs(NewOrderEvent.Item item);
}
