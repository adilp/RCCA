package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Nurse.
 */
@Entity
@Table(name = "NURSE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Nurse implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_active")
    private Boolean isActive;

    @NotNull
    @Column(name = "nurse_id", nullable = false)
    private Integer nurseId;

    @OneToMany(mappedBy = "nurse")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<NurseSchedule> nurseScheduleDates = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getNurseId() {
        return nurseId;
    }

    public void setNurseId(Integer nurseId) {
        this.nurseId = nurseId;
    }

    public Set<NurseSchedule> getNurseScheduleDates() {
        return nurseScheduleDates;
    }

    public void setNurseScheduleDates(Set<NurseSchedule> nurseSchedules) {
        this.nurseScheduleDates = nurseSchedules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Nurse nurse = (Nurse) o;

        if ( ! Objects.equals(id, nurse.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Nurse{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", isActive='" + isActive + "'" +
                ", nurseId='" + nurseId + "'" +
                '}';
    }
}
