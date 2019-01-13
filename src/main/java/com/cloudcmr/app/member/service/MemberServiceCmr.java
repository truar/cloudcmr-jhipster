package com.cloudcmr.app.member.service;

import com.cloudcmr.app.member.domain.Address;
import com.cloudcmr.app.member.domain.Member;
import com.cloudcmr.app.member.domain.Phone;
import com.cloudcmr.app.member.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberServiceCmr {

    private final Logger log = LoggerFactory.getLogger(MemberServiceCmr.class);

    private MemberRepository memberRepository;

    public MemberServiceCmr(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void importMembersFromSFF(List<Member> members) {
        for(Member member : members) {
            log.debug("Processing : " + member);
            Member existingMember = memberRepository.findByLastNameAndFirstNameAndBirthDate(member.getLastName(), member.getFirstName(), member.getBirthDate());

            if (existingMember != null) {
                log.debug("Member already in DB. Preparing for update");
                updateMember(member, existingMember);
            } else {
                log.debug("New Member detected. Inserting...");
                member.setSFF(true);
                insertMember(member);
            }
        }
    }

    private void updateMember(Member member, Member existingMember) {
        existingMember.setLicenceNumber(member.getLicenceNumber());
        existingMember.setLicenceCreationDate(member.getLicenceCreationDate());
        existingMember.setUscaNumber(member.getUscaNumber());
        existingMember.setComment(member.getComment());
        existingMember.setSubscription(member.getSubscription());
        existingMember.setSeason(member.getSeason());
        existingMember.setGender(member.getGender());
        existingMember.setEmail(member.getEmail());
        existingMember.setSFF(true);

        List<Address> existingAddresses = existingMember.getAddresses();
        if(!existingAddresses.isEmpty()) {
            Address addressToUpdate = existingAddresses.get(0);
            Address newAddress = member.getAddresses().get(0);
            addressToUpdate.setAddress1(newAddress.getAddress1());
            addressToUpdate.setAddress2(newAddress.getAddress2());
            addressToUpdate.setAddress3(newAddress.getAddress3());
            addressToUpdate.setZipcode(newAddress.getZipcode());
            addressToUpdate.setCity(newAddress.getCity());
        } else {
            Address newAddress = member.getAddresses().get(0);
            existingMember.addAddresses(newAddress);
        }
        List<Phone> existingPhones = existingMember.getPhones();
        if(existingPhones.size() > 1) {
            Phone phoneToUpdate1 = existingPhones.get(0);
            Phone newPhone1 = member.getPhones().get(0);
            phoneToUpdate1.setPhoneNumber(newPhone1.getPhoneNumber());
            Phone phoneToUpdate2 = existingPhones.get(1);
            Phone newPhone2 = member.getPhones().get(1);
            phoneToUpdate2.setPhoneNumber(newPhone2.getPhoneNumber());
        } else if(existingPhones.size() == 1) {
            Phone phoneToUpdate1 = existingPhones.get(0);
            Phone newPhone1 = member.getPhones().get(0);
            phoneToUpdate1.setPhoneNumber(newPhone1.getPhoneNumber());
            Phone newPhone2 = member.getPhones().get(1);
            existingMember.addPhones(newPhone2);
        }
        else {
            Phone newPhone = member.getPhones().get(0);
            existingMember.addPhones(newPhone);
            Phone newPhone2 = member.getPhones().get(1);
            existingMember.addPhones(newPhone2);
        }

        memberRepository.save(existingMember);
    }

    private Member insertMember(Member member) {
        return memberRepository.save(member);
    }

}
