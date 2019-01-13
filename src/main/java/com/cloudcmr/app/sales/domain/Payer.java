package com.cloudcmr.app.sales.domain;

import com.cloudcmr.app.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payer")
public class Payer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentType paymentType;

    public Payer(Member payer, PaymentType paymentType) {
        this(payer, paymentType, BigDecimal.ZERO);
    }

    public Payer(Member payer, PaymentType paymentType, BigDecimal paymentAmount) {
        this.member = payer;
        this.paymentType = paymentType;
        this.paymentAmount = paymentAmount;
    }


    public Payer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return member.getFirstName();
    }

    public String getLastName() {
        return member.getLastName();
    }

    public String getUscaNumber() {
        return member.getUscaNumber();
    }

    public long getMemberId() {
        return member.getId();
    }

    @JsonIgnore
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
}
