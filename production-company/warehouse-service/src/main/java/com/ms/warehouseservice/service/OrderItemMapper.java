package com.ms.warehouseservice.service;

import com.ms.warehouseservice.ds.OrderItemDs;
import com.ms.warehouseservice.integration.xsd.NewOrderEvent;

public interface OrderItemMapper {

    public OrderItemDs toOrderItemDs(NewOrderEvent.Item item);
}
