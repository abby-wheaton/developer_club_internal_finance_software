package com.devwmu.dc_fin_soft.controllers;

import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;

import java.util.ArrayList;

public class NotificationController {
    
    @GetMapping("/notifications")
    @Operation(
        summary = "Checks all filters for any emails or notifications that need to be sent",
        description = "Takes in no input, and returns all of the deadliens that are soon or past, a new request that was made, and allocation that was not spent"
    )
    public Iterable<String> checkAllNotifications (){
        ArrayList<String> alertsNeeded = new ArrayList<>();
        alertsNeeded.add("alert");

        // emails being sent

    // - New request made
    // - Request status updated
    // - Reimbursement deadline soon
    // - Deliberation deadline soon
    // - Item deadline soon
    // - Allocation deadline soon
    // - Deadline past (all deadlines)
    // - Allocation not spent

        return alertsNeeded;
    }

    @GetMapping("/alerts")
    @Operation(
        summary = "Checks all filters for any alerts or non-urgent pop-ups that need to be sent",
        description = "Takes in no input, and returns all of the deadlines that are soon or past"
    )
    public Iterable<String> checkAllAlerts (){
        ArrayList<String> alertsNeeded = new ArrayList<>();
        alertsNeeded.add("alert");

        // non-urgent pop-ups in web page

    // - Reimbursement deadline soon
    // - Deliberation deadline soon
    // - Item deadline soon
    // - Allocation deadline soon
    // - Deadline past (all deadlines)

        return alertsNeeded;
    }

    @GetMapping("/warnings/food_budget")
    @Operation(
        summary = "checks if an alert is needed when it comes to the food budget",
        description = "Takes in an expense ID and returns if the expense exceeds the given food budget system"
    )
    public Boolean checkFoodBudget (){
        // pop up in web page window
        if(true){
            return true;
        }
        return false;
    }

    @GetMapping("/warnings/allocation_budget")
    @Operation(
        summary = "Checks if an alert is needed regarding money spent and allocation",
        description = "Takes in an expense ID and returns if the expense exceeds the allocation budget"
    )
    public Boolean checkAllocationOver (){
        // pop up in web page window
        if(true){
            return true;
        }
        return false;

    }

}
