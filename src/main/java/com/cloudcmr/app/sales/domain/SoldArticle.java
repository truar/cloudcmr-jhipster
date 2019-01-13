package com.cloudcmr.app.sales.domain;

import com.cloudcmr.app.article.domain.Article;
import com.cloudcmr.app.member.domain.Member;

import javax.persistence.*;

@Entity
@Table(name = "sold_article")
public class SoldArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
    @OneToOne
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Article article;
    @Column
    private int quantity;

    public SoldArticle() {
    }

    public SoldArticle(Member member, Article article, int quantity) {
        this.member = member;
        this.article = article;
        this.quantity = quantity;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
