package com.cloudcmr.app.article.web.rest;

import com.cloudcmr.app.CloudcmrApp;

import com.cloudcmr.app.article.domain.CategoryArticle;
import com.cloudcmr.app.article.repository.CategoryArticleRepository;
import com.cloudcmr.app.article.web.rest.CategoryArticleResource;
import com.cloudcmr.app.web.rest.TestUtil;
import com.cloudcmr.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static com.cloudcmr.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CategoryArticleResource REST controller.
 *
 * @see CategoryArticleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryArticleResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private CategoryArticleRepository categoryArticleRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCategoryArticleMockMvc;

    private CategoryArticle categoryArticle;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CategoryArticleResource categoryArticleResource = new CategoryArticleResource(categoryArticleRepository);
        this.restCategoryArticleMockMvc = MockMvcBuilders.standaloneSetup(categoryArticleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryArticle createEntity(EntityManager em) {
        CategoryArticle categoryArticle = new CategoryArticle()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION);
        return categoryArticle;
    }

    @Before
    public void initTest() {
        categoryArticle = createEntity(em);
    }

    @Test
    @Transactional
    public void createCategoryArticle() throws Exception {
        int databaseSizeBeforeCreate = categoryArticleRepository.findAll().size();

        // Create the CategoryArticle
        restCategoryArticleMockMvc.perform(post("/api/category-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryArticle)))
            .andExpect(status().isCreated());

        // Validate the CategoryArticle in the database
        List<CategoryArticle> categoryArticleList = categoryArticleRepository.findAll();
        assertThat(categoryArticleList).hasSize(databaseSizeBeforeCreate + 1);
        CategoryArticle testCategoryArticle = categoryArticleList.get(categoryArticleList.size() - 1);
        assertThat(testCategoryArticle.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCategoryArticle.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createCategoryArticleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = categoryArticleRepository.findAll().size();

        // Create the CategoryArticle with an existing ID
        categoryArticle.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryArticleMockMvc.perform(post("/api/category-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryArticle)))
            .andExpect(status().isBadRequest());

        // Validate the CategoryArticle in the database
        List<CategoryArticle> categoryArticleList = categoryArticleRepository.findAll();
        assertThat(categoryArticleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCategoryArticles() throws Exception {
        // Initialize the database
        categoryArticleRepository.saveAndFlush(categoryArticle);

        // Get all the categoryArticleList
        restCategoryArticleMockMvc.perform(get("/api/category-articles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getCategoryArticle() throws Exception {
        // Initialize the database
        categoryArticleRepository.saveAndFlush(categoryArticle);

        // Get the categoryArticle
        restCategoryArticleMockMvc.perform(get("/api/category-articles/{id}", categoryArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(categoryArticle.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCategoryArticle() throws Exception {
        // Get the categoryArticle
        restCategoryArticleMockMvc.perform(get("/api/category-articles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategoryArticle() throws Exception {
        // Initialize the database
        categoryArticleRepository.saveAndFlush(categoryArticle);

        int databaseSizeBeforeUpdate = categoryArticleRepository.findAll().size();

        // Update the categoryArticle
        CategoryArticle updatedCategoryArticle = categoryArticleRepository.findById(categoryArticle.getId()).get();
        // Disconnect from session so that the updates on updatedCategoryArticle are not directly saved in db
        em.detach(updatedCategoryArticle);
        updatedCategoryArticle
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION);

        restCategoryArticleMockMvc.perform(put("/api/category-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCategoryArticle)))
            .andExpect(status().isOk());

        // Validate the CategoryArticle in the database
        List<CategoryArticle> categoryArticleList = categoryArticleRepository.findAll();
        assertThat(categoryArticleList).hasSize(databaseSizeBeforeUpdate);
        CategoryArticle testCategoryArticle = categoryArticleList.get(categoryArticleList.size() - 1);
        assertThat(testCategoryArticle.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCategoryArticle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingCategoryArticle() throws Exception {
        int databaseSizeBeforeUpdate = categoryArticleRepository.findAll().size();

        // Create the CategoryArticle

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryArticleMockMvc.perform(put("/api/category-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryArticle)))
            .andExpect(status().isBadRequest());

        // Validate the CategoryArticle in the database
        List<CategoryArticle> categoryArticleList = categoryArticleRepository.findAll();
        assertThat(categoryArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCategoryArticle() throws Exception {
        // Initialize the database
        categoryArticleRepository.saveAndFlush(categoryArticle);

        int databaseSizeBeforeDelete = categoryArticleRepository.findAll().size();

        // Get the categoryArticle
        restCategoryArticleMockMvc.perform(delete("/api/category-articles/{id}", categoryArticle.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CategoryArticle> categoryArticleList = categoryArticleRepository.findAll();
        assertThat(categoryArticleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryArticle.class);
        CategoryArticle categoryArticle1 = new CategoryArticle();
        categoryArticle1.setId(1L);
        CategoryArticle categoryArticle2 = new CategoryArticle();
        categoryArticle2.setId(categoryArticle1.getId());
        assertThat(categoryArticle1).isEqualTo(categoryArticle2);
        categoryArticle2.setId(2L);
        assertThat(categoryArticle1).isNotEqualTo(categoryArticle2);
        categoryArticle1.setId(null);
        assertThat(categoryArticle1).isNotEqualTo(categoryArticle2);
    }
}
