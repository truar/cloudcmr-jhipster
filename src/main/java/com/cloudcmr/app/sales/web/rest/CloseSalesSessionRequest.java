package com.cloudcmr.app.sales.web.rest;

import java.math.BigDecimal;

public class CloseSalesSessionRequest {
    private BigDecimal cash;

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public BigDecimal getCash() {
        return cash;
    }
}
