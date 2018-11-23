package com.beamtrail.devicesmanagement.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "devices")
public class Device implements Serializable {

    private static final long serialVersionUID = -7971135222615273171L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @EqualsAndHashCode.Exclude
    private String name;

    @EqualsAndHashCode.Exclude
    private String brand;

    @EqualsAndHashCode.Exclude
    private String model;

    @EqualsAndHashCode.Exclude
    @Column(name = "is_booked")
    private boolean booked;

    @EqualsAndHashCode.Exclude
    @Column(name = "created_on")
    private Timestamp createdOn;

    public Device(String name, String brand, String model, boolean booked) {
        super();
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.booked = booked;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
