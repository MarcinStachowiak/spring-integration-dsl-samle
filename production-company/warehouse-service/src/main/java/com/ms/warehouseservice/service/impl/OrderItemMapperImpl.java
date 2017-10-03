package com.ms.warehouseservice.service.impl;

import com.ms.warehouseservice.ds.OrderItemDs;
import com.ms.warehouseservice.service.OrderItemMapper;
import com.ms.warehouseservice.integration.xsd.NewOrderEvent;
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
