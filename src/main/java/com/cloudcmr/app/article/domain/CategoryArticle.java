package com.cloudcmr.app.article.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.util.*;

/**
 * A CategoryArticle.
 */
@Entity
@Table(name = "category_article")
public class CategoryArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Article> articles = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public CategoryArticle code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public CategoryArticle description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public CategoryArticle articles(List<Article> articles) {
        this.articles = articles;
        return this;
    }

    public CategoryArticle addArticle(Article article) {
        this.articles.add(article);
        article.setCategoryArticle(this);
        return this;
    }

    public CategoryArticle removeArticle(Article article) {
        this.articles.remove(article);
        article.setCategoryArticle(null);
        return this;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CategoryArticle categoryArticle = (CategoryArticle) o;
        if (categoryArticle.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), categoryArticle.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CategoryArticle{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
