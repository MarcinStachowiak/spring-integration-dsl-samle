package com.ms.orderservice.service;

import com.ms.orderservice.integration.xsd.NewOrderEvent;
import com.ms.orderservice.integration.xsd.OrderReady;

public interface OrderReadyMapper {

    public OrderReady toOrderReady(NewOrderEvent newOrderEvent);

}
