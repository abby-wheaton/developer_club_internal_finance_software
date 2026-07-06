package com.devwmu.dc_fin_soft.entities;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "Calendar")
public class CalendarEvent {

    // initalization
    @Id
    @GeneratedValue
    private Integer id;

    @Schema(description = "The name of the event", example = "Weekly Meeting", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="event_name")
    private String event_name;

    @Schema(description = "The location", example = "Floyd Hall", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name ="location")
    private String location;

    @Schema(description = "The start day and time of the event", example = "2026-12-03T10:15:30", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="start_date_time")
    private LocalDateTime start_date_time;

    @Schema(description = "The end day and time of the event", example = "2026-12-03T10:15:40", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="end_date_time")
    private LocalDateTime end_date_time;

    @Schema(description = "The creator of the event", example = "user_id", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="creator")
    private String creator;

    @Schema(description = "The id of the group associated with the event", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="group_id")
    private Integer group_id;

    @Schema(description = "The category of the event", example = "Web Team", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="category")
    private Integer category;

    @Schema(description = "Will be deleted eventually", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="deleted")
    private Integer deleted;


    // getters
    public Integer getId(){
        return this.id;
    }

    public String getEventName(){
        return this.event_name;
    }

    public String getLocation(){
        return this.location;
    }

    public LocalDateTime getStartDateTime(){
        return this.start_date_time;
    }

    public LocalDateTime getEndDateTime(){
        return this.end_date_time;
    }

    public String getCreator(){
        return this.creator;
    }

    public Integer getGroupId(){
        return this.group_id;
    }

    public Integer getCategory(){
        return this.category;
    }

    public Integer getDeleted(){
        return this.deleted;
    }

    // setters

    public void setEventName(String event_name){
        this.event_name = event_name;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public void setStartDateTime(LocalDateTime start_date_time){
        this.start_date_time = start_date_time;
    }

    public void setEndDateTime(LocalDateTime end_date_time){
        this.end_date_time = end_date_time;
    }

    public void setCreator(String creator){
        this.creator = creator;
    }

    public void setGroupId(Integer group_id){
        this.group_id = group_id;
    }

    public void setCategory(Integer category){
        this.category = category;
    }

    public void setDeleted(Integer deleted){
        this.deleted = deleted;
    }
}
