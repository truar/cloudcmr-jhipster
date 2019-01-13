package com.cloudcmr.app.sales.web.rest;

import com.cloudcmr.app.sales.domain.PaymentType;

import java.math.BigDecimal;

public class AssignPayerRequest {
    private long payerId;
    private BigDecimal paymentAmount;
    private PaymentType paymentType;

    public void setPayerId(long payerId) {
        this.payerId = payerId;
    }

    public long getPayerId() {
        return payerId;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }
}
