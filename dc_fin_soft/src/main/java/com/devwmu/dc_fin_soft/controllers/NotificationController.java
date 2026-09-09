package com.devwmu.dc_fin_soft.controllers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.devwmu.dc_fin_soft.controllers.notifs.AlertsNeeded;
import com.devwmu.dc_fin_soft.controllers.notifs.EmailAlerts;
import com.devwmu.dc_fin_soft.controllers.notifs.EmailService;
import com.devwmu.dc_fin_soft.entities.Expense;
import com.devwmu.dc_fin_soft.entities.FinUser;
import com.devwmu.dc_fin_soft.entities.Request;
import com.devwmu.dc_fin_soft.repositories.ExpenseRepository;
import com.devwmu.dc_fin_soft.repositories.FinUserRepository;
import com.devwmu.dc_fin_soft.repositories.RequestRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
                String id = expense.getId().toString();
                String deadline = expense.getReimbursementDeadline().toString();

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
        if (emailAlerts.getDeliberationDeadline() != 0){
            // get all event dates from now to 2 days in future
            LocalDateTime date1 = LocalDateTime.now();
            LocalDateTime date2 = date1.plusDays(3);
            Iterable<Expense> expensesSoon = this.expenseRepository.findByDeliberationDeadlineBetween(date1, date2);
            
            // send emails to admins for each approaching deadline
            for (Expense expense: expensesSoon){
            try{
                String name = expense.getName();
                String id = expense.getId().toString();
                String deadline = expense.getDeliberationDeadline().toString();

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
                String id = expense.getId().toString();
                String deadline = expense.getItemDeadline().toString();

                for (FinUser admin: admins){
                    String body = "Hello " + admin.getName()+ ",\nThe item deadline for expense item " + name;
                    body = body + " with id of " + id + "is approaching with deadline of " + deadline + ".";
                    this.emailService.sendMail(admin.getEmail(), "WMU Dev Club item deadline upcoming", body);
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
                String id = expense.getId().toString();
                String deadline = expense.getAllocationDeadline().toString();

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
                String id = request.getId().toString();
                String deadline = request.getDeadline().toString();

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
                String id = expense.getId().toString();

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

    @PostMapping("/alerts")
    @Operation(
        summary = "Checks all filters for any alerts or non-urgent pop-ups that need to be sent",
        description = "Takes in no input, and returns all of the deadlines that are soon or past"
    )
    @ApiResponses(value = {
         @ApiResponse(responseCode = "200",
            description = "The alerts were successfully returned",
            content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = AlertsNeeded.class)),
            examples = @ExampleObject(value = "[{\"deadline\": \"2026-05-23T05:00:00\", \"nameDeadline\": \"expense item deadline\", \"id\": 5, \"itemName\": \"pencils\"}]"))})
    })
    public ResponseEntity<?> checkAllAlerts (@RequestBody EmailAlerts emailAlerts){
        ArrayList<AlertsNeeded> alertsNeeded = new ArrayList<>();

        // non-urgent pop-ups in web page
        // make an array of the alerts needed

        // expense: 
        // - Reimbursement deadline soon
        if (emailAlerts.getReimbursementDeadline() != 0){
            // get all event dates from now to 2 days in future
            LocalDateTime date1 = LocalDateTime.now();
            LocalDateTime date2 = date1.plusDays(3);
            Iterable<Expense> expensesSoon = this.expenseRepository.findByReimbursementDeadlineBetween(date1, date2);
            
            // add alerts for each expense with approaching deadline
            for (Expense expense: expensesSoon){
                AlertsNeeded alert = new AlertsNeeded(expense.getReimbursementDeadline(), expense.getId(), expense.getName(), "expense reimbursement deadline");
                alertsNeeded.add(alert);
            }
        }
        // - Deliberation deadline soon
        if (emailAlerts.getDeliberationDeadline() != 0){
            // get all event dates from now to 2 days in future
            LocalDateTime date1 = LocalDateTime.now();
            LocalDateTime date2 = date1.plusDays(3);
            Iterable<Expense> expensesSoon = this.expenseRepository.findByDeliberationDeadlineBetween(date1, date2);
            
            // add alerts for each expense with approaching deadline
            for (Expense expense: expensesSoon){
                AlertsNeeded alert = new AlertsNeeded(expense.getDeliberationDeadline(), expense.getId(), expense.getName(), "expense deliberation deadline");
                alertsNeeded.add(alert);
            }
        }
        // - Item deadline soon
        if (emailAlerts.getItemDeadline() != 0){
            // get all event dates from now to 2 days in future
            LocalDateTime date1 = LocalDateTime.now();
            LocalDateTime date2 = date1.plusDays(3);
            Iterable<Expense> expensesSoon = this.expenseRepository.findByItemDeadlineBetween(date1, date2);
            
            // add alerts for each expense with approaching deadline
            for (Expense expense: expensesSoon){
                AlertsNeeded alert = new AlertsNeeded(expense.getItemDeadline(), expense.getId(), expense.getName(), "expense item deadline");
                alertsNeeded.add(alert);
            }
        }
        // - Allocation deadline soon
        if (emailAlerts.getAllocationDeadline() != 0){
            // get all event dates from now to 2 days in future
            LocalDateTime date1 = LocalDateTime.now();
            LocalDateTime date2 = date1.plusDays(3);
            Iterable<Expense> expensesSoon = this.expenseRepository.findByAllocationDeadlineBetween(date1, date2);
            
            // add alerts for each expense with approaching deadline
            for (Expense expense: expensesSoon){
                AlertsNeeded alert = new AlertsNeeded(expense.getAllocationDeadline(), expense.getId(), expense.getName(), "expense allocation deadline");
                alertsNeeded.add(alert);
            }
        }
        // request:
        // - deadline soon + request approval null
        if (emailAlerts.getRequestDeadline() != 0){
            // get all event dates from now to 2 days in future
            LocalDateTime date1 = LocalDateTime.now();
            LocalDateTime date2 = date1.plusDays(3);
            Iterable<Request> requestsSoon = this.requestRepository.findByDeadlineBetweenAndApprovalIsNull(date1, date2);
            
            // add alerts for each expense with approaching deadline
            for (Request request: requestsSoon){
                AlertsNeeded alert = new AlertsNeeded(request.getDeadline(), request.getId(), request.getItemName(), "request item deadline");
                alertsNeeded.add(alert);
            }
        }
        
        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(alertsNeeded);
    }

}
