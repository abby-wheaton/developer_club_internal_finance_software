package com.devwmu.dc_fin_soft.controllers;
import com.devwmu.dc_fin_soft.repositories.ExpenseRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import org.apache.poi.ss.usermodel.*;
import org.apache.commons.io.*;

import org.springframework.http.HttpHeaders;

import com.devwmu.dc_fin_soft.controllers.forms.AmountRequested;
import com.devwmu.dc_fin_soft.entities.Expense;

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
    public ResponseEntity<?> getAllExpenses (){
        //     OUTPUT: all expenses

        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.expenseRepository.findAll());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to retrieve all expenses");
        }
    }

    @PutMapping("/expenses/search")
    @Operation(
        summary = "Filters through expenses based on specified values",
        description = "Takes in a JSON array, where each element is a Filter object consisting of the column to filter by, the operation to filter based on, and the desired value, and returns all of the rows in the Expenses table which match the Filter objects"
    )
    public ResponseEntity<?> filterExpenses(@RequestBody Filter[] filters){
        // filterExpenses(filterArray[]) ?
        //     Take an array of column names and the desired value, and output the selected SQL rows
        //     OUTPUT: expenses
        Specification<Expense> spec = Specification.unrestricted();
        for (Filter filter: filters){
            String col = filter.getCol();
            String op = filter.getOp().toLowerCase();
            Object value = filter.getVal();

            if (value == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: No value provided for filter on column: " + col + "\n");
            }

            Specification<Expense> condition = null;
            switch (op) {
                case "like":
                    try{
                        List<String> allowedCols = List.of("name", "purpose", "vendor", "link", "pickuplocation", "paymenttype");
                        if (!(allowedCols.contains(col.toLowerCase()))){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with LIKE operator\n");
                        }
                        String lower = "%" + value.toString().toLowerCase() + "%";
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.like(criteraBuilder.lower(root.get(col)), lower);
                        break;
                    }
                    catch (ClassCastException e){
                        System.out.println(e + "\n\n\n");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: non-string value passed with LIKE operator\n");
                    }
                 case "bw":
                    // between two dates
                    try {
                        List<String> allowedCols = List.of("itedeadline", "allocationdeadline", "deliberationdeadline", "reimbursementdeadline");
                        if (!(allowedCols.contains(col.toLowerCase()))){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with BETWEEN operator\n");
                        }
                        ArrayList<String> value2 = (ArrayList<String>) value;
                        LocalDateTime date1 = LocalDateTime.parse(value2.get(0));
                        LocalDateTime date2 = LocalDateTime.parse(value2.get(1));
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.between(root.get(col), date1, date2);

                        break;
                    } catch (ClassCastException e){
                        System.out.println(e + "\n\n\n");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: non-date value passed with BETWEEN operator\n");
                }
                case "leq": 
                    try{
                        List<String> allowedOps = List.of("id", "quantity", "priceperunit", "totalprice", "eventid", "sourceid", "moneyremaining", "totalspent");
                        if (!(allowedOps.contains(col.toLowerCase()))){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with LESS THAN OR EQUAL operator\n");
                        }
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
                        List<String> allowedOps = List.of("id", "quantity", "priceperunit", "totalprice", "eventid", "sourceid", "moneyremaining", "totalspent");
                        if (!(allowedOps.contains(col.toLowerCase()))){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with GREATER THAN OR EQUAL operator\n");
                        }
                        Integer val = (Integer) value;
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.greaterThanOrEqualTo(root.get(col), val);
                        break;
                    } catch (ClassCastException e){
                        System.out.println(e + "\n\n\n");
                        break;
                    }
                case "eq":
                    List<String> notAllowedCols = List.of("name", "purpose", "vendor", "link", "pickuplocation", "paymenttype");
                        if (notAllowedCols.contains(col.toLowerCase())){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with EQUAL operator. Pass this with LIKE operator\n");
                        }
                    condition = (root, query, criteriaBuilder) -> 
                        criteriaBuilder.equal(root.get(col), value);
                    break;
            }
            
            if (condition != null){
                spec = spec.and(condition);
            }

        }
        Iterable<Expense> events =  this.expenseRepository.findAll(spec);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(events);
    }

    @PostMapping("/item")
    @Operation(
        summary = "Adds an expense to the Expenses table",
        description = "Takes in a JSON object and adds that Expense to the Expenses table. Returns the object on success"
    )
    public ResponseEntity<?> budgetItem(@RequestBody Expense expense){
        // budgetItem(name, qty, pricePerUnit, totalPrice, purpose, vendor, foodFlag, eventID, source, link, deadline, community, payment_type, pickup_location) bool
        //     Takes in info to create an entry in the Expenses table and outputs if successful
        //     OUTPUT: success or not

        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.expenseRepository.save(expense));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to create event: " + expense.toString() + "\n");
        }
    }

    @PutMapping("/item/edit_{id}")
    @Operation(
        summary = "Edits an expense in the Expenses table",
        description = "Takes in a JSON object and the id of the expense to edit, and edits that expense in the Expenses table with the new values provided. Returns the object on success"
    )
    public ResponseEntity<?> editItem(@PathVariable("id") Integer id, @RequestBody Expense expense){
        // editItem(id, editArray[]): bool
        //     The ID of the item and the array of columns to be changed
        //     OUTPUT: success or not

        Optional<Expense> expenseToUpdateOptional = this.expenseRepository.findById(id);
        if (!expenseToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: Invalid expense id: " + id.toString() + "\n");
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
        
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.expenseRepository.save(expenseToUpdate));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update expense");
        }
    }

    @PutMapping("/item/food_flag_id={id}_num={num}")
    @Operation(
        summary = "Toggles the feeFlag for an expense",
        description = "Using the id provided, it will toggle the foodFlag for an expense to either 1 or 0"
    )
    public ResponseEntity<?> FoodFlagItem(@PathVariable("id") Integer id, @PathVariable("num") Integer num) {
        // deleteItem(id): bool
        //     The id of the item to be updates (from display, not database)
        //     OUTPUT: updated expense

        Optional<Expense> expenseToUpdateOptional = this.expenseRepository.findById(id);
        if (!expenseToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: invalid expense id: " + id.toString() + "\n");
        }
        Expense expense = expenseToUpdateOptional.get();
        if (num == 0){
            expense.setFoodFlag(0);
        }
        else{
            expense.setFoodFlag(1);
        }
        
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.expenseRepository.save(expense));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update expense");
        }   
    }

    @PutMapping("/item/requested_flag_id={id}_num={num}")
    @Operation(
        summary = "Toggles the requestedFlag for an expense",
        description = "Using the id provided, it will toggle the requestedFlag for an expense to either 1 or 0"
    )
    public ResponseEntity<?> requestedFlagItem(@PathVariable("id") Integer id, @PathVariable("num") Integer num) {
        // deleteItem(id): bool
        //     The id of the item to be updates (from display, not database)
        //     OUTPUT: updated expense

        Optional<Expense> expenseToUpdateOptional = this.expenseRepository.findById(id);
        if (!expenseToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: invalid expense id: " + id.toString() + "\n");
        }
        Expense expense = expenseToUpdateOptional.get();
        if (num == 0){
            expense.setRequestedFlag(0);
        }
        else{
            expense.setRequestedFlag(1);
        }
        
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.expenseRepository.save(expense));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update event");
        }   
    }

    @PutMapping("/item/s_buying_flag_id={id}_num={num}")
    @Operation(
        summary = "Toggles the startedBuyingFlag for an expense",
        description = "Using the id provided, it will toggle the startedBuyingFlag for an expense to either 1 or 0"
    )
    public ResponseEntity<?> sBuyingFlagItem(@PathVariable("id") Integer id, @PathVariable("num") Integer num) {
        // deleteItem(id): bool
        //     The id of the item to be updates (from display, not database)
        //     OUTPUT: updated expense

        Optional<Expense> expenseToUpdateOptional = this.expenseRepository.findById(id);
        if (!expenseToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: invalid expense id: " + id.toString() + "\n");
        }
        Expense expense = expenseToUpdateOptional.get();
        if (num == 0){
            expense.setStartedBuyingFlag(0);
        }
        else{
            expense.setStartedBuyingFlag(1);
        }
        
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.expenseRepository.save(expense));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update event");
        }      
    }

    @PutMapping("/item/f_buying_flag_id={id}_num={num}")
    @Operation(
        summary = "Toggles the finishedBuyingFlag for an expense",
        description = "Using the id provided, it will toggle the finishedBuyingFlag for an expense to either 1 or 0"
    )
    public ResponseEntity<?> fBuyingFlagItem(@PathVariable("id") Integer id, @PathVariable("num") Integer num) {
        // deleteItem(id): bool
        //     The id of the item to be updates (from display, not database)
        //     OUTPUT: updated expense

        Optional<Expense> expenseToUpdateOptional = this.expenseRepository.findById(id);
        if (!expenseToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: invalid expense id: " + id.toString() + "\n");
        }
        Expense expense = expenseToUpdateOptional.get();
        if (num == 0){
            expense.setFinishedBuyingFlag(0);
        }
        else{
            expense.setFinishedBuyingFlag(1);
        }
        
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.expenseRepository.save(expense));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update event");
        }      
    }

    @PutMapping("/item/picked_up_flag_id={id}_num={num}")
    @Operation(
        summary = "Toggles the pickedUpFlag for an expense",
        description = "Using the id provided, it will toggle the pickedUpFlag for an expense to either 1 or 0"
    )
    public ResponseEntity<?> pickedUpFlagItem(@PathVariable("id") Integer id, @PathVariable("num") Integer num) {
        // deleteItem(id): bool
        //     The id of the item to be updates (from display, not database)
        //     OUTPUT: updated expense

        Optional<Expense> expenseToUpdateOptional = this.expenseRepository.findById(id);
        if (!expenseToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: invalid expense id: " + id.toString() + "\n");
        }
        Expense expense = expenseToUpdateOptional.get();
        if (num == 0){
            expense.setPickedUpFlag(0);
        }
        else{
            expense.setPickedUpFlag(1);
        }
        
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.expenseRepository.save(expense));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update event");
        }    
    }

    @PutMapping("/item/reimbursed_flag_id={id}_num={num}")
    @Operation(
        summary = "Toggles the reimbursedFlag for an expense",
        description = "Using the id provided, it will toggle the reimbursedFlag for an expense to either 1 or 0"
    )
    public ResponseEntity<?> reimbursedFlagItem(@PathVariable("id") Integer id, @PathVariable("num") Integer num) {
        // deleteItem(id): bool
        //     The id of the item to be updates (from display, not database)
        //     OUTPUT: updated expense

        Optional<Expense> expenseToUpdateOptional = this.expenseRepository.findById(id);
        if (!expenseToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: invalid expense id: " + id.toString() + "\n");
        }
        Expense expense = expenseToUpdateOptional.get();
        if (num == 0){
            expense.setReimbursedFlag(0);
        }
        else{
            expense.setReimbursedFlag(1);
        }
        
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.expenseRepository.save(expense));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update event");
        }     
    }

    @PutMapping("/item/delete_{id}")
    @Operation(
        summary = "Deletes an expense from the Expenses table",
        description = "Modifies the deleted column of the expense based on the id provided to be 1"
    )
    public ResponseEntity<?> deleteItem(@PathVariable("id") Integer id) {
        // deleteItem(id): bool
        //     The id of the item to be deleted (from display, not database)
        //     OUTPUT: success or not

        Optional<Expense> expenseToDeleteOptional = this.expenseRepository.findById(id);
        if (!expenseToDeleteOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: invalid event id: " + id.toString() + "\n");
        }
        Expense expense = expenseToDeleteOptional.get();
        expense.setDeleted(1);
        
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.expenseRepository.save(expense));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update event");
        }    
    }

    @PostMapping("/operational_allocation_form")
    public ResponseEntity<?> createOperationalAllocationForm(@RequestBody AmountRequested[] amountRequests, 
    @RequestParam("rsoName") String rsoName, @RequestParam("rsoRep") String rsoRep, @RequestParam("rsoEmail") String rsoEmail, 
    @RequestParam("rsoMeetingTime") String rsoMeetingTime, @RequestParam("rsoMeetingLocation") String rsoMeetingLocation){
        // custom
        // createConferenceAllocationForm(ExpenseID): bool
        //     Generate a conference request form
        //     OUTPUT: success or not

        // extracts info for form, 
        // make calls to excel api to edit the excel file, 
        // then output the form

        File sourceFile = new File("src/main/java/com/devwmu/dc_fin_soft/controllers/forms/(2026) WSAAC Operational Proposal - RSO Name.xlsx");
        File outfile = new File("src/main/java/com/devwmu/dc_fin_soft/controllers/forms/(2026) WSAAC Operational Proposal - Developer Club.xlsx");
        // copy the file, so that it can work on copy to preserve the source file
        try{
            FileUtils.copyFile(sourceFile, outfile);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: failed to create new form");
        }
        try(FileInputStream infile = new FileInputStream(outfile)){
            // create workbook
            Workbook workbook = WorkbookFactory.create(infile);

            // get proposal sheet
            Sheet sheet = workbook.getSheetAt(0);

            // set rso name
            Row r3 = sheet.getRow(2);
            Cell cellr3cE = r3.getCell(4);
            cellr3cE.setCellValue(rsoName);

            // set rso rep
            Row r4 = sheet.getRow(3);
            Cell cellr4cE = r4.getCell(4);
            cellr4cE.setCellValue(rsoRep);

            // set email 
            Row r5 = sheet.getRow(4);
            Cell cellr5cE = r5.getCell(4);
            cellr5cE.setCellValue(rsoEmail);

            // set signiture
            Row r8 = sheet.getRow(7);
            Cell cellr8cE = r8.getCell(4);
            cellr8cE.setCellValue(rsoRep);

            // set rso meeting time
            Row r11 = sheet.getRow(10);
            Cell cellr11cE = r11.getCell(4);
            cellr11cE.setCellValue(rsoMeetingTime);

            // set rso meeting location
            Row r12 = sheet.getRow(11);
            Cell cellr12cE = r12.getCell(4);
            cellr12cE.setCellValue(rsoMeetingLocation);

            Integer curRow = 21;
            for (AmountRequested amountRequested: amountRequests){

                Optional<Expense> expenseOpt = this.expenseRepository.findById(amountRequested.getId());
                if (!expenseOpt.isPresent()){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Expense id: " + amountRequested.getId().toString() + " was invalid");
                }

                Expense expense = expenseOpt.get();
                // name of item
                Row row = sheet.getRow(curRow);
                Cell cellName = row.getCell(1);
                cellName.setCellValue(expense.getName());

                // vendor name
                Cell cellVendor = row.getCell(4);
                cellVendor.setCellValue(expense.getVendor());

                // cost
                Cell cellCost = row.getCell(5);
                cellCost.setCellValue(expense.getTotalPrice().doubleValue());

                // amount requesting
                Cell cellRequesting = row.getCell(7);
                try{
                    cellRequesting.setCellValue(amountRequested.getAmt().doubleValue());
                } catch (ClassCastException e){
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Incorrect type for amount provided");
                }

                curRow += 1;
            }            

            // calculate formulas
            workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
            try(FileOutputStream outFile = new FileOutputStream(new File("src/main/java/com/devwmu/dc_fin_soft/controllers/forms/(2026) WSAAC Conference Proposal - Developer Club.xlsx"))){
                workbook.write(outFile);
            }catch (Exception e){
                e.printStackTrace();
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: Output file could not be written to");
            }
        } catch (Exception e){
            e.printStackTrace();
            outfile.delete();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: Output file could not be opened");

        }
        // then output the form
        File file = new File("src/main/java/com/devwmu/dc_fin_soft/controllers/forms/(2026) WSAAC Conference Proposal - Developer Club.xlsx");
        try{
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + file.getName() + "\"";
        

            ResponseEntity<InputStreamResource> response =  ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .contentLength(file.length())
                .body(resource);

            
            file.delete();

            return response;
        
        } catch (Exception e){
            e.printStackTrace();
            outfile.delete();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: file not found");
        }
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
