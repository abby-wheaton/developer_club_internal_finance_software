package com.devwmu.dc_fin_soft.controllers;
import com.devwmu.dc_fin_soft.repositories.ExpenseRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import com.devwmu.dc_fin_soft.entities.Expense;

// Fix outputs and inputs

@RestController
@RequestMapping("/expense")
@Tag(name = "Expense Controller", description = "This controller interacts with the expense table in various ways")
public class ExpenseController {
    private final ExpenseRepository expenseRepository;

    ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("/expenses")
    @Operation(
        summary = "Retrieves all of the expenses",
        description = "Takes in no input, and returns all of the rows in the Expense table"
    )
    public Iterable<Expense> getAllExpenses (){
        //     OUTPUT: all expenses

        return this.expenseRepository.findAll();
    }

    @PutMapping("/expenses/search")
    @Operation(
        summary = "Filters through expenses based on specified values",
        description = "Takes in a JSON array, where each element is a Filter object consisting of the column to filter by, the operation to filter based on, and the desired value, and returns all of the rows in the Expenses table which match the Filter objects"
    )
    public Iterable<Expense> filterExpenses(@RequestBody Filter[] filters){
        // filterExpenses(filterArray[]) ?
        //     Take an array of column names and the desired value, and output the selected SQL rows
        //     OUTPUT: expenses
        Specification<Expense> spec = Specification.unrestricted();
        for (Filter filter: filters){
            String col = filter.getCol();
            String op = filter.getOp().toLowerCase();
            Object value = filter.getVal();

            if (value == null){
                continue;
            }

            Specification<Expense> condition = null;
            switch (op) {
                case "like":
                    try{
                        String lower = "%" + value.toString().toLowerCase() + "%";
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.like(criteraBuilder.lower(root.get(col)), lower);
                        break;
                    }
                    catch (ClassCastException e){
                        System.out.println(e + "\n\n\n");
                        break;
                    }
                case "leq": 
                    try{
                        Integer val = (Integer) value;
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.lessThanOrEqualTo(root.get(col), val);
                        break;
                    } catch (ClassCastException e){
                        System.out.println(e + "\n\n\n");
                        break;
                    }
                case "geq":
                    try{
                        Integer val = (Integer) value;
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.greaterThanOrEqualTo(root.get(col), val);
                        break;
                    } catch (ClassCastException e){
                        System.out.println(e + "\n\n\n");
                        break;
                    }
                case "eq":
                    condition = (root, query, criteriaBuilder) -> 
                        criteriaBuilder.equal(root.get(col), value);
                    break;
            }
            
            if (condition != null){
                spec = spec.and(condition);
            }

        }
        return this.expenseRepository.findAll(spec);
    }

    @PostMapping("/item")
    @Operation(
        summary = "Adds an expense to the Expenses table",
        description = "Takes in a JSON object and adds that Expense to the Expenses table. Returns the object on success"
    )
    public Expense budgetItem(@RequestBody Expense expense){
        // budgetItem(name, qty, pricePerUnit, totalPrice, purpose, vendor, foodFlag, eventID, source, link, deadline, community, payment_type, pickup_location) bool
        //     Takes in info to create an entry in the Expenses table and outputs if successful
        //     OUTPUT: success or not

        return this.expenseRepository.save(expense);
    }

