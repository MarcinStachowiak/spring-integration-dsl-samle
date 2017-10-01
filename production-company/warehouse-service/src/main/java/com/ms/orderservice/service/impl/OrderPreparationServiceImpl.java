package com.ms.orderservice.service.impl;

import com.ms.orderservice.ds.OrderItemDs;
import com.ms.orderservice.service.OrderPreparationService;
import org.springframework.stereotype.Service;

@Service
public class OrderPreparationServiceImpl implements OrderPreparationService {

    @Override
    public void prepare(OrderItemDs orderItemDs, boolean priorityRealization) {
        try {
            Thread.sleep(priorityRealization ? 2000:4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
