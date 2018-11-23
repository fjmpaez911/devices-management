package com.beamtrail.devicesmanagement.pojo;

import java.sql.Timestamp;
import java.util.Objects;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookHistoryEntry {

    private long id;

    @JsonProperty("booked_by")
    private String bookedBy;

    @JsonProperty("booked_timestamp")
    private long bookedTimestamp;

    @JsonProperty("returned_timestamp")
    private long returnedTimestamp;

    public BookHistoryEntry(long id, String bookedBy, Timestamp bookedTimestamp,
            Timestamp returnedTimestamp) {

        super();
        this.id = id;
        this.bookedBy = bookedBy;

        try {

            if (!Objects.isNull(bookedTimestamp)) {
                this.bookedTimestamp = bookedTimestamp.getTime();
            }

            if (!Objects.isNull(returnedTimestamp)) {
                this.returnedTimestamp = returnedTimestamp.getTime();
            }

        } catch (Exception e) {
            log.error("error parsing timestamps", e);
        }

    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
