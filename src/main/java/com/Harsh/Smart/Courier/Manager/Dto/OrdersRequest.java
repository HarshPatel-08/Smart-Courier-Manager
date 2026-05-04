package com.Harsh.Smart.Courier.Manager.Dto;

import java.time.LocalDate;

public class OrdersRequest {

    private Integer senderLocationId;
    private Integer receiverLocationId;
    private LocalDate expectedDeliveryDate;

    public Integer getSenderLocationId() {
        return senderLocationId;
    }

    public void setSenderLocationId(Integer senderLocationId) {
        this.senderLocationId = senderLocationId;
    }

    public Integer getReceiverLocationId() {
        return receiverLocationId;
    }

    public void setReceiverLocationId(Integer receiverLocationId) {
        this.receiverLocationId = receiverLocationId;
    }

    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }
}
