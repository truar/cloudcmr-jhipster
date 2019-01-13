package com.cloudcmr.app.article.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cloudcmr.app.article.domain.CategoryArticle;
import com.cloudcmr.app.article.repository.CategoryArticleRepository;
import com.cloudcmr.app.web.rest.errors.BadRequestAlertException;
import com.cloudcmr.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CategoryArticle.
 */
@RestController
@RequestMapping("/api")
public class CategoryArticleResource {

    private final Logger log = LoggerFactory.getLogger(CategoryArticleResource.class);

    private static final String ENTITY_NAME = "categoryArticle";

    private final CategoryArticleRepository categoryArticleRepository;

    public CategoryArticleResource(CategoryArticleRepository categoryArticleRepository) {
        this.categoryArticleRepository = categoryArticleRepository;
    }

    /**
     * POST  /category-articles : Create a new categoryArticle.
     *
     * @param categoryArticle the categoryArticle to create
     * @return the ResponseEntity with getStatus 201 (Created) and with body the new categoryArticle, or with getStatus 400 (Bad Request) if the categoryArticle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/category-articles")
    @Timed
    public ResponseEntity<CategoryArticle> createCategoryArticle(@RequestBody CategoryArticle categoryArticle) throws URISyntaxException {
        log.debug("REST request to save CategoryArticle : {}", categoryArticle);
        if (categoryArticle.getId() != null) {
            throw new BadRequestAlertException("A new categoryArticle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategoryArticle result = categoryArticleRepository.save(categoryArticle);
        return ResponseEntity.created(new URI("/api/category-articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /category-articles : Updates an existing categoryArticle.
     *
     * @param categoryArticle the categoryArticle to update
     * @return the ResponseEntity with getStatus 200 (OK) and with body the updated categoryArticle,
     * or with getStatus 400 (Bad Request) if the categoryArticle is not valid,
     * or with getStatus 500 (Internal Server Error) if the categoryArticle couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/category-articles")
    @Timed
    public ResponseEntity<CategoryArticle> updateCategoryArticle(@RequestBody CategoryArticle categoryArticle) throws URISyntaxException {
        log.debug("REST request to update CategoryArticle : {}", categoryArticle);
        if (categoryArticle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CategoryArticle result = categoryArticleRepository.save(categoryArticle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, categoryArticle.getId().toString()))
            .body(result);
    }

    /**
     * GET  /category-articles : get all the categoryArticles.
     *
     * @return the ResponseEntity with getStatus 200 (OK) and the list of categoryArticles in body
     */
    @GetMapping("/category-articles")
    @Timed
    public List<CategoryArticle> getAllCategoryArticles() {
        log.debug("REST request to get all CategoryArticles");
        return categoryArticleRepository.findAll();
    }

    /**
     * GET  /category-articles/:id : get the "id" categoryArticle.
     *
     * @param id the id of the categoryArticle to retrieve
     * @return the ResponseEntity with getStatus 200 (OK) and with body the categoryArticle, or with getStatus 404 (Not Found)
     */
    @GetMapping("/category-articles/{id}")
    @Timed
    public ResponseEntity<CategoryArticle> getCategoryArticle(@PathVariable Long id) {
        log.debug("REST request to get CategoryArticle : {}", id);
        Optional<CategoryArticle> categoryArticle = categoryArticleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(categoryArticle);
    }

    /**
     * DELETE  /category-articles/:id : delete the "id" categoryArticle.
     *
     * @param id the id of the categoryArticle to delete
     * @return the ResponseEntity with getStatus 200 (OK)
     */
    @DeleteMapping("/category-articles/{id}")
    @Timed
    public ResponseEntity<Void> deleteCategoryArticle(@PathVariable Long id) {
        log.debug("REST request to delete CategoryArticle : {}", id);

        categoryArticleRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
