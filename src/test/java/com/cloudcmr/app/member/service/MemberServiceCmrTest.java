package com.cloudcmr.app.member.service;

import com.cloudcmr.app.member.domain.Address;
import com.cloudcmr.app.member.domain.Member;
import com.cloudcmr.app.member.domain.Phone;
import com.cloudcmr.app.member.repository.MemberRepository;
import com.cloudcmr.app.member.service.MemberServiceCmr;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MemberServiceCmrTest {

    @Mock
    private MemberRepository memberRepository;

    private MemberServiceCmr memberService;

    @Before
    public void setUp() {
        this.memberService = new MemberServiceCmr(memberRepository);
    }

    @Test
    public void shouldImportTheInexistingMemberIntoTheDatabase() {
        List<Member> members = initMembers();

        when(memberRepository.findByLastNameAndFirstNameAndBirthDate(any(), any(), any())).thenReturn(null);
        memberService.importMembersFromSFF(members);

        verify(memberRepository, times(1)).save(any());
    }

    @Test
    public void shouldUpdateTheExistingMemberIntoTheDatabase() {
        List<Member> members = initMembers();

        when(memberRepository.findByLastNameAndFirstNameAndBirthDate(any(), any(), any())).thenReturn(members.get(0));
        memberService.importMembersFromSFF(members);

        verify(memberRepository, times(1)).save(any());
    }

    private List<Member> initMembers() {
        LocalDate birthDate = LocalDate.of(1970, 11, 10);

        List<Member> members = new ArrayList<>();
        Member member = new Member();
        member.setLastName("last name");
        member.setFirstName("first name");
        member.setBirthDate(birthDate);
        member.addAddresses(new Address());
        member.addPhones(new Phone());
        member.addPhones(new Phone());
        members.add(member);
        return members;
    }

}