    @PutMapping("/item/edit_{id}")
    @Operation(
        summary = "Edits an expense in the Expenses table",
        description = "Takes in a JSON object and the id of the expense to edit, and edits that expense in the Expenses table with the new values provided. Returns the object on success"
    )
    public Expense editItem(@PathVariable("id") Integer id, @RequestBody Expense expense){
        // editItem(id, editArray[]): bool
        //     The ID of the item and the array of columns to be changed
        //     OUTPUT: success or not

        Optional<Expense> expenseToUpdateOptional = this.expenseRepository.findById(id);
        if (!expenseToUpdateOptional.isPresent()){
            return null;
        }

        Expense expenseToUpdate = expenseToUpdateOptional.get();
        if (expense.getName() != null){
            expenseToUpdate.setName(expense.getName());
        }
        if (expense.getQuantity() != null){
            expenseToUpdate.setQuantity(expense.getQuantity());
        }
        if (expense.getPricePerUnit() != null){
            expenseToUpdate.setPricePerUnit(expense.getPricePerUnit());
        }
        if (expense.getTotalPrice() != null){
            expenseToUpdate.setTotalPrice(expense.getTotalPrice());
        }
        if (expense.getPurpose() != null){
            expenseToUpdate.setPurpose(expense.getPurpose());
        }
        if (expense.getVendor() != null){
            expenseToUpdate.setVendor(expense.getVendor());
        }
        if (expense.getSourceId() != null){
            expenseToUpdate.setSourceId(expense.getSourceId());
        }
        if (expense.getLink() != null){
            expenseToUpdate.setLink(expense.getLink());
        }
        if (expense.getItemDeadline() != null){
            expenseToUpdate.setItemDeadline(expense.getItemDeadline());
        }
        if (expense.getCommunity() != null){
            expenseToUpdate.setCommunity(expense.getCommunity());
        }
        if (expense.getFoodFlag() != null){
            expenseToUpdate.setFoodFlag(expense.getFoodFlag());
        }
        if (expense.getRequestedFlag() != null){
            expenseToUpdate.setRequestedFlag(expense.getRequestedFlag());
        }
        if (expense.getApprovedFlag() != null){
            expenseToUpdate.setApprovedFlag(expense.getApprovedFlag());
        }
        if (expense.getStartedBuyingFlag() != null){
            expenseToUpdate.setStartedBuyingFlag(expense.getStartedBuyingFlag());
        }
        if (expense.getFinishedBuyingFlag() != null){
            expenseToUpdate.setFinishedBuyingFlag(expense.getFinishedBuyingFlag());
        }
        if (expense.getPickedUpFlag() != null){
            expenseToUpdate.setPickedUpFlag(expense.getPickedUpFlag());
        }
        if (expense.getReimbursedFlag() != null){
            expenseToUpdate.setReimbursedFlag(expense.getReimbursedFlag());
        }
        if (expense.getMoneyRemaining() != null){
            expenseToUpdate.setMoneyRemaining(expense.getMoneyRemaining());
        }
        if (expense.getTotalSpent() != null){
            expenseToUpdate.setTotalSpent(expense.getTotalSpent());
        }
        if (expense.getPickupLocation() != null){
            expenseToUpdate.setPickupLocation(expense.getPickupLocation());
        }
        if (expense.getAllocationDeadline() != null){
            expenseToUpdate.setAllocationDeadline(expense.getAllocationDeadline());
        }
        if (expense.getDeliberationDeadline() != null){
            expenseToUpdate.setDeliberationDeadline(expense.getDeliberationDeadline());
        }
        if (expense.getReimbursementDeadline() != null){
            expenseToUpdate.setReimbursementDeadline(expense.getReimbursementDeadline());
        }
        if (expense.getPaymentType() != null){
            expenseToUpdate.setPaymentType(expense.getPaymentType());
        }
        if (expense.getDeleted() != null){
            expenseToUpdate.setDeleted(expense.getDeleted());
        }
        
        return this.expenseRepository.save(expenseToUpdate);
    }

    @PutMapping("/item/food_flag_id={id}_num={num}")
    @Operation(
        summary = "Toggles the feeFlag for an expense",
        description = "Using the id provided, it will toggle the foodFlag for an expense to either 1 or 0"
    )
    public Expense FoodFlagItem(@PathVariable("id") Integer id, @PathVariable("num") Integer num) {
        // deleteItem(id): bool
        //     The id of the item to be updates (from display, not database)
        //     OUTPUT: updated expense

        Optional<Expense> expenseToUpdateOptional = this.expenseRepository.findById(id);
        if (!expenseToUpdateOptional.isPresent()){
            return null;
        }
        Expense expense = expenseToUpdateOptional.get();
        if (num == 0){
            expense.setFoodFlag(0);
        }
        else{
            expense.setFoodFlag(1);
        }
        
        return this.expenseRepository.save(expense);    
    }

