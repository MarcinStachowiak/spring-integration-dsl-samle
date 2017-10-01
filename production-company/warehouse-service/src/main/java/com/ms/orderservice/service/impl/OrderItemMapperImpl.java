package com.ms.orderservice.service.impl;

import com.ms.orderservice.ds.OrderItemDs;
import com.ms.orderservice.integration.xsd.NewOrderEvent;
import com.ms.orderservice.service.OrderItemMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderItemMapperImpl implements OrderItemMapper {

    @Override
    public OrderItemDs toOrderItemDs(NewOrderEvent.Item item) {
        OrderItemDs ds = new OrderItemDs();
        ds.setId(item.getId());
        ds.setItemCode(item.getItemCode());
        ds.setItemName(item.getItemName());
        ds.setItemDescription(item.getItemDescription());
        ds.setPrice(item.getPrice());
        ds.setNumberOfItems(item.getNumberOfItems());
        return ds;
    }
}
