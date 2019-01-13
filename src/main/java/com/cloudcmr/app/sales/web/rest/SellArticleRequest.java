package com.cloudcmr.app.sales.web.rest;

public class SellArticleRequest {
    private long articleId;
    private long memberId;
    private int quantity;

    public SellArticleRequest(long articleId, long memberId, int quantity) {
        this.articleId = articleId;
        this.memberId = memberId;
        this.quantity = quantity;
    }

    public SellArticleRequest() {
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
