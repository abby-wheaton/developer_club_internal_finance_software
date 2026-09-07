package com.devwmu.dc_fin_soft.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.devwmu.dc_fin_soft.controllers.mail.EmailAlerts;
import com.devwmu.dc_fin_soft.repositories.ExpenseRepository;
import com.devwmu.dc_fin_soft.repositories.FinUserRepository;
import com.devwmu.dc_fin_soft.repositories.RequestRepository;
import com.devwmu.dc_fin_soft.entities.*;
import com.devwmu.dc_fin_soft.controllers.mail.EmailService;

import java.util.List;

public class NotificationController {

    private final ExpenseRepository expenseRepository;
    private final FinUserRepository finUserRepository;
    private  final RequestRepository requestRepository;
    private  final EmailService emailService;
    
    NotificationController(ExpenseRepository expenseRepository, FinUserRepository finUserRepository, RequestRepository requestRepository, EmailService emailService) {
        this.expenseRepository = expenseRepository;
        this.finUserRepository = finUserRepository;
        this.requestRepository = requestRepository;
        this.emailService = emailService;
    }


    @PostMapping("/notifications")
    @Operation(
        summary = "Checks all filters for any emails or notifications that need to be sent",
        description = "Takes in no input, and returns all of the deadlines that are soon or past, a new request that was made, and allocation that was not spent"
    )
    @ApiResponses(value = {
         @ApiResponse(responseCode = "200",
            description = "The admins were successfully emailed",
            content = {@Content(mediaType = "text/plains",
            schema = @Schema(type = "string"),
            examples = @ExampleObject(value = "Successfully sent all emails"))}),
         @ApiResponse(responseCode = "500",
            description = "Unable to send email",
            content = {@Content(mediaType = "text/plain",
            schema = @Schema(type = "string"),
            examples = @ExampleObject(value = "Error: failed to send email(s)"))})
    })
    public ResponseEntity<String> checkAllNotifications (@RequestBody EmailAlerts emailAlerts){
        List<FinUser> admins = this.finUserRepository.findByFinGroup(1);
        // expense: 
        // - Reimbursement deadline soon
        if (emailAlerts.getReimbursementDeadline() != 0){
            // get all event dates from now to 2 days in future
            LocalDateTime date1 = LocalDateTime.now();
            LocalDateTime date2 = date1.plusDays(3);
            Iterable<Expense> expensesSoon = this.expenseRepository.findByReimbursementDeadlineBetween(date1, date2);
            
            // send emails to admins for each approaching deadline
            for (Expense expense: expensesSoon){
            try{
                String name = expense.getName();
                Integer id = expense.getId();
                LocalDateTime deadline = expense.getReimbursementDeadline();

                for (FinUser admin: admins){
                    String body = "Hello " + admin.getName()+ ",\nThe reimbursement deadline for expense item " + name;
                    body = body + " with id of " + id + "is approaching with deadline of " + deadline + ".";
                    this.emailService.sendMail(admin.getEmail(), "WMU Dev Club reimbursement deadline upcoming", body);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: failed to send email(s)");
            }
            }
        }
        // - Deliberation deadline soon
        if (emailAlerts.getReimbursementDeadline() != 0){
            // get all event dates from now to 2 days in future
            LocalDateTime date1 = LocalDateTime.now();
            LocalDateTime date2 = date1.plusDays(3);
            Iterable<Expense> expensesSoon = this.expenseRepository.findByDeliberationDeadlineBetween(date1, date2);
            
            // send emails to admins for each approaching deadline
            for (Expense expense: expensesSoon){
            try{
                String name = expense.getName();
                Integer id = expense.getId();
                LocalDateTime deadline = expense.getDeliberationDeadline();

                for (FinUser admin: admins){
                    String body = "Hello " + admin.getName()+ ",\nThe Deliberation deadline for expense item " + name;
                    body = body + " with id of " + id + "is approaching with deadline of " + deadline + ".";
                    this.emailService.sendMail(admin.getEmail(), "WMU Dev Club deliberation deadline upcoming", body);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: failed to send email(s)");
            }
            }
        }
        // - Item deadline soon
        if (emailAlerts.getItemDeadline() != 0){
            // get all event dates from now to 2 days in future
            LocalDateTime date1 = LocalDateTime.now();
            LocalDateTime date2 = date1.plusDays(3);
            Iterable<Expense> expensesSoon = this.expenseRepository.findByItemDeadlineBetween(date1, date2);
            
            // send emails to admins for each approaching deadline
            for (Expense expense: expensesSoon){
            try{
                String name = expense.getName();
                Integer id = expense.getId();
                LocalDateTime deadline = expense.getItemDeadline();

                for (FinUser admin: admins){
                    String body = "Hello " + admin.getName()+ ",\nThe deliberation deadline for expense item " + name;
                    body = body + " with id of " + id + "is approaching with deadline of " + deadline + ".";
                    this.emailService.sendMail(admin.getEmail(), "WMU Dev Club deliberation deadline upcoming", body);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: failed to send email(s)");
            }
            }
        }
        // - Allocation deadline soon
        if (emailAlerts.getAllocationDeadline() != 0){
            // get all event dates from now to 2 days in future
            LocalDateTime date1 = LocalDateTime.now();
            LocalDateTime date2 = date1.plusDays(3);
            Iterable<Expense> expensesSoon = this.expenseRepository.findByAllocationDeadlineBetween(date1, date2);
            
            // send emails to admins for each approaching deadline
            for (Expense expense: expensesSoon){
            try{
                String name = expense.getName();
                Integer id = expense.getId();
                LocalDateTime deadline = expense.getAllocationDeadline();

                for (FinUser admin: admins){
                    String body = "Hello " + admin.getName()+ ",\nThe allocation deadline for expense item " + name;
                    body = body + " with id of " + id + "is approaching with deadline of " + deadline + ".";
                    this.emailService.sendMail(admin.getEmail(), "WMU Dev Club allocation deadline upcoming", body);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: failed to send email(s)");
            }
            }
        }
        // request:
        // - deadline soon + request approval null
        if (emailAlerts.getRequestDeadline() != 0){
            // get all event dates from now to 2 days in future
            LocalDateTime date1 = LocalDateTime.now();
            LocalDateTime date2 = date1.plusDays(3);
            Iterable<Request> requestsSoon = this.requestRepository.findByDeadlineBetweenAndApprovalIsNull(date1, date2);
            
            // send emails to admins for each approaching deadline
            for (Request request: requestsSoon){
            try{
                String name = request.getItemName();
                Integer id = request.getId();
                LocalDateTime deadline = request.getDeadline();

                for (FinUser admin: admins){
                    String body = "Hello " + admin.getName()+ ",\nThe item deadline for request item " + name;
                    body = body + " with id of " + id + ", which has not been marked for approval or disapproval, is approaching with deadline of " + deadline + ".";
                    this.emailService.sendMail(admin.getEmail(), "WMU Dev Club item deadline request upcoming", body);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: failed to send email(s)");
            }
            }
        }
        // - Allocation not spent (money remaining > 0)
        if (emailAlerts.getAllocationNotSpent() != 0){
            // get all event dates from now to 2 days in future
            Iterable<Expense> expensesWithMoneyRemaining = this.expenseRepository.findByMoneyRemainingGreaterThan(BigDecimal.ZERO);
            
            // send emails to admins for each approaching deadline
            for (Expense expense: expensesWithMoneyRemaining){
            try{
                String name = expense.getName();
                Integer id = expense.getId();

                for (FinUser admin: admins){
                    String body = "Hello " + admin.getName()+ ",\nThe item deadline for expense item " + name;
                    body = body + " with id of " + id + "has allocated money not yet spent.";
                    this.emailService.sendMail(admin.getEmail(), "WMU Dev Club allocated money not yet spent", body);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: failed to send email(s)");
            }
            }
        }

        return ResponseEntity.status(HttpStatus.OK)
            .body("Successfully sent all emails");
        
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
        // make an array of the alerts needed

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
