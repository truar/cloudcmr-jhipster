package com.cloudcmr.app.member.web.rest.fileParser;

import com.cloudcmr.app.member.domain.Address;
import com.cloudcmr.app.member.domain.Member;
import com.cloudcmr.app.member.domain.Phone;
import com.cloudcmr.app.domain.enumeration.GenderType;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CSVParserTest {

    private static final DateTimeFormatter licenceDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Test
    public void shouldReturnAnEmptyListWhenNoInput() throws IOException {
        Parser parser = new CSVParser(new ByteArrayInputStream("".getBytes()));
        assertThat(parser.parse()).isEqualTo(new ArrayList<Member>());
    }

    @Test
    public void shouldReturnAListOf1MemberGiven1Line() throws IOException {
        String lines = "PC1234567;;LASTNAME;FIRSTNAME;M;1980;01/01/1980;123 RUE FRANCE;;RUE 3;12345;CITY1;FRANCE;04.01.02.03.04;06.01.02.03.04;first.last@mail.fr;NON;RCA;2019;11/01/2019;20:01:47;11/01/2019;14/10/2019;1819-19257;PC\n";
        Parser parser = new CSVParser(new ByteArrayInputStream(lines.getBytes()));

        Member member = assertMember(parser);

        List<Address> addresses = member.getAddresses();
        assertThat(addresses.size()).isEqualTo(1);
        Address address = addresses.get(0);
        assertThat(address.getAddress1()).isEqualTo("123 RUE FRANCE");
        assertThat(address.getAddress2()).isEqualTo("");
        assertThat(address.getAddress3()).isEqualTo("RUE 3");
        assertThat(address.getZipcode()).isEqualTo("12345");
        assertThat(address.getCity()).isEqualTo("CITY1");

        List<Phone> phones = member.getPhones();
        assertThat(phones.size()).isEqualTo(2);
        assertThat(phones.get(0).getPhoneNumber()).isEqualTo("04.01.02.03.04");
        assertThat(phones.get(1).getPhoneNumber()).isEqualTo("06.01.02.03.04");

    }

    @Test
    public void shouldReturnOneMemberWith2PhonesAnd1AddressEvenIfEmpty() throws IOException {
        String lines = "PC1234567;;LASTNAME;FIRSTNAME;M;1980;01/01/1980;;;;;;FRANCE;;;first.last@mail.fr;NON;RCA;2019;11/01/2019;20:01:47;11/01/2019;14/10/2019;1819-19257;PC\n";
        Parser parser = new CSVParser(new ByteArrayInputStream(lines.getBytes()));

        Member member = assertMember(parser);

        List<Address> addresses = member.getAddresses();
        assertThat(addresses.size()).isEqualTo(1);
        Address address = addresses.get(0);
        assertThat(address.getAddress1()).isEmpty();
        assertThat(address.getAddress2()).isEmpty();
        assertThat(address.getAddress3()).isEmpty();
        assertThat(address.getZipcode()).isEmpty();
        assertThat(address.getCity()).isEmpty();

        List<Phone> phones = member.getPhones();
        assertThat(phones.size()).isEqualTo(2);
        assertThat(phones.get(0).getPhoneNumber()).isEmpty();
        assertThat(phones.get(1).getPhoneNumber()).isEmpty();

    }

    private Member assertMember(Parser parser) throws IOException {
        List<Member> membersParsed = parser.parse();
        assertThat(membersParsed.size()).isEqualTo(1);
        Member member = membersParsed.get(0);
        assertThat(member.getLicenceNumber()).isEqualTo("PC1234567");
        assertThat(member.getFirstName()).isEqualTo("FIRSTNAME");
        assertThat(member.getLastName()).isEqualTo("LASTNAME");
        assertThat(member.getGender()).isEqualTo(GenderType.MALE);
        assertThat(member.getBirthDate()).isEqualTo(LocalDate.of(1980, 01, 01));
        assertThat(member.getEmail()).isEqualTo("first.last@mail.fr");
        assertThat(member.getUscaNumber()).isEqualTo("1819-19257");
        TemporalAccessor temporalAccessor = licenceDateFormatter.parse("11/01/2019 20:01:47");
        LocalDateTime localDateTime = LocalDateTime.from(temporalAccessor);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        Instant result = Instant.from(zonedDateTime);
        assertThat(member.getLicenceCreationDate()).isEqualTo(result);
        assertThat(member.getSubscription()).isEqualTo("RCA");
        assertThat(member.getSeason()).isEqualTo(2019);
        return member;
    }

    @Test
    public void shouldReturnAListOf2MembersGiven2Lines() throws IOException {
        String lines = "PC1234567;;LASTNAME;FIRSTNAME;M;1980;01/01/1980;123 RUE FRANCE;;RUE 3;12345;CITY1;FRANCE;;;first.last@mail.fr;NON;RCA;2019;11/01/2019;20:01:47;11/01/2019;14/10/2019;1819-19257;PC\n" +
            "PC98765432;;LASTNAME3;FIRSTNAME3;M;1973;13/06/1973;13 RUE DU BELVEDERE;;;23456;CITY2;FRANCE;04.01.02.03.04;06.01.02.03.04;aloa@mail.com;OUI;LA;2019;21/12/2018;20:19:07;21/12/2018;14/10/2019;;PC\n";

        Parser parser = new CSVParser(new ByteArrayInputStream(lines.getBytes()));
        List<Member> membersParsed = parser.parse();
        assertThat(membersParsed).hasSize(2);
    }

    @Test
    public void shouldSkipTheHeaderLine() throws IOException {
        String lines = "licence;bordereau;nom;prenom;sexe;annee_naissance;date_naissance;adresse1;adresse2;adresse3;code_postal;ville;pays;telephone;portable;email;exploitation_mail;cotisation;saison;date_prise_licence;heure_prise_licence;deb_validite;fin_validite;info_club;origine\n" +
            "PC1234567;;LASTNAME;FIRSTNAME;M;1980;01/01/1980;123 RUE FRANCE;;RUE 3;12345;CITY1;FRANCE;;;first.last@mail.fr;NON;RCA;2019;11/01/2019;20:01:47;11/01/2019;14/10/2019;1819-19257;PC\n" +
            "PC98765432;;LASTNAME3;FIRSTNAME3;M;1973;13/06/1973;13 RUE DU BELVEDERE;;;23456;CITY2;FRANCE;04.01.02.03.04;06.01.02.03.04;aloa@mail.com;OUI;LA;2019;21/12/2018;20:19:07;21/12/2018;14/10/2019;;PC\n";

        Parser parser = new CSVParser(new ByteArrayInputStream(lines.getBytes()));
        List<Member> membersParsed = parser.parse();
        assertThat(membersParsed).hasSize(2);
    }

    @Test
    public void shouldFlagTheMemberAsComingFromSFF() throws IOException {
        String lines = "PC1234567;;LASTNAME;FIRSTNAME;M;1980;01/01/1980;123 RUE FRANCE;;RUE 3;12345;CITY1;FRANCE;;;first.last@mail.fr;NON;RCA;2019;11/01/2019;20:01:47;11/01/2019;14/10/2019;1819-19257;PC";
        Parser parser = new CSVParser(new ByteArrayInputStream(lines.getBytes()));
        List<Member> membersParsed = parser.parse();
        assertThat(membersParsed).hasSize(1);
        assertThat(membersParsed.get(0).isSFF()).isTrue();
    }
}
