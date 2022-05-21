package com.vasyl.kafka.model;

import lombok.Data;

@Data
public class OrderEnvelop {

    String xmlOrderKey;
    String xmlOrderValue;

    String orderTag;
    Order validOrder;
}