    @PutMapping("/item/requested_flag_id={id}_num={num}")
    @Operation(
        summary = "Toggles the requestedFlag for an expense",
        description = "Using the id provided, it will toggle the requestedFlag for an expense to either 1 or 0"
    )
    public Expense requestedFlagItem(@PathVariable("id") Integer id, @PathVariable("num") Integer num) {
        // deleteItem(id): bool
        //     The id of the item to be updates (from display, not database)
        //     OUTPUT: updated expense

        Optional<Expense> expenseToUpdateOptional = this.expenseRepository.findById(id);
        if (!expenseToUpdateOptional.isPresent()){
            return null;
        }
        Expense expense = expenseToUpdateOptional.get();
        if (num == 0){
            expense.setRequestedFlag(0);
        }
        else{
            expense.setRequestedFlag(1);
        }
        
        return this.expenseRepository.save(expense);    
    }

    @PutMapping("/item/s_buying_flag_id={id}_num={num}")
    @Operation(
        summary = "Toggles the startedBuyingFlag for an expense",
        description = "Using the id provided, it will toggle the startedBuyingFlag for an expense to either 1 or 0"
    )
    public Expense sBuyingFlagItem(@PathVariable("id") Integer id, @PathVariable("num") Integer num) {
        // deleteItem(id): bool
        //     The id of the item to be updates (from display, not database)
        //     OUTPUT: updated expense

        Optional<Expense> expenseToUpdateOptional = this.expenseRepository.findById(id);
        if (!expenseToUpdateOptional.isPresent()){
            return null;
        }
        Expense expense = expenseToUpdateOptional.get();
        if (num == 0){
            expense.setStartedBuyingFlag(0);
        }
        else{
            expense.setStartedBuyingFlag(1);
        }
        
        return this.expenseRepository.save(expense);    
    }

    @PutMapping("/item/f_buying_flag_id={id}_num={num}")
    @Operation(
        summary = "Toggles the finishedBuyingFlag for an expense",
        description = "Using the id provided, it will toggle the finishedBuyingFlag for an expense to either 1 or 0"
    )
    public Expense fBuyingFlagItem(@PathVariable("id") Integer id, @PathVariable("num") Integer num) {
        // deleteItem(id): bool
        //     The id of the item to be updates (from display, not database)
        //     OUTPUT: updated expense

        Optional<Expense> expenseToUpdateOptional = this.expenseRepository.findById(id);
        if (!expenseToUpdateOptional.isPresent()){
            return null;
        }
        Expense expense = expenseToUpdateOptional.get();
        if (num == 0){
            expense.setFinishedBuyingFlag(0);
        }
        else{
            expense.setFinishedBuyingFlag(1);
        }
        
        return this.expenseRepository.save(expense);    
    }

    @PutMapping("/item/picked_up_flag_id={id}_num={num}")
    @Operation(
        summary = "Toggles the pickedUpFlag for an expense",
        description = "Using the id provided, it will toggle the pickedUpFlag for an expense to either 1 or 0"
    )
    public Expense pickedUpFlagItem(@PathVariable("id") Integer id, @PathVariable("num") Integer num) {
        // deleteItem(id): bool
        //     The id of the item to be updates (from display, not database)
        //     OUTPUT: updated expense

        Optional<Expense> expenseToUpdateOptional = this.expenseRepository.findById(id);
        if (!expenseToUpdateOptional.isPresent()){
            return null;
        }
        Expense expense = expenseToUpdateOptional.get();
        if (num == 0){
            expense.setPickedUpFlag(0);
        }
        else{
            expense.setPickedUpFlag(1);
        }
        
        return this.expenseRepository.save(expense);    
    }

