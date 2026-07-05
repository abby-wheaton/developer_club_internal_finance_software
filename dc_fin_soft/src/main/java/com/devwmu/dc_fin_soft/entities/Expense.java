package com.devwmu.dc_fin_soft.entities;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Table(name = "Expenses")
public class Expense {
    
    // initalization
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name ="name")
    private String name;

    @Column(name ="quantity")
    private Integer quantity;

    @Column(name ="price_per_unit")
    private BigDecimal price_per_unit;

    @Column(name ="total_price")
    private BigDecimal total_price;

    @Column(name ="purpose")
    private String purpose;

    @Column(name ="vendor")
    private String vendor;

    @Column(name ="event_id")
    private Integer event_id;

    @Column(name ="source_id")
    private Integer source_id;

    @Column(name ="link")
    private String link;

    @Column(name ="item_deadline")
    private LocalDateTime item_deadline;

    @Column(name ="community")
    private Integer community;

    @Column(name ="food_flag")
    private Integer food_flag;

    @Column(name ="requested_flag")
    private Integer requested_flag;

    @Column(name ="approved_flag")
    private Integer approved_flag;

    @Column(name ="started_buying_flag")
    private Integer started_buying_flag;

    @Column(name ="finished_buying_flag")
    private Integer finished_buying_flag;

    @Column(name ="picked_up_flag")
    private Integer picked_up_flag;

    @Column(name ="reimbursed_flag")
    private Integer reimbursed_flag;

    @Column(name ="money_remaining")
    private BigDecimal money_remaining;

    @Column(name ="total_spent")
    private BigDecimal total_spent;

    @Column(name ="pickup_location")
    private String pickup_location;

    @Column(name ="allocation_deadline")
    private LocalDateTime allocation_deadline;

    @Column(name ="deliberation_deadline")
    private LocalDateTime deliberation_deadline;

    @Column(name ="reimbursement_deadline")
    private LocalDateTime reimbursement_deadline;

    @Column(name ="payment_type")
    private String payment_type;

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
        return this.event_id;
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
        return this.food_flag;
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
        this.event_id = event_id;
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
        this.food_flag = food_flag;
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
