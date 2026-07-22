package com.devwmu.dc_fin_soft.controllers;
import com.devwmu.dc_fin_soft.repositories.ExpenseRepository;
import com.devwmu.dc_fin_soft.entities.Expense;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devwmu.dc_fin_soft.controllers.forms.AmountRequested;
import com.devwmu.dc_fin_soft.entities.Event;
import com.devwmu.dc_fin_soft.repositories.EventRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.commons.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;


// Fix outputs and inputs

@RestController
@RequestMapping("/event")
@Tag(name = "Event Controller", description = "This controller interacts with the Event table in various ways")
public class EventController {
    private final ExpenseRepository expenseRepository;
    private final EventRepository eventRepository;

    public EventController(final EventRepository eventRepository, final ExpenseRepository expenseRepository) {
        this.eventRepository = eventRepository;
        this.expenseRepository = expenseRepository; 
    }

    
    @Operation(
        summary = "Filters through events based on specified values",
        description = "Takes in a JSON array, where each element is a Filter object consisting of the column to filter by, the operation to filter based on, and the desired value, and returns all of the rows in the Events table which match the Filter objects"
    )
    @PutMapping("/search")
    /** 
   * DESCRIPTION
   * 
   * @param filters an array of filter objects, which represents the columns, operations, and values to filter by
   * @return the rows of Events that match the filters. On error, the filter will not apply. If no filters are applied, returns all of the rows. 
  */
    public ResponseEntity<?> filterEvents(@RequestBody Filter[] filters){
        // filterEvents(filterArray[]) Iterable<Event>
        //     INPUT: filters: Filter[] -  an array of filters to apply to the table
        //      ex) [{"col":"est_attendance", "op":"geq", "val":16}] - this will apply a filter for if the estimated attendance is >= 16
        //     OUTPUT: the selected rows of the events table
    
        // returns the events that match

        Specification<Event> spec = Specification.unrestricted();
        // iterates through each Filter item in the array passed
        for (Filter filter: filters){
            String col = filter.getCol();
            String op = filter.getOp().toLowerCase();
            Object value = filter.getVal();

            if (value == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error: No value provided for filter on column: " + col + "\n");
            }

            Specification<Event> condition = null;
            // looks through the different cases of operations
            switch (op) {
                case "like":
                    try{
                        if (!(col.equalsIgnoreCase("name") | col.equalsIgnoreCase("location"))){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  "passed with LIKE operator\n");
                        }
                        
                        String lower = "%" + value.toString().toLowerCase() + "%";
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.like(criteraBuilder.lower(root.get(col)), lower);

                 
                        break;
                    }
                    catch (ClassCastException e){
                        System.out.println(e);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: non-string value passed with LIKE operator\n");
                    }
                case "bw":
                    // between two dates
                    try {
                        if (!col.equalsIgnoreCase("date")){
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
                        List<String> allowedOps = List.of("id", "estattendance");
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
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: non-number value passed with LESS THAN OR EQUAL TO operator\n");
                    }
                case "geq":
                    try{
                        List<String> allowedOps = List.of("id", "estattendance");
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
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: non-number value passed with GREATER THAN OR EQUAL TO operator\n");
                    }
                case "eq":
                    List<String> notAllowedOps = List.of("name", "location");
                        if (notAllowedOps.contains(col.toLowerCase())){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: invalid column: " + col +  " passed with EQUAL operator. Pass this with LIKE operator\n");
                        }
                    condition = (root, query, criteriaBuilder) -> 
                        criteriaBuilder.equal(root.get(col), value);
                    break;
            }
            // if a condition was applied, then it adds it on (all together will chain with other filters)
            if (condition != null){
                spec = spec.and(condition);
            }

        }
        Iterable<Event> events =  this.eventRepository.findAll(spec);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(events);
    }

