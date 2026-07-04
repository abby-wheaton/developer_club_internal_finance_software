package com.devwmu.dc_fin_soft.entities;
import java.time.LocalDateTime;

import org.springframework.boot.micrometer.metrics.autoconfigure.export.prometheus.PrometheusProperties.Pushgateway.Scheme;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "EventFin")
public class Event {
        // initalization
    @Id
    @GeneratedValue
    private Integer id;

    @Schema(description = "The name of the event", example = "Opening night", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="name")
    private String name;

    @Schema(description = "The date that the event takes place", example = "2026-05-23T05:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="date")
    private LocalDateTime date;

    @Schema(description = "The location that the event takes place", example = "D204 Floyd Hall", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="location")
    private String location;

    @Schema(description = "The estimated number of people for the event", example = "25", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="est_attendance")
    private Integer est_attendance;

    @Schema(description = "A flag for if there is a fee for the event", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="fee_flag")
    private Integer fee_flag;

    @Schema(description = "A flag for if this is a philanthropy event", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="philanthropy_flag")
    private Integer philanthropy_flag;

    @Schema(description = "A flag for if this event is a conference", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="conference_flag")
    private Integer conference_flag;

    @Schema(description = "A flag for if this event is deleted", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="deleted")
    private Integer deleted;

    // getters

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public String getLocation() {
        return this.location;
    }

    public Integer getEstAttendance() {
        return this.est_attendance;
    }

    public Integer getFeeFlag() {
        return this.fee_flag;
    }

    public Integer getPhilanthropyFlag() {
        return this.philanthropy_flag;
    }

    public Integer getConferenceFlag() {
        return this.conference_flag;
    }

    public Integer getDeleted() {
        return this.deleted;
    }

    // setters

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEstAttendance(Integer est_attendance) {
        this.est_attendance = est_attendance;
    }

    public void setFeeFlag(Integer fee_flag) {
        this.fee_flag = fee_flag;
    }

    public void setPhilanthropyFlag(Integer philanthropy_flag) {
        this.philanthropy_flag = philanthropy_flag;
    }

    public void setConferenceFlag(Integer conference_flag) {
        this.conference_flag = conference_flag;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

}
