package com.cloudcmr.app.member.web.rest;

import com.cloudcmr.app.member.domain.Member;
import com.cloudcmr.app.member.repository.MemberRepository;
import com.cloudcmr.app.member.service.MemberServiceCmr;
import com.cloudcmr.app.member.web.rest.errors.IncorrectExportFileException;
import com.cloudcmr.app.member.web.rest.fileParser.CSVParser;
import com.cloudcmr.app.member.web.rest.fileParser.Parser;
import com.cloudcmr.app.web.rest.errors.BadRequestAlertException;
import com.cloudcmr.app.web.rest.util.HeaderUtil;
import com.cloudcmr.app.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Member.
 */
@RestController
@RequestMapping("/api")
public class MemberResource {

    private final Logger log = LoggerFactory.getLogger(MemberResource.class);

    private static final String ENTITY_NAME = "member";

    private final MemberRepository memberRepository;

    private MemberServiceCmr memberService;

    public MemberResource(MemberRepository memberRepository, MemberServiceCmr memberService) {
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    /**
     * POST  /members : Create a new member.
     *
     * @param member the member to create
     * @return the ResponseEntity with getStatus 201 (Created) and with body the new member, or with getStatus 400 (Bad Request) if the member has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/members")
    @Timed
    public ResponseEntity<Member> createMember(@Valid @RequestBody Member member) throws URISyntaxException {
        log.debug("REST request to save Member : {}", member);
        if (member.getId() != null) {
            throw new BadRequestAlertException("A new member cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Member result = memberRepository.save(member);
        return ResponseEntity.created(new URI("/api/members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /members : Updates an existing member.
     *
     * @param member the member to update
     * @return the ResponseEntity with getStatus 200 (OK) and with body the updated member,
     * or with getStatus 400 (Bad Request) if the member is not valid,
     * or with getStatus 500 (Internal Server Error) if the member couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/members")
    @Timed
    public ResponseEntity<Member> updateMember(@Valid @RequestBody Member member) throws URISyntaxException {
        log.debug("REST request to update Member : {}", member);
        if (member.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Member result = memberRepository.save(member);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, member.getId().toString()))
            .body(result);
    }

    /**
     * GET  /members : get all the members.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with getStatus 200 (OK) and the list of members in body
     */
    @GetMapping("/members")
    @Timed
    public ResponseEntity<List<Member>> getAllMembers(Pageable pageable, Searchable searchable) {
        log.info("REST request to get a page of Members. Pageable: {} - SearchText: {}", pageable, searchable);
        Page<Member> page;
        if(searchable.isActive()) {
            page = memberRepository.searchMembers(searchable.getSearchText(), pageable);
        } else {
            page = memberRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/members");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /members/:id : get the "id" member.
     *
     * @param id the id of the member to retrieve
     * @return the ResponseEntity with getStatus 200 (OK) and with body the member, or with getStatus 404 (Not Found)
     */
    @GetMapping("/members/{id}")
    @Timed
    public ResponseEntity<Member> getMember(@PathVariable Long id) {
        log.debug("REST request to get Member : {}", id);
        Optional<Member> member = memberRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(member);
    }

    /**
     * DELETE  /members/:id : delete the "id" member.
     *
     * @param id the id of the member to delete
     * @return the ResponseEntity with getStatus 200 (OK)
     */
    @DeleteMapping("/members/{id}")
    @Timed
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        log.debug("REST request to delete Member : {}", id);

        memberRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/members/file")
    @Timed
    public ResponseEntity<Void> readMemberFile(@RequestParam("exportFFS") MultipartFile exportFFS) {
        log.info("REST request to upload the members of a file : {}", exportFFS.getOriginalFilename());
        try {
            Parser parser = new CSVParser(exportFFS.getInputStream());
            memberService.importMembersFromSFF(parser.parse());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new IncorrectExportFileException();
        }
        return ResponseEntity.ok().build();
    }

}
