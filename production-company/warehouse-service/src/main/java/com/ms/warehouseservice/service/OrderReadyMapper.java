package com.ms.warehouseservice.service;

import com.ms.warehouseservice.integration.xsd.NewOrderEvent;
import com.ms.warehouseservice.integration.xsd.OrderReady;

public interface OrderReadyMapper {

    public OrderReady toOrderReady(NewOrderEvent newOrderEvent);

}