    @Operation(
        summary = "Retrives all of the events",
        description = "Takes in no input, and returns all of the rows in the Events table"
    )
    /** 
   * DESCRIPTION
   * 
   * 
   * @return returns all of the rows of the events table
  */
    @GetMapping("/all")
    public ResponseEntity<?> getAllEvents() {
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.eventRepository.findAll());
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to retrieve all events");
        }
    }
    
    @PostMapping("/create")
    @Operation(
        summary = "Adds an event to the Events table",
        description = "Takes in a JSON object and adds that Event to the Events table. Returns the object on success"
    )
    /** 
   * DESCRIPTION
   * 
   * @param event an event object to be added to the table
   * @return will return the created event
  */
    public ResponseEntity<?> createEvent(@RequestBody Event event){
        // createEvent(name, date, location, attendance, fee?, philanthropy?, conference?):
        //     INPUT: event: Event - the event to be saved to the database
        //     OUTPUT: created event

        // saves the passed Event object to the database
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.eventRepository.save(event));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to create event: " + event.toString() + "\n");
        }
    }
    

    @PutMapping("/edit/id={id}")
    @Operation(
        summary = "Edits a calandar event in the Events table",
        description = "Takes in a JSON object and the id of the event to edit, and edits that Event in the Events table with the new values provided. Returns the object on success"
    )
    /** 
   * DESCRIPTION
   * 
   * @param id the id of the event to edit
   * @param event the updated event (what you want the event to be)
   * @return returns the updated event
  */
    public ResponseEntity<?> editEvent(@PathVariable("id") Integer id, @RequestBody Event event){
        // editEvent(id, editArray[]): bool
        //     INPUT: id: int - The ID of the event, event: Event - the updated Event object
        //     OUTPUT: success or not
        Optional<Event> eventToUpdateOptional = this.eventRepository.findById(id);
        if (!eventToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: Invalid event id: " + id.toString() + "\n");
        }
        Event newEvent = eventToUpdateOptional.get();
        // looks through the attributes of the event and if it is set in the object passed, will 
        // set it to be the same in the new event
        if (event.getName() != null){
            newEvent.setName(event.getName());
        }
        if (event.getDate() != null){
            newEvent.setDate(event.getDate());
        }
        if (event.getLocation() != null){
            newEvent.setLocation(event.getLocation());
        }
        if (event.getEstAttendance() != null){
            newEvent.setEstAttendance(event.getEstAttendance());
        }
        if (event.getFeeFlag() != null){
            newEvent.setFeeFlag(event.getFeeFlag());
        }
        if (event.getPhilanthropyFlag() != null){
            newEvent.setPhilanthropyFlag(event.getPhilanthropyFlag());
        }
        if (event.getConferenceFlag() != null){
            newEvent.setConferenceFlag(event.getConferenceFlag());
        }
        if (event.getDeleted() != null){
            newEvent.setDeleted(event.getDeleted());
        }

        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.eventRepository.save(newEvent));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update event\n");
        }
    }

    @PutMapping("/fee_flag/id={id}_val={val}")
    @Operation(
        summary = "Toggles the feeFlag for an event",
        description = "Using the id provided, it will toggle the feeFlag for an event to either 1 or 0"
    )
    /** 
   * DESCRIPTION
   * 
   * @param id the id of the event you want to set the fee flag for
   * @param val the value to set the flag to
   * @return returns the updated event
  */
    public ResponseEntity<?> feeFlagEvent(@PathVariable("id") Integer id, @PathVariable("val") Integer val){
        // feeFlagEvent(id, val): bool
        //     INPUT: id: Integer - The id of the item to change the fee flag (from database), val: Integer -  what to set the flag to
        //     OUTPUT: updated event
        // sets the feeFlag to be 1 or 0
        Optional<Event> eventToUpdateOptional = this.eventRepository.findById(id);
        if (!eventToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: Invalid event id: " + id.toString() + "\n");
        }

        Event updateEvent = eventToUpdateOptional.get();
        if (val == 0){
            updateEvent.setFeeFlag(0);
        }
        else{
            updateEvent.setFeeFlag(1);
        }
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.eventRepository.save(updateEvent));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update event");
        }
    }

    @PutMapping("/phil_flag/id={id}_val=_{val}")
    @Operation(
        summary = "Toggles the philanthropyFlag for an event",
        description = "Using the id provided, it will toggle the philanthropyFlag for an event to either 1 or 0"
    )
    /** 
   * DESCRIPTION
   * 
   * @param id the id of the event you want to set the phil flag for
   * @param val the value to set the flag to
   * @return returns the updated event
  */
    public ResponseEntity<?> philFlagEvent(@PathVariable("id") Integer id, @PathVariable("val") Integer val){
        // feeFlagEvent(id, val): bool
        //     INPUT: id: Integer - The id of the item to change the philanthropy flag (from database), val: Integer -  what to set the flag to
        //     OUTPUT: updated event
        // sets the philanthropyFlag to be 1 or 0
        Optional<Event> eventToUpdateOptional = this.eventRepository.findById(id);
        if (!eventToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: invalid event id: " + id.toString() + "\n");
        }
        Event updateEvent = eventToUpdateOptional.get();
        if (val == 0){
            updateEvent.setPhilanthropyFlag(0);
        }
        else{
            updateEvent.setPhilanthropyFlag(1);
        }
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.eventRepository.save(updateEvent));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update event");
        }
    }

    @Operation(
        summary = "Toggles the conferenceFlag for an event",
        description = "Using the id provided, it will toggle the conferenceFlag for an event to either 1 or 0"
    )
    /** 
   * DESCRIPTION
   * 
   * @param id the id of the event you want to set the conference flag for
   * @param val the value to set the flag to
   * @return returns the updated event
  */
    @PutMapping("/conf_flag/id={id}_val={val}")
    public ResponseEntity<?> confFlagEvent(@PathVariable("id") Integer id, @PathVariable("val") Integer val){
        // feeFlagEvent(id, val): bool
        //     INPUT: id: Integer - The id of the item to change the conference flag (from database), val: Integer -  what to set the flag to
        //     OUTPUT: updated event
        // sets the conferenceFlag to be 1 or 0
        Optional<Event> eventToUpdateOptional = this.eventRepository.findById(id);
        if (!eventToUpdateOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: event id invalid: " + id.toString() + "\n");
        }
        Event updateEvent = eventToUpdateOptional.get();
        if (val == 0){
            updateEvent.setConferenceFlag(0);
        }
        else{
            updateEvent.setConferenceFlag(1);
        }
        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.eventRepository.save(updateEvent));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to update event");
        }
    }

    @PutMapping("/safe_delete/id={id}")
    /** 
   * DESCRIPTION
   * 
   * @param id the id of the event you want to set the deleted flag for
   * @return returns the updated event
  */
    @Operation(
        summary = "Deletes an event from the Events table",
        description = "Modifies the deleted column of the event based on the id provided to be 1"
    )
    public ResponseEntity<?> deleteEvent(@PathVariable("id") Integer id){
        // deleteEvent(id): bool
        //     INPUT: id: Integer - The id of the item to be deleted (from the database)
        //     OUTPUT: deleted event
        // sets the delete flag to be 1 
        Optional<Event> eventToDeleteOptional = this.eventRepository.findById(id);
        if (!eventToDeleteOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: invalid event id: " + id.toString() + "\n");
        }
        Event deleteEvent = eventToDeleteOptional.get();
        deleteEvent.setDeleted(1);

        try{
            return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.eventRepository.save(deleteEvent));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to delete event");
        }
    }

    @Operation(
        summary = "Creates the event allocation form",
        description = "Takes in the id of the event, an array of amount requested objects, and several strings to represent club information, and returns the filled in excel form"
    )
    /** 
   * DESCRIPTION
   * 
   * @param id the id of the event
   * @param amountRequests an array of amount requested objects (each item in the array is used to fill in the amount requested column of the form) there should be an object for every item that will be requested
   * @param rsoName a string which is the name of the rso
   * @param rsoRep a string which is the representative of the rso
   * @param rsoEmail a string which is the email for the rso
   * @return returns the form on success in an http response, and on failure returns empty http response body and an error code
  */
    @PostMapping("/event_allocation_form/id={id}")
    public ResponseEntity<?> createEventAllocationForm(@PathVariable("id") Integer id, @RequestBody AmountRequested[] amountRequests, 
    @RequestParam("rsoName") String rsoName, @RequestParam("rsoRep") String rsoRep, @RequestParam("rsoEmail") String rsoEmail){
        // CUSTOM
        // createEventAllocationForm(ExpenseID): bool
        //     Generates an Event request form
        //     OUTPUT: form
        
        // takes in the event id, extracts info for form, 

        Optional<Event> eventOptional = this.eventRepository.findById(id);
        if (!eventOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: invalid event id: " + id.toString() + "\n");
        }
        Event event = eventOptional.get();
        File sourceFile = new File("src/main/java/com/devwmu/dc_fin_soft/controllers/forms/(2026) WSAAC Event Proposal - RSO Name.xlsx");
        File outfile = new File("src/main/java/com/devwmu/dc_fin_soft/controllers/forms/(2026) WSAAC Event Proposal - Developer Club.xlsx");
        // copy the file, so that it can work on copy to preserve the source file
        try{
            FileUtils.copyFile(sourceFile, outfile);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to create new form\n");
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

            // set event name
            Row r10 = sheet.getRow(9);
            Cell cellr10cE = r10.getCell(4);
            cellr10cE.setCellValue(event.getName());;

            // set event date
            Row r11 = sheet.getRow(10);
            Cell cellr11cE = r11.getCell(4);
            cellr11cE.setCellValue(event.getDate());

            // set event location
            Row r12 = sheet.getRow(11);
            Cell cellr12cE = r12.getCell(4);
            cellr12cE.setCellValue(event.getLocation());

            // set est attendance
            Row r13 = sheet.getRow(12);
            Cell cellr13cE = r13.getCell(4);
            cellr13cE.setCellValue(Integer.toString(event.getEstAttendance()));

            // set fee flag
            Row r14 = sheet.getRow(13);
            Cell cellr14cE = r14.getCell(4);
            if (event.getFeeFlag() == 0){
                cellr14cE.setCellValue("No");
            }
            else{
                cellr14cE.setCellValue("Yes");
            }

            // set event already happened flag
            Row r15 = sheet.getRow(14);
            Cell cellr15cE = r15.getCell(4);
            LocalDateTime now = LocalDateTime.now();
            // checks to see if event has passed
            if (now.isAfter(event.getDate())){
                cellr15cE.setCellValue("Yes");
            }
            else{
                cellr15cE.setCellValue("No");
            }

            // set phil flag
            Row r16 = sheet.getRow(15);
            Cell cellr16cE = r16.getCell(4);
            if (event.getPhilanthropyFlag() == 0){
                cellr16cE.setCellValue("No");
            }
            else{
                cellr16cE.setCellValue("Yes");
            }

            // need to get all of the expenses related to this event that is not food
            Iterable<Expense> expensesNonFood = this.expenseRepository.findByEventIdAndFoodFlag(event.getId(), 0);
            Integer curRow = 21;
            for (Expense expense: expensesNonFood){
                // name of item
                Row row = sheet.getRow(curRow);
                Cell cellName = row.getCell(1);
                cellName.setCellValue(expense.getName());

                // vendor name
                Cell cellVendor = row.getCell(2);
                cellVendor.setCellValue(expense.getVendor());

                // cost
                Cell cellCost = row.getCell(3);
                cellCost.setCellValue(expense.getTotalPrice().doubleValue());

                // amount requesting
                Cell cellRequesting = row.getCell(4);
                try{
                    for (AmountRequested amtReq: amountRequests){
                        if(expense.getId() == amtReq.getId()){
                            cellRequesting.setCellValue(amtReq.getAmt().doubleValue());
                            break;
                        } 
                    }
                } catch (ClassCastException e){
                    e.printStackTrace();

                    outfile.delete();
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Incorrect type passed to amount requested for amount requested\n"); 
                }

                curRow += 1;
            }

            // need to get all of the expenses related to this event that is food
            Iterable<Expense> expensesFood = this.expenseRepository.findByEventIdAndFoodFlag(event.getId(), 1);
            curRow = 21;
            for (Expense expense: expensesFood){
                // name of item
                Row row = sheet.getRow(curRow);
                Cell cellName = row.getCell(6);
                cellName.setCellValue(expense.getName());

                // vendor name
                Cell cellVendor = row.getCell(7);
                cellVendor.setCellValue(expense.getVendor());

                // cost
                Cell cellCost = row.getCell(8);
                cellCost.setCellValue(expense.getTotalPrice().doubleValue());

                // amount requesting
                Cell cellRequesting = row.getCell(9);
                try{
                    for (AmountRequested amtReq: amountRequests){
                        if(expense.getId() == amtReq.getId()){
                            cellRequesting.setCellValue(amtReq.getAmt().doubleValue());
                            break;
                        } 
                    }
                } catch (ClassCastException e){
                    e.printStackTrace();

                    outfile.delete();
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Incorrect type passed to amount requested for amount requested\n"); 
                }

                curRow += 1;
            }

            

            workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
            try(FileOutputStream outFile = new FileOutputStream(new File("src/main/java/com/devwmu/dc_fin_soft/controllers/forms/(2026) WSAAC Event Proposal - Developer Club.xlsx"))){
                workbook.write(outFile);
            }catch (Exception e){
                e.printStackTrace();
                outfile.delete();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: failed to write to file\n");

            }
        } catch (Exception e){
            e.printStackTrace();
            outfile.delete();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: failed to open file\n");
        }
        // make calls to excel api to edit the excel file, 
        // then output the form
        File file = new File("src/main/java/com/devwmu/dc_fin_soft/controllers/forms/(2026) WSAAC Event Proposal - Developer Club.xlsx");
        try{
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + file.getName() + "\"";
        

            ResponseEntity<InputStreamResource> response =  ResponseEntity.ok()
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
            .body("Error: file not found\n");
        }
    } 

    @Operation(
        summary = "Creates the conference allocation form",
        description = "Takes in the id of the event, an array of amount requested objects, and several strings to represent club information, and returns the filled in excel form"
    )
    /** 
   * DESCRIPTION
   * 
   * @param id the id of the event
   * @param amountRequests an array of amount requested objects (each item in the array is used to fill in the amount requested column of the form) there should be an object for every item that will be requested
   * @param rsoName a string which is the name of the rso
   * @param rsoRep a string which is the representative of the rso
   * @param rsoEmail a string which is the email for the rso
   * @param rsoMeetingTime a string which is the times that the rso meetins
   * @param rsoMeetingLocation a string which is the location where the rso regularly meets
   * @return returns the form on success in an http response, and on failure returns empty http response body and an error code
  */
    @PostMapping("/conference_allocation_form/id={id}")
    public ResponseEntity<?> createConferenceAllocationForm(@PathVariable("id") Integer id, @RequestBody AmountRequested[] amountRequests, 
    @RequestParam("rsoName") String rsoName, @RequestParam("rsoRep") String rsoRep, @RequestParam("rsoEmail") String rsoEmail, 
    @RequestParam("rsoMeetingTime") String rsoMeetingTime, @RequestParam("rsoMeetingLocation") String rsoMeetingLocation){
        // custom
        // createConferenceAllocationForm(ExpenseID): bool
        //     Generate a conference request form
        //     OUTPUT: success or not

        // takes in the event id, extracts info for form, 
        // make calls to excel api to edit the excel file, 
        // then output the form

        Optional<Event> eventOptional = this.eventRepository.findById(id);
        if (!eventOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Error: invalid event id: " + id.toString() + "\n");
        }
        Event event = eventOptional.get();
        File sourceFile = new File("src/main/java/com/devwmu/dc_fin_soft/controllers/forms/(2026) WSAAC Conference Proposal - RSO Name.xlsx");
        File outfile = new File("src/main/java/com/devwmu/dc_fin_soft/controllers/forms/(2026) WSAAC Conference Proposal - Developer Club.xlsx");
        // copy the file, so that it can work on copy to preserve the source file
        try{
            FileUtils.copyFile(sourceFile, outfile);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: unable to create new form\n");
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

            // set conference name
            Row r14 = sheet.getRow(13);
            Cell cellr14cE = r14.getCell(4);
            cellr14cE.setCellValue(event.getName());

            // set event location
            Row r15 = sheet.getRow(14);
            Cell cellr15cE = r15.getCell(4);
            cellr15cE.setCellValue(event.getLocation());

            // set conference date
            Row r16 = sheet.getRow(15);
            Cell cellr16cE = r16.getCell(4);
            cellr16cE.setCellValue(event.getDate());

            // need to get all of the expenses related to this event that is not food
            Iterable<Expense> expensesNonFood = this.expenseRepository.findByEventIdAndFoodFlag(event.getId(), 0);
            Integer curRow = 21;
            for (Expense expense: expensesNonFood){
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
                    for (AmountRequested amtReq: amountRequests){
                        if(expense.getId() == amtReq.getId()){
                            cellRequesting.setCellValue(amtReq.getAmt().doubleValue());
                            break;
                        } 
                    }
                } catch (ClassCastException e){
                    e.printStackTrace();

                    outfile.delete();
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Incorrect type passed to amount requested for amount requested\n"); 
                }

                curRow += 1;
            }            

            // calculate formulas
            workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
            try(FileOutputStream outFile = new FileOutputStream(new File("src/main/java/com/devwmu/dc_fin_soft/controllers/forms/(2026) WSAAC Conference Proposal - Developer Club.xlsx"))){
                workbook.write(outFile);
            }catch (Exception e){
                e.printStackTrace();
                outfile.delete();
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
        

            ResponseEntity<InputStreamResource> response =  ResponseEntity.ok()
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
}
