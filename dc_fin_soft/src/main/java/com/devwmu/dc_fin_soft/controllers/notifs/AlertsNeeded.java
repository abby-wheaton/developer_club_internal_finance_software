package com.devwmu.dc_fin_soft.controllers.notifs;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public class AlertsNeeded {
    public AlertsNeeded(LocalDateTime deadline, Integer id, String itemName, String nameDeadline) {
        this.deadline = deadline;
        this.id = id;
        this.itemName = itemName;
        this.nameDeadline = nameDeadline;
    }

    @Schema(description = "The deadline for the alert", example = "2026-05-23T05:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime deadline;
    @Schema(description = "Name of the deadline for the alert (the type of deadline ex. reimbursement, deliberation, etc) with the table name before it", example = "expense item deadline", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nameDeadline;
    @Schema(description = "The id of the expense/request that has that deadline", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;
    @Schema(description = "The name of the expense/request that has that deadline", example = "pencils", requiredMode = Schema.RequiredMode.REQUIRED)
    private String itemName;


    public LocalDateTime getDeadline() {
        return this.deadline;
    }
    public String getNameDeadline() {
        return this.nameDeadline;
    }
    public Integer getId() {
        return this.id;
    }
    public String getItemName() {
        return this.itemName;
    }
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
    public void setNameDeadline(String nameDeadline) {
        this.nameDeadline = nameDeadline;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

}
