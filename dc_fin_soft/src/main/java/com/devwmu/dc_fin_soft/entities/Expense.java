package com.devwmu.dc_fin_soft.entities;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "Expenses")
public class Expense {
    
    // initalization
    @Id
    @GeneratedValue
    private Integer id;

    @Schema(description = "The name of the expense", example = "Pencils and Pens", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="name")
    private String name;

    @Schema(description = "The quantity requested for the expense", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="quantity")
    private Integer quantity;

    @Schema(description = "The price of the individual unit of the expense", example = "4.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="price_per_unit")
    private BigDecimal price_per_unit;

    @Schema(description = "The total price of the expense", example = "12.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="total_price")
    private BigDecimal total_price;

    @Schema(description = "Why the expense is needed", example = "Office supplies running low", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="purpose")
    private String purpose;

    @Schema(description = "Who sells the expense", example = "Amazon", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="vendor")
    private String vendor;

    @Schema(description = "The id of the event associated if applicable", example = "12", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name ="event_id")
    private Integer eventId;

    @Schema(description = "The source used to pay for the expense", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="source_id")
    private Integer source_id;

    @Schema(description = "The link to the expense on a website", example = "www.amazon.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name ="link")
    private String link;

    @Schema(description = "When the item is needed by", example = "2026-05-23T05:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="item_deadline")
    private LocalDateTime item_deadline;

    @Schema(description = "What community will use this expense", example = "Web Team", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="community")
    private Integer community;

    @Schema(description = "If food is included in this expense", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="food_flag")
    private Integer foodFlag;

    @Schema(description = "If the expense has been requested", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="requested_flag")
    private Integer requested_flag;

    @Schema(description = "If the expense has been approved", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="approved_flag")
    private Integer approved_flag;

    @Schema(description = "If the expense has begun being bought", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="started_buying_flag")
    private Integer started_buying_flag;

    @Schema(description = "If the expense has finished being bought", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="finished_buying_flag")
    private Integer finished_buying_flag;

    @Schema(description = "If the expense has been picked up", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="picked_up_flag")
    private Integer picked_up_flag;

    @Schema(description = "If the expense has been reimbursed", example = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name ="reimbursed_flag")
    private Integer reimbursed_flag;

    @Schema(description = "How much money is remaining to be spent on this expense", example = "20.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="money_remaining")
    private BigDecimal money_remaining;

    @Schema(description = "How much money is spent on this expense", example = "40.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="total_spent")
    private BigDecimal total_spent;

    @Schema(description = "Where to pick up this expense if applicable", example = "Student Center Admin Offices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name ="pickup_location")
    private String pickup_location;

    @Schema(description = "When the expense's allocation is due", example = "2026-05-23T05:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="allocation_deadline")
    private LocalDateTime allocation_deadline;

    @Schema(description = "When the expense's deliberation is due", example = "2026-05-23T05:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name ="deliberation_deadline")
    private LocalDateTime deliberation_deadline;

    @Schema(description = "When the expense must be reimbursed by", example = "2026-05-23T05:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name ="reimbursement_deadline")
    private LocalDateTime reimbursement_deadline;

    @Schema(description = "How the expense is paid for", example = "Club Credit Card", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="payment_type")
    private String payment_type;

    @Schema(description = "Will be deleted soon", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="deleted")
    private Integer deleted;

    // getters
    public Integer getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public Integer getQuantity(){
        return this.quantity;
    }

    public BigDecimal getPricePerUnit(){
        return this.price_per_unit;
    }

    public BigDecimal getTotalPrice(){
        return this.total_price;
    }

    public String getPurpose(){
        return this.purpose;
    }

    public String getVendor(){
        return this.vendor;
    }

    public Integer getSourceId(){
        return this.source_id;
    }

    public Integer getEventId(){
        return this.eventId;
    }
    public String getLink() {
        return this.link;
    }

    public LocalDateTime getItemDeadline() {
        return this.item_deadline;
    }

    public Integer getCommunity() {
        return this.community;
    }

    public Integer getFoodFlag() {
        return this.foodFlag;
    }

    public Integer getRequestedFlag() {
        return this.requested_flag;
    }

    public Integer getApprovedFlag() {
        return this.approved_flag;
    }

    public Integer getStartedBuyingFlag() {
        return this.started_buying_flag;
    }

    public Integer getFinishedBuyingFlag() {
        return this.finished_buying_flag;
    }

    public Integer getPickedUpFlag() {
        return this.picked_up_flag;
    }

    public Integer getReimbursedFlag() {
        return this.reimbursed_flag;
    }

    public BigDecimal getMoneyRemaining() {
        return this.money_remaining;
    }

    public BigDecimal getTotalSpent() {
        return this.total_spent;
    }

    public String getPickupLocation() {
        return this.pickup_location;
    }

    public LocalDateTime getAllocationDeadline() {
        return this.allocation_deadline;
    }

    public LocalDateTime getDeliberationDeadline() {
        return this.deliberation_deadline;
    }

    public LocalDateTime getReimbursementDeadline() {
        return this.reimbursement_deadline;
    }

    public String getPaymentType() {
        return this.payment_type;
    }

    public Integer getDeleted() {
        return this.deleted;
    }

    // setters
    public void setName(String name){
        this.name = name;
    }

    public void setQuantity(Integer quantity){
        this.quantity = quantity;
    }

    public void setPricePerUnit(BigDecimal price_per_unit){
        this.price_per_unit = price_per_unit;
    }

    public void setTotalPrice(BigDecimal total_price) {
        this.total_price = total_price;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setEventId(Integer event_id) {
        this.eventId = event_id;
    }

    public void setSourceId(Integer source_id) {
        this.source_id = source_id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setItemDeadline(LocalDateTime item_deadline) {
        this.item_deadline = item_deadline;
    }

    public void setCommunity(Integer community) {
        this.community = community;
    }

    public void setFoodFlag(Integer food_flag) {
        this.foodFlag = food_flag;
    }

    public void setRequestedFlag(Integer requested_flag) {
        this.requested_flag = requested_flag;
    }

    public void setApprovedFlag(Integer approved_flag) {
        this.approved_flag = approved_flag;
    }

    public void setStartedBuyingFlag(Integer started_buying_flag) {
        this.started_buying_flag = started_buying_flag;
    }

    public void setFinishedBuyingFlag(Integer finished_buying_flag) {
        this.finished_buying_flag = finished_buying_flag;
    }

    public void setPickedUpFlag(Integer picked_up_flag) {
        this.picked_up_flag = picked_up_flag;
    }

    public void setReimbursedFlag(Integer reimbursed_flag) {
        this.reimbursed_flag = reimbursed_flag;
    }

    public void setMoneyRemaining(BigDecimal money_remaining) {
        this.money_remaining = money_remaining;
    }

    public void setTotalSpent(BigDecimal total_spent) {
        this.total_spent = total_spent;
    }

    public void setPickupLocation(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    public void setAllocationDeadline(LocalDateTime allocation_deadline) {
        this.allocation_deadline = allocation_deadline;
    }

    public void setDeliberationDeadline(LocalDateTime deliberation_deadline) {
        this.deliberation_deadline = deliberation_deadline;
    }

    public void setReimbursementDeadline(LocalDateTime reimbursement_deadline) {
        this.reimbursement_deadline = reimbursement_deadline;
    }

    public void setPaymentType(String payment_type) {
        this.payment_type = payment_type;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
    
}
