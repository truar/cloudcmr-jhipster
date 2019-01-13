package com.cloudcmr.app.sales.domain;

import com.cloudcmr.app.member.domain.Member;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sale_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<SoldArticle> soldArticles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payer_id", referencedColumnName = "id")
    private Payer payer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payer2_id", referencedColumnName = "id")
    private Payer payer2;

    public Sale() {
        status = Status.NEW;
        soldArticles = new ArrayList<>(10);
    }

    public Status getStatus() {
        return status;
    }

    public void addSoldArticle(SoldArticle soldArticle) {
        status = Status.IN_PROGRESS;
        for(SoldArticle soldArticle1 : soldArticles) {
            if(soldArticle1.getMember().equals(soldArticle.getMember()) && soldArticle1.getArticle().equals(soldArticle.getArticle())) {
                soldArticle1.setQuantity(soldArticle1.getQuantity() + soldArticle.getQuantity());
                return;
            }
        }
        soldArticles.add(soldArticle);
        ventilateTotal();
    }

    public List<SoldArticle> getSoldArticles() {
        return soldArticles;
    }

    public void setPayer(Member payer, PaymentType paymentType) {
        status = Status.IN_PROGRESS;
        this.payer = new Payer(payer, paymentType);
        ventilateTotal();
    }

    public Payer getPayer() {
        return payer;
    }

    public void close() {
        status = Status.CLOSED;
    }

    public void removeArticle(long soldArticleId) {
        soldArticles.removeIf(soldArticle -> soldArticle.getId().equals(soldArticleId));
        ventilateTotal();
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for(SoldArticle soldArticle : soldArticles) {
            total = total.add(soldArticle.getArticle().getPrice().multiply(new BigDecimal(soldArticle.getQuantity())));
        }
        return total;
    }

    private void ventilateTotal() {
        BigDecimal paymentAmountPayer2 = payer2 != null ? payer2.getPaymentAmount() : BigDecimal.ZERO;
        if (payer != null) {
            payer.setPaymentAmount(getTotal().subtract(paymentAmountPayer2));
        }
    }

    public void setPayer2(Member secondPayer, PaymentType paymentType, BigDecimal paymentAmount) {
        payer2 = new Payer(secondPayer, paymentType, paymentAmount);
        ventilateTotal();
    }

    public Payer getPayer2() {
        return payer2;
    }

    public void removePayer2() {
        payer2 = null;
        ventilateTotal();
    }

    public enum Status {
        IN_PROGRESS, CLOSED, NEW
    }

    // DO NOT USE. For Hibernate & Jackson

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setSoldArticles(List<SoldArticle> soldArticles) {
        this.soldArticles = soldArticles;
    }
}