    @PutMapping("/item/reimbursed_flag_id={id}_num={num}")
    @Operation(
        summary = "Toggles the reimbursedFlag for an expense",
        description = "Using the id provided, it will toggle the reimbursedFlag for an expense to either 1 or 0"
    )
    public Expense reimbursedFlagItem(@PathVariable("id") Integer id, @PathVariable("num") Integer num) {
        // deleteItem(id): bool
        //     The id of the item to be updates (from display, not database)
        //     OUTPUT: updated expense

        Optional<Expense> expenseToUpdateOptional = this.expenseRepository.findById(id);
        if (!expenseToUpdateOptional.isPresent()){
            return null;
        }
        Expense expense = expenseToUpdateOptional.get();
        if (num == 0){
            expense.setReimbursedFlag(0);
        }
        else{
            expense.setReimbursedFlag(1);
        }
        
        return this.expenseRepository.save(expense);    
    }

    @PutMapping("/item/delete_{id}")
    @Operation(
        summary = "Deletes an expense from the Expenses table",
        description = "Modifies the deleted column of the expense based on the id provided to be 1"
    )
    public Expense deleteItem(@PathVariable("id") Integer id) {
        // deleteItem(id): bool
        //     The id of the item to be deleted (from display, not database)
        //     OUTPUT: success or not

        Optional<Expense> expenseToDeleteOptional = this.expenseRepository.findById(id);
        if (!expenseToDeleteOptional.isPresent()){
            return null;
        }
        Expense expense = expenseToDeleteOptional.get();
        expense.setDeleted(1);
        
        return this.expenseRepository.save(expense);    
    }

    @PostMapping("/operational_allocation_form")
    public Expense createOperationalAllocationForm(){
        // createOperationalAllocationForm(ExpenseID): bool
        //     Generate an operational allocation request form
        //     OUTPUT: success or not

        return new Expense();
    }

    @PostMapping("/allocation_not_spent")
    public Expense allocationNotSpentAlert(){
        // allocationNotSpentAlert(expenseID) success
        //     Sends an alert to the dev club admin if [CONDITION] 
        //     OUTPUT: success or not

        return new Expense();
    }

    @PostMapping("/food_budget_too_high")
    public Expense foodBudgetTooHighAlert(){
        // foodBudgetTooHighAlert(expenseID) success
        //     Sends an alert to the dev club admin if the food budget is greater than 20 dollars per estimated attendee
        //     OUTPUT: success or not
        return new Expense();
    }

    @PostMapping("/allocation_too_big")
    public Expense allocationTooBigAlert(){
        // allocationTooBigAlert(expenseID) success
        //     Sends an alert to the dev club admin if the allocation is too big for the amount of money in the source
        //     OUTPUT: success or not
        return new Expense();
    }

    @PostMapping("/deadline_past")
    public Expense deadlinePastAlert(){
        // deadlinePastAlert(expenseID) success
        //     Sends an alert to the dev club admin if the deadline for an item is in the past
        //     OUTPUT: success or not
        return new Expense();
    }

    @GetMapping("/total_price")
    public Expense calculateRecommendedTotalPrice(){
        // calcRecommendedTotalPrice(qty, pricePerUnit) double
        //     Calculating total price
        //     OUTPUT: recommended total price

        return new Expense();
    }

    @GetMapping("/source")
    public Expense calculateRecommendedSource(){
        // calcRecommendedSource(totalPrice, type, curDate) str
        //     Recommends a source
        //     OUTPUT: recommended source

        return new Expense();
    }

    @PostMapping("/receipt")
    public Expense addReceipt(){
        // addReceipt(imgFile) success
        //     Adds a receipt to the Google Drive
        //     OUTPUT: success or not

        return new Expense();
    }
}
