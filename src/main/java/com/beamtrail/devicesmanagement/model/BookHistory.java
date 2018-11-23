package com.beamtrail.devicesmanagement.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_history")
public class BookHistory implements Serializable {

    private static final long serialVersionUID = 4581637682519555860L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "device_id")
    private long deviceId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "booked_timestamp", insertable = true, updatable = false)
    private Timestamp bookedTimestamp = new Timestamp(new Date().getTime());

    @Column(name = "returned_timestamp")
    private Timestamp returnedTimestamp;

    public BookHistory(long deviceId, String userName) {
        super();
        this.deviceId = deviceId;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
