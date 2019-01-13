package com.cloudcmr.app.sales.domain;

import com.cloudcmr.app.article.domain.Article;
import com.cloudcmr.app.domain.User;
import com.cloudcmr.app.member.domain.Member;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales_session")
public class SalesSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private User seller;

    @Column(name = "cash_when_new")
    private BigDecimal cashWhenNew;

    @Column(name = "cash_when_closed")
    private BigDecimal cashWhenClosed;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "running_sale_id", referencedColumnName = "id")
    private Sale runningSale;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sales_session_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Sale> closedSales = new ArrayList<>();

    public SalesSession(User seller, BigDecimal cashWhenNew) {
        this.status = Status.NEW;
        this.seller = seller;
        this.runningSale = new Sale();
        this.cashWhenNew = new BigDecimal(cashWhenNew.toString());
    }

    public Status status() {
        return status;
    }

    public User seller() {
        return seller;
    }

    public Sale runningSale() {
        return runningSale;
    }

    public BigDecimal cashWhenNew() {
        return cashWhenNew;
    }

    public void sellArticle(Member member, Article article, int quantity) {
        status = Status.IN_PROGRESS;
        SoldArticle soldArticle = new SoldArticle(member, article, quantity);
        runningSale.addSoldArticle(soldArticle);
    }

    public void assignPayer(Member member, PaymentType paymentType) {
        runningSale.setPayer(member, paymentType);
    }

    public void assignSecondPayer(Member secondPayer, PaymentType paymentType, BigDecimal paymentAmount) {
        runningSale.setPayer2(secondPayer, paymentType, paymentAmount);
    }

    public void closeRunningSale() {
        if(!runningSale().getStatus().equals(Sale.Status.NEW)) {
            runningSale.close();
            closedSales.add(runningSale);
            runningSale = new Sale();
        }
    }

    public List<Sale> closedSales() {
        return closedSales;
    }

    public void removeArticle(long soldArticleId) {
        runningSale.removeArticle(soldArticleId);
    }

    public void close(BigDecimal cashWhenClosed) {
        if(!runningSale.getStatus().equals(Sale.Status.IN_PROGRESS)
            || runningSale.getStatus().equals(Sale.Status.IN_PROGRESS) && runningSale.getSoldArticles().isEmpty()) {
            status = Status.CLOSED;
            this.cashWhenClosed = cashWhenClosed;
            runningSale = null;
        }
    }

    public BigDecimal getCashWhenClosed() {
        return cashWhenClosed;
    }

    public void setCashWhenClosed(BigDecimal cashWhenClosed) {
        this.cashWhenClosed = cashWhenClosed;
    }

    public void removeSecondPayer() {
        this.runningSale().removePayer2();

    }


    public enum Status {
        IN_PROGRESS, CLOSED, NEW
    }


    // For Hibernate & Jackson. Do not use
    public SalesSession() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSeller() {
        return seller;
    }

    public BigDecimal getCashWhenNew() {
        return cashWhenNew;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Sale getRunningSale() {
        return runningSale;
    }

    public void setRunningSale(Sale runningSale) {
        this.runningSale = runningSale;
    }

    public List<Sale> getClosedSales() {
        return closedSales;
    }

    public void setClosedSales(List<Sale> closedSales) {
        this.closedSales = closedSales;
    }
}
