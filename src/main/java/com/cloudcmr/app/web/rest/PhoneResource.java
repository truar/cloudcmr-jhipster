package com.cloudcmr.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cloudcmr.app.domain.Phone;
import com.cloudcmr.app.repository.PhoneRepository;
import com.cloudcmr.app.web.rest.errors.BadRequestAlertException;
import com.cloudcmr.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Phone.
 */
@RestController
@RequestMapping("/api")
public class PhoneResource {

    private final Logger log = LoggerFactory.getLogger(PhoneResource.class);

    private static final String ENTITY_NAME = "phone";

    private final PhoneRepository phoneRepository;

    public PhoneResource(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    /**
     * POST  /phones : Create a new phone.
     *
     * @param phone the phone to create
     * @return the ResponseEntity with status 201 (Created) and with body the new phone, or with status 400 (Bad Request) if the phone has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/phones")
    @Timed
    public ResponseEntity<Phone> createPhone(@Valid @RequestBody Phone phone) throws URISyntaxException {
        log.debug("REST request to save Phone : {}", phone);
        if (phone.getId() != null) {
            throw new BadRequestAlertException("A new phone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Phone result = phoneRepository.save(phone);
        return ResponseEntity.created(new URI("/api/phones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /phones : Updates an existing phone.
     *
     * @param phone the phone to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated phone,
     * or with status 400 (Bad Request) if the phone is not valid,
     * or with status 500 (Internal Server Error) if the phone couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/phones")
    @Timed
    public ResponseEntity<Phone> updatePhone(@Valid @RequestBody Phone phone) throws URISyntaxException {
        log.debug("REST request to update Phone : {}", phone);
        if (phone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Phone result = phoneRepository.save(phone);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, phone.getId().toString()))
            .body(result);
    }

    /**
     * GET  /phones : get all the phones.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of phones in body
     */
    @GetMapping("/phones")
    @Timed
    public List<Phone> getAllPhones() {
        log.debug("REST request to get all Phones");
        return phoneRepository.findAll();
    }

    /**
     * GET  /phones/:id : get the "id" phone.
     *
     * @param id the id of the phone to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the phone, or with status 404 (Not Found)
     */
    @GetMapping("/phones/{id}")
    @Timed
    public ResponseEntity<Phone> getPhone(@PathVariable Long id) {
        log.debug("REST request to get Phone : {}", id);
        Optional<Phone> phone = phoneRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(phone);
    }

    /**
     * DELETE  /phones/:id : delete the "id" phone.
     *
     * @param id the id of the phone to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/phones/{id}")
    @Timed
    public ResponseEntity<Void> deletePhone(@PathVariable Long id) {
        log.debug("REST request to delete Phone : {}", id);

        phoneRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
