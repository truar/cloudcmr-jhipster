package com.cloudcmr.app.member.web.rest;

import com.cloudcmr.app.CloudcmrApp;
import com.cloudcmr.app.member.domain.Address;
import com.cloudcmr.app.member.domain.Member;
import com.cloudcmr.app.member.domain.Phone;
import com.cloudcmr.app.domain.enumeration.GenderType;
import com.cloudcmr.app.member.repository.MemberRepository;
import com.cloudcmr.app.member.service.MemberServiceCmr;
import com.cloudcmr.app.web.rest.*;
import com.cloudcmr.app.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cloudcmr.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the MemberResourceCmr REST controller.
 *
 * @see MemberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberResourceImportFFSIntTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberServiceCmr memberServiceCmr;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private MockMvc restMemberMockMvc;

    private String csvFileContent =
        "PC1234567;;LASTNAME;FIRSTNAME;M;1980;01/01/1980;123 RUE FRANCE;;RUE 3;12345;CITY1;FRANCE;04.01.02.03.04;06.01.02.03.04;first.last@mail.fr;NON;RCA;2019;11/01/2019;20:01:47;11/01/2019;14/10/2019;1819-19257;PC\n" +
        "PC98765432;;LASTNAME3;FIRSTNAME3;M;1973;01/06/1973;456 ROUTE FR;;;23456;CITY2;FRANCE;04.01.02.03.04;06.01.02.03.04;aloa@mail.com;OUI;LA;2019;21/12/2018;20:19:07;21/12/2018;14/10/2019;;PC\n";

    private MockMultipartFile csvFile;

    @Before
    public void setup() {
        final MemberResource memberResource = new MemberResource(null, memberServiceCmr);
        this.restMemberMockMvc = MockMvcBuilders.standaloneSetup(memberResource).build();

        csvFile = new MockMultipartFile("exportFFS", "members.csv", "text/plain", csvFileContent.getBytes());

        // Clean the db
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.execute((status) -> {
            memberRepository.deleteAll();
            return null;
        });
    }

    @Test
    public void shouldInsertAllTheNonExistingMembersFromACsvFileManyTimesWithoutChangingTheResult() throws Exception {
        for(int i = 0; i < 3; i++) {
            importFFSFile();
            assertMembers();
        }
    }

    @Test
    public void shouldOverrideTheExistingMembersWithTheOnesFromTheCsv() throws Exception {
        initDBForUpdateTest();
        importFFSFile();
        assertMembers();
    }

    @Test
    public void shouldAddPhonesAndAdressesToExistingMembersFromTheCsv() throws Exception {
        final Member member = MemberResourceIntTest.createEntity(null);
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.execute((status) -> memberRepository.save(member.lastName("LASTNAME").firstName("FIRSTNAME").birthDate(LocalDate.of(1980, 01, 01))));

        importFFSFile();

        Member memberWithRelations = memberRepository.findById(member.getId()).get();
        assertThat(memberWithRelations.getAddresses()).hasSize(1);
        assertThat(memberWithRelations.getPhones()).hasSize(2);
        assertThat(memberWithRelations.getPhones().get(0).getPhoneNumber()).isEqualTo("04.01.02.03.04");
        assertThat(memberWithRelations.getPhones().get(1).getPhoneNumber()).isEqualTo("06.01.02.03.04");

    }

    private void importFFSFile() throws Exception {
        restMemberMockMvc.perform(multipart("/api/members/file")
            .file(csvFile)
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }

    private void initDBForUpdateTest() {
        Member member = MemberResourceIntTest.createEntity(null);
        Member member2 = MemberResourceIntTest.createEntity(null);
        Address address = MemberResourceIntTest.createAddressEntity(null);
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        member.setAddresses(addresses);

        Phone phone = MemberResourceIntTest.createPhoneEntity(null);
        Phone phone2 = MemberResourceIntTest.createPhoneEntity(null);
        member.setPhones(Arrays.asList(phone, phone2));
        List<Phone> phones = new ArrayList<>();
        phones.add(phone);
        phones.add(phone2);
        member.setPhones(phones);

        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.execute((status) ->
            memberRepository.save(member.lastName("LASTNAME").firstName("FIRSTNAME").birthDate(LocalDate.of(1980, 01, 01)))
        );

        assertThat(memberRepository.findAll()).hasSize(1);
    }

    private void assertMembers() {
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(2);
        Member firstMember = members.get(0);
        assertThat(firstMember.getFirstName()).isEqualTo("FIRSTNAME");
        assertThat(firstMember.getLastName()).isEqualTo("LASTNAME");
        assertThat(firstMember.getGender()).isEqualTo(GenderType.MALE);
        assertThat(firstMember.getBirthDate()).isEqualTo(LocalDate.of(1980, 01, 01));
        assertThat(firstMember.getEmail()).isEqualTo("first.last@mail.fr");
        assertThat(firstMember.getUscaNumber()).isEqualTo("1819-19257");
        assertThat(firstMember.getLicenceNumber()).isEqualTo("PC1234567");
        DateTimeFormatter licenceDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        TemporalAccessor temporalAccessor = licenceDateFormatter.parse("11/01/2019 20:01:47");
        LocalDateTime localDateTime = LocalDateTime.from(temporalAccessor);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        Instant result = Instant.from(zonedDateTime);
        assertThat(firstMember.getLicenceCreationDate()).isEqualTo(result);
        assertThat(firstMember.getSubscription()).isEqualTo("RCA");
        assertThat(firstMember.getSeason()).isEqualTo(2019);
        assertThat(firstMember.isSFF()).isEqualTo(true);

        assertAddresses(firstMember.getAddresses());
        assertPhones(firstMember.getPhones());
    }

    private void assertAddresses(List<Address> addresses) {
        assertThat(addresses).hasSize(1);
        Address address = addresses.get(0);
        assertThat(address.getAddress1()).isEqualTo("123 RUE FRANCE");
        assertThat(address.getAddress2()).isEqualTo("");
        assertThat(address.getAddress3()).isEqualTo("RUE 3");
        assertThat(address.getZipcode()).isEqualTo("12345");
        assertThat(address.getCity()).isEqualTo("CITY1");
    }

    private void assertPhones(List<Phone> phones) {
        assertThat(phones).hasSize(2);
        Phone updatedPhone1 = phones.get(0);
        Phone updatedPhone2 = phones.get(1);
        assertThat(updatedPhone1.getPhoneNumber()).isEqualTo("04.01.02.03.04");
        assertThat(updatedPhone2.getPhoneNumber()).isEqualTo("06.01.02.03.04");
    }
}
