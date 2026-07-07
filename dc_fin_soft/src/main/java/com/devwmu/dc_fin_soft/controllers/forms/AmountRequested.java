package com.devwmu.dc_fin_soft.controllers.forms;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public class AmountRequested {
    @Schema(description = "The id of the expense", example = "8", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;
    @Schema(description = "The amount requested for this expense", example = "50.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amt;

    public Integer getId(){
        return this.id;
    }

    public BigDecimal getAmt(){
        return this.amt;
    }
}
