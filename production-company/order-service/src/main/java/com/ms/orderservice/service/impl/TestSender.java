package com.ms.orderservice.service.impl;


import com.ms.orderservice.configuration.integration.IntegrationFlowKeys;
import com.ms.orderservice.integration.xsd.NewOrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class TestSender {

    @Autowired
    @Qualifier(IntegrationFlowKeys.NEW_ORDER_OUT_CHANNEL)
    MessageChannel newOrderOutChannel;


    public void send(){
        NewOrderEvent newOrderEvent=new NewOrderEvent();
        newOrderEvent.setEventDateTime(Calendar.getInstance());
        newOrderEvent.setPriorityRealization(false);
        NewOrderEvent.Item item =new NewOrderEvent.Item();
        item.setId(2);
        item.setItemCode(23232);
        item.setItemName("sss1");
        item.setItemDescription("ss");
        item.setNumberOfItems(1);
        item.setPrice("45,43");

        NewOrderEvent.Item item2=new NewOrderEvent.Item();
        item2.setId(2);
        item2.setItemCode(23232);
        item2.setItemName("sss2");
        item2.setItemDescription("ss");
        item2.setNumberOfItems(1);
        item2.setPrice("45,43");

        NewOrderEvent.Item item3 =new NewOrderEvent.Item();
        item3.setId(2);
        item3.setItemCode(23232);
        item3.setItemName("sss3");
        item3.setItemDescription("ss");
        item3.setNumberOfItems(1);
        item3.setPrice("45,43");


        newOrderEvent.getItems().add(item);
        newOrderEvent.getItems().add(item2);
        newOrderEvent.getItems().add(item3);

        NewOrderEvent.Address addess=new NewOrderEvent.Address();
        addess.setCity("city");
        addess.setHouseNumber("32");
        addess.setPostalCode("00-111");
        addess.setRecipientName("Jan Kowalski2");
        addess.setStreet("aaa");
        addess.setEMail("marcin.stachowiak.ms@gmail.com");


        newOrderEvent.setAddress(addess);

        Message message = MessageBuilder.withPayload(newOrderEvent)
                .setHeader("customId", "1")
                .setHeader("sentTime", "2")
                .build();
int i=0;
while (i<1) {
    newOrderOutChannel.send(message);
    i++;
}
    }
}
