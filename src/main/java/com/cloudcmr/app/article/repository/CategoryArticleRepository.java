package com.cloudcmr.app.article.repository;

import com.cloudcmr.app.article.domain.CategoryArticle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CategoryArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryArticleRepository extends JpaRepository<CategoryArticle, Long> {

}
