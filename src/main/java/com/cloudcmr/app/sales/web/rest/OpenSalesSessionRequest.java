package com.cloudcmr.app.sales.web.rest;

import java.math.BigDecimal;

public class OpenSalesSessionRequest {

    private BigDecimal cash;

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }
}
