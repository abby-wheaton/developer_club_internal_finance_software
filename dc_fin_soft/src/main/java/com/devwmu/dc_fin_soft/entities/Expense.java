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
    private BigDecimal pricePerUnit;

    @Schema(description = "The total price of the expense", example = "12.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="total_price")
    private BigDecimal totalPrice;

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
    private Integer sourceId;

    @Schema(description = "The link to the expense on a website", example = "www.amazon.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name ="link")
    private String link;

    @Schema(description = "When the item is needed by", example = "2026-05-23T05:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="item_deadline")
    private LocalDateTime itemDeadline;

    @Schema(description = "What community will use this expense", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="community")
    private Integer community;

    @Schema(description = "If food is included in this expense", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="food_flag")
    private Integer foodFlag;

    @Schema(description = "If the expense has been requested", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="requested_flag")
    private Integer requestedFlag;

    @Schema(description = "If the expense has been approved", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="approved_flag")
    private Integer approvedFlag;

    @Schema(description = "If the expense has begun being bought", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="started_buying_flag")
    private Integer startedBuyingFlag;

    @Schema(description = "If the expense has finished being bought", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="finished_buying_flag")
    private Integer finishedBuyingFlag;

    @Schema(description = "If the expense has been picked up", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="picked_up_flag")
    private Integer pickedUpFlag;

    @Schema(description = "If the expense has been reimbursed", example = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name ="reimbursed_flag")
    private Integer reimbursedFlag;

    @Schema(description = "How much money is remaining to be spent on this expense", example = "20.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="money_remaining")
    private BigDecimal moneyRemaining;

    @Schema(description = "How much money is spent on this expense", example = "40.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="total_spent")
    private BigDecimal totalSpent;

    @Schema(description = "Where to pick up this expense if applicable", example = "Student Center Admin Offices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name ="pickup_location")
    private String pickupLocation;

    @Schema(description = "When the expense's allocation is due", example = "2026-05-23T05:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="allocation_deadline")
    private LocalDateTime allocationDeadline;

    @Schema(description = "When the expense's deliberation is due", example = "2026-05-23T05:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name ="deliberation_deadline")
    private LocalDateTime deliberationDeadline;

    @Schema(description = "When the expense must be reimbursed by", example = "2026-05-23T05:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name ="reimbursement_deadline")
    private LocalDateTime reimbursementDeadline;

    @Schema(description = "How the expense is paid for", example = "Club Credit Card", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="payment_type")
    private String paymentType;

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
        return this.pricePerUnit;
    }

    public BigDecimal getTotalPrice(){
        return this.totalPrice;
    }

    public String getPurpose(){
        return this.purpose;
    }

    public String getVendor(){
        return this.vendor;
    }

    public Integer getSourceId(){
        return this.sourceId;
    }

    public Integer getEventId(){
        return this.eventId;
    }
    public String getLink() {
        return this.link;
    }

    public LocalDateTime getItemDeadline() {
        return this.itemDeadline;
    }

    public Integer getCommunity() {
        return this.community;
    }

    public Integer getFoodFlag() {
        return this.foodFlag;
    }

    public Integer getRequestedFlag() {
        return this.requestedFlag;
    }

    public Integer getApprovedFlag() {
        return this.approvedFlag;
    }

    public Integer getStartedBuyingFlag() {
        return this.startedBuyingFlag;
    }

    public Integer getFinishedBuyingFlag() {
        return this.finishedBuyingFlag;
    }

    public Integer getPickedUpFlag() {
        return this.pickedUpFlag;
    }

    public Integer getReimbursedFlag() {
        return this.reimbursedFlag;
    }

    public BigDecimal getMoneyRemaining() {
        return this.moneyRemaining;
    }

    public BigDecimal getTotalSpent() {
        return this.totalSpent;
    }

    public String getPickupLocation() {
        return this.pickupLocation;
    }

    public LocalDateTime getAllocationDeadline() {
        return this.allocationDeadline;
    }

    public LocalDateTime getDeliberationDeadline() {
        return this.deliberationDeadline;
    }

    public LocalDateTime getReimbursementDeadline() {
        return this.reimbursementDeadline;
    }

    public String getPaymentType() {
        return this.paymentType;
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

    public void setPricePerUnit(BigDecimal pricePerUnit){
        this.pricePerUnit = pricePerUnit;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setItemDeadline(LocalDateTime itemDeadline) {
        this.itemDeadline = itemDeadline;
    }

    public void setCommunity(Integer community) {
        this.community = community;
    }

    public void setFoodFlag(Integer foodFlag) {
        this.foodFlag = foodFlag;
    }

    public void setRequestedFlag(Integer requestedFlag) {
        this.requestedFlag = requestedFlag;
    }

    public void setApprovedFlag(Integer approvedFlag) {
        this.approvedFlag = approvedFlag;
    }

    public void setStartedBuyingFlag(Integer startedBuyingFlag) {
        this.startedBuyingFlag = startedBuyingFlag;
    }

    public void setFinishedBuyingFlag(Integer finishedBuyingFlag) {
        this.finishedBuyingFlag = finishedBuyingFlag;
    }

    public void setPickedUpFlag(Integer pickedUpFlag) {
        this.pickedUpFlag = pickedUpFlag;
    }

    public void setReimbursedFlag(Integer reimbursedFlag) {
        this.reimbursedFlag = reimbursedFlag;
    }

    public void setMoneyRemaining(BigDecimal moneyRemaining) {
        this.moneyRemaining = moneyRemaining;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public void setAllocationDeadline(LocalDateTime allocationDeadline) {
        this.allocationDeadline = allocationDeadline;
    }

    public void setDeliberationDeadline(LocalDateTime deliberationDeadline) {
        this.deliberationDeadline = deliberationDeadline;
    }

    public void setReimbursementDeadline(LocalDateTime reimbursementDeadline) {
        this.reimbursementDeadline = reimbursementDeadline;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
    
}
