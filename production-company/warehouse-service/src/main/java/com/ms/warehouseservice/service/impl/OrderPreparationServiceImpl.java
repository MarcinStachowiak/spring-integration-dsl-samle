package com.ms.warehouseservice.service.impl;

import com.ms.warehouseservice.ds.OrderItemDs;
import com.ms.warehouseservice.service.OrderPreparationService;
import org.springframework.stereotype.Service;

@Service
public class OrderPreparationServiceImpl implements OrderPreparationService{
    @Override
    public void prepare(OrderItemDs orderItemDs, boolean priorityRealization) {

    }
}
