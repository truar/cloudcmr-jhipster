package com.cloudcmr.app.member.web.rest;

import com.cloudcmr.app.CloudcmrApp;
import com.cloudcmr.app.domain.enumeration.GenderType;
import com.cloudcmr.app.member.domain.Address;
import com.cloudcmr.app.member.domain.Member;
import com.cloudcmr.app.member.domain.Phone;
import com.cloudcmr.app.member.repository.MemberRepository;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.cloudcmr.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Test class for the MemberResourceCmr REST controller.
 *
 * @see MemberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudcmrApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final GenderType DEFAULT_GENDER = GenderType.MALE;
    private static final GenderType UPDATED_GENDER = GenderType.FEMALE;

    private static final String DEFAULT_USCA_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_USCA_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String DEFAULT_LICENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LICENCE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_LICENCE_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LICENCE_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SUBSCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SUBSCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_2 = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_2 = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEASON = 1;
    private static final Integer UPDATED_SEASON = 2;

    private static final String DEFAULT_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_3 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_3 = "BBBBBBBBBB";

    private static final String DEFAULT_ZIPCODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIPCODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "02";
    private static final String UPDATED_PHONE_NUMBER = "06";

    private static final boolean DEFAULT_SFF = false;


    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private Validator validator;

    private MockMvc restMemberMockMvc;

    private Member member;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MemberResource memberResource = new MemberResource(memberRepository, null);
        this.restMemberMockMvc = MockMvcBuilders.standaloneSetup(memberResource)
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
    public static Member createEntity(EntityManager em) {
        Member member = new Member()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .birthDate(DEFAULT_BIRTH_DATE)
            .gender(DEFAULT_GENDER)
            .uscaNumber(DEFAULT_USCA_NUMBER)
            .comment(DEFAULT_COMMENT)
            .licenceNumber(DEFAULT_LICENCE_NUMBER)
            .licenceCreationDate(DEFAULT_LICENCE_CREATION_DATE)
            .subscription(DEFAULT_SUBSCRIPTION)
            .email2(DEFAULT_EMAIL_2)
            .season(DEFAULT_SEASON)
            .ssf(DEFAULT_SFF);
        return member;
    }

    @Before
    public void initTest() {
        member = createEntity(em);
    }

    @Test
    @Transactional
    public void createMember() throws Exception {
        int databaseSizeBeforeCreate = memberRepository.findAll().size();

        // Create the Member
        restMemberMockMvc.perform(post("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(member)))
            .andExpect(status().isCreated());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeCreate + 1);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testMember.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testMember.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMember.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testMember.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testMember.getUscaNumber()).isEqualTo(DEFAULT_USCA_NUMBER);
        assertThat(testMember.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testMember.getLicenceNumber()).isEqualTo(DEFAULT_LICENCE_NUMBER);
        assertThat(testMember.getLicenceCreationDate()).isEqualTo(DEFAULT_LICENCE_CREATION_DATE);
        assertThat(testMember.getSubscription()).isEqualTo(DEFAULT_SUBSCRIPTION);
        assertThat(testMember.getEmail2()).isEqualTo(DEFAULT_EMAIL_2);
        assertThat(testMember.getSeason()).isEqualTo(DEFAULT_SEASON);
    }

    @Test
    @Transactional
    public void createMemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = memberRepository.findAll().size();

        // Create the Member with an existing ID
        member.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberMockMvc.perform(post("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(member)))
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMembers() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList
        restMemberMockMvc.perform(get("/api/members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].uscaNumber").value(hasItem(DEFAULT_USCA_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].licenceNumber").value(hasItem(DEFAULT_LICENCE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].licenceCreationDate").value(hasItem(DEFAULT_LICENCE_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].subscription").value(hasItem(DEFAULT_SUBSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].email2").value(hasItem(DEFAULT_EMAIL_2.toString())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON)))
            .andExpect(jsonPath("$.[*].sff").value(hasItem(DEFAULT_SFF)));
    }

    @Test
    @Transactional
    public void shouldFindNoMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        restMemberMockMvc.perform(get("/api/members?searchText=NotExist"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("[]"));
    }

    @Test
    @Transactional
    public void shouldFindMemberThatMatchTheExactLastName() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        restMemberMockMvc.perform(get("/api/members?searchText=" + DEFAULT_FIRST_NAME))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].uscaNumber").value(hasItem(DEFAULT_USCA_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].licenceNumber").value(hasItem(DEFAULT_LICENCE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].licenceCreationDate").value(hasItem(DEFAULT_LICENCE_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].subscription").value(hasItem(DEFAULT_SUBSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].email2").value(hasItem(DEFAULT_EMAIL_2.toString())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON)));
    }

    @Test
    @Transactional
    public void shouldFindMemberThatMatchThePartialLastName() throws Exception {
        member.firstName("firstName")
            .lastName("lastName")
            .licenceNumber("PC12345");
        // Initialize the database
        memberRepository.saveAndFlush(member);

        restMemberMockMvc.perform(get("/api/members?searchText=ast"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem("firstName")))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem("lastName")))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].uscaNumber").value(hasItem(DEFAULT_USCA_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].licenceNumber").value(hasItem("PC12345")))
            .andExpect(jsonPath("$.[*].licenceCreationDate").value(hasItem(DEFAULT_LICENCE_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].subscription").value(hasItem(DEFAULT_SUBSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].email2").value(hasItem(DEFAULT_EMAIL_2.toString())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON)));
    }

    @Test
    @Transactional
    public void shouldFindMemberThatMatchThePartialFirstName() throws Exception {
        member.firstName("firstName")
            .lastName("lastName")
            .licenceNumber("PC12345");
        // Initialize the database
        memberRepository.saveAndFlush(member);

        restMemberMockMvc.perform(get("/api/members?searchText=irst"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem("firstName")))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem("lastName")))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].uscaNumber").value(hasItem(DEFAULT_USCA_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].licenceNumber").value(hasItem("PC12345")))
            .andExpect(jsonPath("$.[*].licenceCreationDate").value(hasItem(DEFAULT_LICENCE_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].subscription").value(hasItem(DEFAULT_SUBSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].email2").value(hasItem(DEFAULT_EMAIL_2.toString())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON)));
    }

    @Test
    @Transactional
    public void shouldFindMemberThatMatchThePartialLicenceNumber() throws Exception {
        member.firstName("firstName")
            .lastName("lastName")
            .licenceNumber("PC12345");
        // Initialize the database
        memberRepository.saveAndFlush(member);

        restMemberMockMvc.perform(get("/api/members?searchText=345"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem("firstName")))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem("lastName")))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].uscaNumber").value(hasItem(DEFAULT_USCA_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].licenceNumber").value(hasItem("PC12345")))
            .andExpect(jsonPath("$.[*].licenceCreationDate").value(hasItem(DEFAULT_LICENCE_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].subscription").value(hasItem(DEFAULT_SUBSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].email2").value(hasItem(DEFAULT_EMAIL_2.toString())))
            .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON)));
    }

    @Test
    @Transactional
    public void getMember() throws Exception {

        memberRepository.saveAndFlush(member);

        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(member.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.uscaNumber").value(DEFAULT_USCA_NUMBER.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.licenceNumber").value(DEFAULT_LICENCE_NUMBER.toString()))
            .andExpect(jsonPath("$.licenceCreationDate").value(DEFAULT_LICENCE_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.subscription").value(DEFAULT_SUBSCRIPTION.toString()))
            .andExpect(jsonPath("$.email2").value(DEFAULT_EMAIL_2.toString()))
            .andExpect(jsonPath("$.season").value(DEFAULT_SEASON));

    }

    @Test
    public void getMemberWithAddressesAndPhones() throws Exception {

        Member member = new MemberBuilder()
            .withAddress()
            .withPhone().build();

        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.execute((status) -> memberRepository.save(member));

        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(member.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.uscaNumber").value(DEFAULT_USCA_NUMBER))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.licenceNumber").value(DEFAULT_LICENCE_NUMBER))
            .andExpect(jsonPath("$.licenceCreationDate").value(DEFAULT_LICENCE_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.subscription").value(DEFAULT_SUBSCRIPTION))
            .andExpect(jsonPath("$.email2").value(DEFAULT_EMAIL_2))
            .andExpect(jsonPath("$.season").value(DEFAULT_SEASON))
            .andExpect(jsonPath("$.sff").value(DEFAULT_SFF))
            .andExpect(jsonPath("$.addresses.[0].address1").value("ADDRESS1"))
            .andExpect(jsonPath("$.addresses.[0].address2").value("ADDRESS2"))
            .andExpect(jsonPath("$.addresses.[0].address3").value("ADDRESS3"))
            .andExpect(jsonPath("$.addresses.[0].zipcode").value("12345"))
            .andExpect(jsonPath("$.addresses.[0].city").value("CITY"))
            .andExpect(jsonPath("$.phones.[0].phoneNumber").value("0123456789"));

//        System.out.println(restMemberMockMvc.perform(get("/api/members/{id}", member.getId())).andDo(MockMvcResultHandlers.print()));

    }

    @Test
    @Transactional
    public void getNonExistingMember() throws Exception {
        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMember() throws Exception {
        // Initialize the database
        Member member = new MemberBuilder().withAddress().withPhone().build();
        memberRepository.saveAndFlush(member);

        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Update the member
        Member updatedMember = memberRepository.findById(member.getId()).get();
        // Disconnect from session so that the updates on updatedMember are not directly saved in db
        em.detach(updatedMember);
        updatedMember
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .uscaNumber(UPDATED_USCA_NUMBER)
            .comment(UPDATED_COMMENT)
            .licenceNumber(UPDATED_LICENCE_NUMBER)
            .licenceCreationDate(UPDATED_LICENCE_CREATION_DATE)
            .subscription(UPDATED_SUBSCRIPTION)
            .email2(UPDATED_EMAIL_2)
            .season(UPDATED_SEASON);

        Address updatedAddress = updatedMember.getAddresses().get(0);
        updatedAddress
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .address3(UPDATED_ADDRESS_3)
            .zipcode(UPDATED_ZIPCODE)
            .city(UPDATED_CITY);

        Phone updatedPhone = updatedMember.getPhones().get(0);
        updatedPhone
            .phoneNumber("012345");

        restMemberMockMvc.perform(put("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMember)))
            .andExpect(status().isOk());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testMember.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testMember.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMember.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testMember.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testMember.getUscaNumber()).isEqualTo(UPDATED_USCA_NUMBER);
        assertThat(testMember.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testMember.getLicenceNumber()).isEqualTo(UPDATED_LICENCE_NUMBER);
        assertThat(testMember.getLicenceCreationDate()).isEqualTo(UPDATED_LICENCE_CREATION_DATE);
        assertThat(testMember.getSubscription()).isEqualTo(UPDATED_SUBSCRIPTION);
        assertThat(testMember.getEmail2()).isEqualTo(UPDATED_EMAIL_2);
        assertThat(testMember.getSeason()).isEqualTo(UPDATED_SEASON);

        assertThat(testMember.getAddresses().get(0).getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testMember.getAddresses().get(0).getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testMember.getAddresses().get(0).getAddress3()).isEqualTo(UPDATED_ADDRESS_3);
        assertThat(testMember.getAddresses().get(0).getZipcode()).isEqualTo(UPDATED_ZIPCODE);
        assertThat(testMember.getAddresses().get(0).getCity()).isEqualTo(UPDATED_CITY);

        assertThat(testMember.getPhones().get(0).getPhoneNumber()).isEqualTo("012345");
    }

    @Test
    @Transactional
    public void updateNonExistingMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Create the Member

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberMockMvc.perform(put("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(member)))
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        int databaseSizeBeforeDelete = memberRepository.findAll().size();

        // Get the member
        restMemberMockMvc.perform(delete("/api/members/{id}", member.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Member.class);
        Member member1 = new Member();
        member1.setId(1L);
        Member member2 = new Member();
        member2.setId(member1.getId());
        assertThat(member1).isEqualTo(member2);
        member2.setId(2L);
        assertThat(member1).isNotEqualTo(member2);
        member1.setId(null);
        assertThat(member1).isNotEqualTo(member2);
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createAddressEntity(EntityManager em) {
        Address address = new Address()
            .address1(DEFAULT_ADDRESS_1)
            .address2(DEFAULT_ADDRESS_2)
            .address3(DEFAULT_ADDRESS_3)
            .zipcode(DEFAULT_ZIPCODE)
            .city(DEFAULT_CITY);
        return address;
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phone createPhoneEntity(EntityManager em) {
        Phone phone = new Phone()
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return phone;
    }

    public static class MemberBuilder {
        private Member member;

        public MemberBuilder() {
            member = new Member()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(DEFAULT_EMAIL)
                .birthDate(DEFAULT_BIRTH_DATE)
                .gender(DEFAULT_GENDER)
                .uscaNumber(DEFAULT_USCA_NUMBER)
                .comment(DEFAULT_COMMENT)
                .licenceNumber(DEFAULT_LICENCE_NUMBER)
                .licenceCreationDate(DEFAULT_LICENCE_CREATION_DATE)
                .subscription(DEFAULT_SUBSCRIPTION)
                .email2(DEFAULT_EMAIL_2)
                .season(DEFAULT_SEASON);
        }

        public MemberBuilder withAddress() {
            Address address = new Address()
                .address1("ADDRESS1")
                .address2("ADDRESS2")
                .address3("ADDRESS3")
                .zipcode("12345")
                .city("CITY");
            member.addAddresses(address);
            return this;
        }

        public MemberBuilder withPhone() {
            Phone phone = new Phone();
            phone.setPhoneNumber("0123456789");
            member.addPhones(phone);
            return this;
        }

        public Member build() {
            return member;
        }
    }

}
