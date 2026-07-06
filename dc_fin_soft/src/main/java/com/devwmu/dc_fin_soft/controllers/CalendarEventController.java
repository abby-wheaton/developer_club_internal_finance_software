package com.devwmu.dc_fin_soft.controllers;
import com.devwmu.dc_fin_soft.DcFinSoftApplication;
import com.devwmu.dc_fin_soft.repositories.CalendarEventRepository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Optional;

import com.devwmu.dc_fin_soft.entities.CalendarEvent;

// Fix outputs and inputs

// @PreAuthorize("hasRole('ADMIN')) to check role of the user
// when they log in, assign/check role

@RestController
@RequestMapping("/calendar_events")
@Tag(name = "Calendar Events", description = "This controller interacts with the Calandar table to display calandar events")
public class CalendarEventController {
    private final CalendarEventRepository calendarEventRepository;

    CalendarEventController(CalendarEventRepository calendarEventRepository, RequestController requestController, DcFinSoftApplication dcFinSoftApplication) {
        this.calendarEventRepository = calendarEventRepository;
    }

    @GetMapping("")
    @Operation(
        summary = "Retrives all of the calandar events",
        description = "Takes in no input, and returns all of the rows in the calandar table"
    )
    public Iterable<CalendarEvent> getAllCalendarEvents(){
        //     OUTPUT: all calendar events

        return this.calendarEventRepository.findAll();
    }

    @PutMapping("/search")
    @Operation(
        summary = "Filters through the calandar events based on specified values",
        description = "Takes in a JSON array, where each element is a Filter object consisting of the column to filter by, the operation to filter based on, and the desired value, and returns all of the rows in the calandar table which match the Filter objects"
    )
    public Iterable<CalendarEvent> filterCalendarEvents(@RequestBody Filter[] filters){
        // filterCalendarEvents(filterArray[]) ???
        //     Take an array of column names and desired values, and output the selected SQL rows
        //     OUTPUT: calendar events

        // returns the events that match
        Specification<CalendarEvent> spec = Specification.unrestricted();
        for (Filter filter: filters){
            String col = filter.getCol();
            String op = filter.getOp().toLowerCase();
            Object value = filter.getVal();

            if (value == null){
                continue;
            }

            Specification<CalendarEvent> condition = null;
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
        return this.calendarEventRepository.findAll(spec);
    }

    @Operation(
        summary = "Adds a calandar event to the Calandar table",
        description = "Takes in a JSON object and adds that CalandarEvent to the Calandar table. Returns the object on success"
    )
    @PostMapping("")
    public CalendarEvent createCalendarEvent(@RequestBody CalendarEvent calendarEvent){
        // createCalendarEvent(name, location, start, end, creator, group, category): success
        //     Uses the input info to enter a calendar event into the database
        //     OUTPUT: success or not

        return this.calendarEventRepository.save(calendarEvent);
    }

    @Operation(
        summary = "Edits a calandar event in the Calandar table",
        description = "Takes in a JSON object and the id of the event to edit, and edits that CalandarEvent in the Calandar table with the new values provided. Returns the object on success"
    )
    @PutMapping("edit_{id}")
    public CalendarEvent editCalendarEvent(@PathVariable("id") Integer id, @RequestBody CalendarEvent calendarEvent){
        // editCalendarEvent(id, editArray[]): success
        //     The ID of the calendar event and the array of columns to be changed
        //     OUTPUT: success or not
        Optional<CalendarEvent> calendarEventToUpdateOptional = this.calendarEventRepository.findById(id);
        if(!calendarEventToUpdateOptional.isPresent()){
            return null;
        }

        CalendarEvent calendarEventToUpdate = calendarEventToUpdateOptional.get();

        if (calendarEvent.getEventName() != null){
            calendarEventToUpdate.setEventName(calendarEvent.getEventName());
        }
        if (calendarEvent.getLocation() != null){
            calendarEventToUpdate.setLocation(calendarEvent.getLocation());
        }
        if (calendarEvent.getStartDateTime() != null){
            calendarEventToUpdate.setStartDateTime(calendarEvent.getStartDateTime());
        }
        if (calendarEvent.getEndDateTime() != null){
            calendarEventToUpdate.setEndDateTime(calendarEvent.getEndDateTime());
        }
        if (calendarEvent.getCreator() != null){
            calendarEventToUpdate.setCreator(calendarEvent.getCreator());
        }
        if (calendarEvent.getGroupId() != null){
            calendarEventToUpdate.setGroupId(calendarEvent.getGroupId());
        }
        if (calendarEvent.getCategory() != null){
            calendarEventToUpdate.setCategory(calendarEvent.getCategory());
        }
        if (calendarEvent.getDeleted() != null){
            calendarEventToUpdate.setDeleted(calendarEvent.getDeleted());
        }
        
        return this.calendarEventRepository.save(calendarEventToUpdate);
           
    }

    @Operation(
        summary = "Deletes an event from the Calandar table",
        description = "Modifies the deleted column of the calandar event based on the id provided to be 1"
    )
    @PutMapping("delete_{id}")
    public CalendarEvent deleteCalendarEvent(@PathVariable("id") Integer id){
        // deleteCalendarEvent(id): success
        //     The id of the calendar event to be deleted (from display, not database)
        //     OUTPUT: success or not

        Optional<CalendarEvent> calendarEventToDeleteOptional = this.calendarEventRepository.findById(id);
        if (!calendarEventToDeleteOptional.isPresent()){
            return null;
        }
        CalendarEvent calendarEvent = calendarEventToDeleteOptional.get();
        calendarEvent.setDeleted(1);
        
        return this.calendarEventRepository.save(calendarEvent);     
    }
    
}
