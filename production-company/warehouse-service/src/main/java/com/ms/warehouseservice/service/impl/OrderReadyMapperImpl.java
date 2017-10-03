package com.ms.warehouseservice.service.impl;

import com.ms.warehouseservice.service.OrderReadyMapper;
import com.ms.warehouseservice.integration.xsd.NewOrderEvent;
import com.ms.warehouseservice.integration.xsd.OrderReady;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class OrderReadyMapperImpl implements OrderReadyMapper {

    @Override
    public OrderReady toOrderReady(NewOrderEvent newOrderEvent) {
        OrderReady orderReady = new OrderReady();
        NewOrderEvent.Address address = newOrderEvent.getAddress();
        orderReady.setEventDateTime(Calendar.getInstance());
        if(address!=null){
            orderReady.setRecipientName(address.getRecipientName());
            orderReady.setEMail(address.getEMail());
        }
        return orderReady;
    }
}
