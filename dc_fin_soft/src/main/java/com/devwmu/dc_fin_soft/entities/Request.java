package com.devwmu.dc_fin_soft.entities;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "ClubRequests")
public class Request {
    
    // initalization
    @Id
    @GeneratedValue
    private Integer id;

    @Schema(description = "The community name for the requestee", example = "Web team", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="community_name")
    private Integer community_name;

    @Schema(description = "The person who is submitting the request", example = "Khang", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="requestee_user")
    private String requestee_user;

    @Schema(description = "The name of the item being requested", example = "Napkins", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="item_name")
    private String item_name;

    @Schema(description = "Whether or not the request has been approved, denied, or neither", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name ="approval")
    private Integer approval;

    @Schema(description = "The number of items that is being requested", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="quantity")
    private Integer quantity;

    @Schema(description = "The price for one unit of the item being requested", example = "14.99", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="price_per_unit")
    private BigDecimal price_per_unit;

    @Schema(description = "When the item is needed by", example = "2026-05-23T05:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="deadline")
    private LocalDateTime deadline;

    @Schema(description = "Why the requestee is asking for the item", example = "the attendees of event x need food", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="purpose")
    private String purpose;

    @Schema(description = "A flag for if the request has been deleted or not", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="deleted")
    private Integer deleted;

    // getters

    public Integer getId() {
        return this.id;
    }

    public Integer getCommunityName() {
        return this.community_name;
    }

    public String getRequesteeUser() {
        return this.requestee_user;
    }

    public String getItemName() {
        return this.item_name;
    }

    public Integer getApproval() {
        return this.approval;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public BigDecimal getPricePerUnit() {
        return this.price_per_unit;
    }

    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    public String getPurpose() {
        return this.purpose;
    }

    public Integer getDeleted() {
        return this.deleted;
    }

    // setters

    public void setCommunityName(Integer community_name) {
        this.community_name = community_name;
    }

    public void setRequesteeUser(String requestee_user) {
        this.requestee_user = requestee_user;
    }

    public void setItemName(String item_name) {
        this.item_name = item_name;
    }

    public void setApproval(Integer approval) {
        this.approval = approval;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPricePerUnit(BigDecimal price_per_unit) {
        this.price_per_unit = price_per_unit;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }


    

}
