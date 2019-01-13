package com.cloudcmr.app.member.domain;

import com.cloudcmr.app.domain.enumeration.GenderType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Member.
 */
@Entity
@Table(name = "member")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderType gender;

    @Column(name = "usca_number")
    private String uscaNumber;

    @Column(name = "jhi_comment")
    private String comment;

    @Column(name = "licence_number")
    private String licenceNumber;

    @Column(name = "licence_creation_date")
    private Instant licenceCreationDate;

    @Column(name = "subscription")
    private String subscription;

    @Column(name = "email_2")
    private String email2;

    @Column(name = "season")
    private Integer season;

    @Column(name="is_ssf", columnDefinition = "Boolean default false")
    private boolean sFF;

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "member_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "member_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Phone> phones = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Member firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Member lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public Member email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Member birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public GenderType getGender() {
        return gender;
    }

    public Member gender(GenderType gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public String getUscaNumber() {
        return uscaNumber;
    }

    public Member uscaNumber(String uscaNumber) {
        this.uscaNumber = uscaNumber;
        return this;
    }

    public void setUscaNumber(String uscaNumber) {
        this.uscaNumber = uscaNumber;
    }

    public String getComment() {
        return comment;
    }

    public Member comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public Member licenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
        return this;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public Instant getLicenceCreationDate() {
        return licenceCreationDate;
    }

    public Member licenceCreationDate(Instant licenceCreationDate) {
        this.licenceCreationDate = licenceCreationDate;
        return this;
    }

    public void setLicenceCreationDate(Instant licenceCreationDate) {
        this.licenceCreationDate = licenceCreationDate;
    }

    public String getSubscription() {
        return subscription;
    }

    public Member subscription(String subscription) {
        this.subscription = subscription;
        return this;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getEmail2() {
        return email2;
    }

    public Member email2(String email2) {
        this.email2 = email2;
        return this;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public Integer getSeason() {
        return season;
    }

    public Member season(Integer season) {
        this.season = season;
        return this;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public Member addresses(List<Address> addresses) {
        this.addresses = addresses;
        return this;
    }

    public Member addAddresses(Address address) {
        this.addresses.add(address);
        return this;
    }

    public Member removeAddresses(Address address) {
        this.addresses.remove(address);
        return this;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public Member phones(List<Phone> phones) {
        this.phones = phones;
        return this;
    }

    public Member addPhones(Phone phone) {
        this.phones.add(phone);
        return this;
    }

    public Member removePhones(Phone phone) {
        this.phones.remove(phone);
        return this;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public boolean isSFF() {
        return sFF;
    }

    public void setSFF(boolean sff) {
        this.sFF = sff;
    }

    public Member ssf(boolean sff) {
        this.sFF = sff;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        if (member.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), member.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Member{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", gender='" + getGender() + "'" +
            ", uscaNumber='" + getUscaNumber() + "'" +
            ", comment='" + getComment() + "'" +
            ", licenceNumber='" + getLicenceNumber() + "'" +
            ", licenceCreationDate='" + getLicenceCreationDate() + "'" +
            ", subscription='" + getSubscription() + "'" +
            ", email2='" + getEmail2() + "'" +
            ", season=" + getSeason() +
            ", sFF=" + isSFF() +
            "}";
    }

}
