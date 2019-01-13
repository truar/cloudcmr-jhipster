package com.cloudcmr.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.cloudcmr.app.domain.enumeration.PhoneType;

/**
 * A Phone.
 */
@Entity
@Table(name = "phone")
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private PhoneType type;

    @NotNull
    @Pattern(regexp = "^0[0-9]*$")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PhoneType getType() {
        return type;
    }

    public Phone type(PhoneType type) {
        this.type = type;
        return this;
    }

    public void setType(PhoneType type) {
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Phone phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Member getMember() {
        return member;
    }

    public Phone member(Member member) {
        this.member = member;
        return this;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Phone phone = (Phone) o;
        if (phone.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), phone.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Phone{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
