package com.devwmu.dc_fin_soft.controllers.notifs;

import io.swagger.v3.oas.annotations.media.Schema;

public class EmailAlerts {
    // expense: 
    // - Reimbursement deadline soon
    // - Deliberation deadline soon
    // - Item deadline soon
    // - Allocation deadline soon
    // request:
    // - deadline soon + request approval null
    // - Allocation not spent (money remaining > 0)
    // 
    // - Request status updated
    @Schema(description = "A flag for if notifications for the reimbursement deadline for expenses are on", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer reimbursementDeadline;
    @Schema(description = "A flag for if notifications for the item deadline for expenses are on", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer itemDeadline;
    @Schema(description = "A flag for if notifications for the allocation deadline for expenses are on", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer allocationDeadline;
    @Schema(description = "A flag for if notifications for the deliberation deadline for expenses are on", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer deliberationDeadline;
    @Schema(description = "A flag for if notifications for the deadline for requests are on", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer requestDeadline;
    @Schema(description = "A flag for if notifications for allocated money not spent for expenses are on", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer allocationNotSpent;
    
    public Integer getReimbursementDeadline() {
        return reimbursementDeadline;
    }
    public Integer getItemDeadline() {
        return itemDeadline;
    }
    public Integer getAllocationDeadline() {
        return allocationDeadline;
    }
    public Integer getDeliberationDeadline() {
        return deliberationDeadline;
    }
    public Integer getRequestDeadline() {
        return requestDeadline;
    }
    public Integer getAllocationNotSpent() {
        return allocationNotSpent;
    }

    public void setReimbursementDeadline(Integer reimbursementDeadline) {
        this.reimbursementDeadline = reimbursementDeadline;
    }
    public void setItemDeadline(Integer itemDeadline) {
        this.itemDeadline = itemDeadline;
    }
    public void setAllocationDeadline(Integer allocationDeadline) {
        this.allocationDeadline = allocationDeadline;
    }
    public void setDeliberationDeadline(Integer deliberationDeadline) {
        this.deliberationDeadline = deliberationDeadline;
    }
    public void setRequestDeadline(Integer requestDeadline) {
        this.requestDeadline = requestDeadline;
    }
    public void setAllocationNotSpent(Integer allocationNotSpent) {
        this.allocationNotSpent = allocationNotSpent;
    }

    



    
}
