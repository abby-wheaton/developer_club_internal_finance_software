package com.devwmu.dc_fin_soft.entities;
import jakarta.persistence.*;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "Source")
public class Source {
        // initalization
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Schema(description = "The name of the source", example = "WSA", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="name")
    private String name;

    @Schema(description = "What type the source is", example = "Event", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Column(name ="type")
    private String type;

    @Schema(description = "A flag for if the source is internal or external", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="internal")
    private Integer internal;

    @Schema(description = "What the cap on money spent is for this source", example = "8000", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="money_cap")
    private BigDecimal moneyCap;

    @Schema(description = "How much money has already been spent for this source", example = "1000", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="spent")
    private BigDecimal spent;

    @Schema(description = "How much money has been budgeted for for this source", example = "1500", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="budgeted")
    private BigDecimal budgeted;

    @Schema(description = "How much money is avaiable for this source", example = "6500", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="available")
    private BigDecimal available;

    @Schema(description = "A flag for if this source is deleted or not", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name ="deleted")
    private Integer deleted;

    // getters

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public Integer getInternal() {
        return this.internal;
    }

    public BigDecimal getMoneyCap() {
        return this.moneyCap;
    }

    public BigDecimal getSpent() {
        return this.spent;
    }

    public BigDecimal getBudgeted() {
        return this.budgeted;
    }

    public BigDecimal getAvailable() {
        return this.available;
    }

    public Integer getDeleted() {
        return this.deleted;
    }

    // setters

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setInternal(Integer internal) {
        this.internal = internal;
    }

    public void setMoneyCap(BigDecimal moneyCap) {
        this.moneyCap = moneyCap;
    }

    public void setSpent(BigDecimal spent) {
        this.spent = spent;
    }

    public void setBudgeted(BigDecimal budgeted) {
        this.budgeted = budgeted;
    }

    public void setAvailable(BigDecimal available) {
        this.available = available;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
    

}
