package com.devwmu.dc_fin_soft.controllers;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import com.devwmu.dc_fin_soft.entities.Event;
import com.devwmu.dc_fin_soft.repositories.EventRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;


// Fix outputs and inputs

@RestController
@RequestMapping("/event")
@Tag(name = "Event Controller", description = "This controller interacts with the Event table in various ways")
public class EventController {
    private final EventRepository eventRepository;

    public EventController(final EventRepository eventRepository) {
    this.eventRepository = eventRepository;
    }
    @Operation(
        summary = "Filters through events based on specified values",
        description = "Takes in a JSON array, where each element is a Filter object consisting of the column to filter by, the operation to filter based on, and the desired value, and returns all of the rows in the Events table which match the Filter objects"
    )
    @PutMapping("/events/search")
    public Iterable<Event> filterEvents(@RequestBody Filter[] filters){
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
                continue;
            }

            Specification<Event> condition = null;
            // looks through the different cases of operations
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
                case "bw":
                    // between two dates
                    try {
                        ArrayList<String> value2 = (ArrayList<String>) value;
                        LocalDateTime date1 = LocalDateTime.parse(value2.get(0));
                        LocalDateTime date2 = LocalDateTime.parse(value2.get(1));
                        condition =  (root, query, criteraBuilder) ->
                            criteraBuilder.between(root.get(col), date1, date2);

                        break;
                    } catch (ClassCastException e){
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
            // if a condition was applied, then it adds it on (all together will chain with other filters)
            if (condition != null){
                spec = spec.and(condition);
            }

        }
        return this.eventRepository.findAll(spec);
    }

    @Operation(
        summary = "Retrives all of the events",
        description = "Takes in no input, and returns all of the rows in the Events table"
    )
    @GetMapping("/all_events")
    public Iterable<Event> getAllEvents() {
        return this.eventRepository.findAll();
    }
    
    @PostMapping("/event")
    @Operation(
        summary = "Adds an event to the Events table",
        description = "Takes in a JSON object and adds that Event to the Events table. Returns the object on success"
    )
    public Event createEvent(@RequestBody Event event){
        // createEvent(name, date, location, attendance, fee?, philanthropy?, conference?):
        //     INPUT: event: Event - the event to be saved to the database
        //     OUTPUT: created event

        // saves the passed Event object to the database
        return this.eventRepository.save(event);
    }

    @PutMapping("/event/edit_{id}")
    @Operation(
        summary = "Edits a calandar event in the Events table",
        description = "Takes in a JSON object and the id of the event to edit, and edits that Event in the Events table with the new values provided. Returns the object on success"
    )
    public Event editEvent(@PathVariable("id") Integer id, @RequestBody Event event){
        // editEvent(id, editArray[]): bool
        //     INPUT: id: int - The ID of the event, event: Event - the updated Event object
        //     OUTPUT: success or not
        Optional<Event> eventToUpdateOptional = this.eventRepository.findById(id);
        if (!eventToUpdateOptional.isPresent()){
            return null;
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

        return this.eventRepository.save(newEvent);
    }

    @PutMapping("/event/fee_flag_id={id}_num={num}")
    @Operation(
        summary = "Toggles the feeFlag for an event",
        description = "Using the id provided, it will toggle the feeFlag for an event to either 1 or 0"
    )
    public Event feeFlagEvent(@PathVariable("id") Integer id, @PathVariable("num") Integer num){
        // feeFlagEvent(id, num): bool
        //     INPUT: id: Integer - The id of the item to change the fee flag (from database), num: Integer -  what to set the flag to
        //     OUTPUT: updated event
        // sets the feeFlag to be 1 or 0
        Optional<Event> eventToUpdateOptional = this.eventRepository.findById(id);
        if (!eventToUpdateOptional.isPresent()){
            return null;
        }

        Event updateEvent = eventToUpdateOptional.get();
        if (num == 0){
            updateEvent.setFeeFlag(0);
        }
        else{
            updateEvent.setFeeFlag(1);
        }
        return this.eventRepository.save(updateEvent);
    }

    @PutMapping("/event/phil_flag_id={id}_num=_{num}")
    @Operation(
        summary = "Toggles the philanthropyFlag for an event",
        description = "Using the id provided, it will toggle the philanthropyFlag for an event to either 1 or 0"
    )
    public Event philFlagEvent(@PathVariable("id") Integer id, @PathVariable("num") Integer num){
        // feeFlagEvent(id, num): bool
        //     INPUT: id: Integer - The id of the item to change the philanthropy flag (from database), num: Integer -  what to set the flag to
        //     OUTPUT: updated event
        // sets the philanthropyFlag to be 1 or 0
        Optional<Event> eventToUpdateOptional = this.eventRepository.findById(id);
        if (!eventToUpdateOptional.isPresent()){
            return null;
        }
        Event updateEvent = eventToUpdateOptional.get();
        if (num == 0){
            updateEvent.setPhilanthropyFlag(0);
        }
        else{
            updateEvent.setPhilanthropyFlag(1);
        }
        return this.eventRepository.save(updateEvent);
    }

    @Operation(
        summary = "Toggles the conferenceFlag for an event",
        description = "Using the id provided, it will toggle the conferenceFlag for an event to either 1 or 0"
    )
    @PutMapping("/event/conf_flag_id={id}_num={num}")
    public Event confFlagEvent(@PathVariable("id") Integer id, @PathVariable("num") Integer num){
        // feeFlagEvent(id, num): bool
        //     INPUT: id: Integer - The id of the item to change the conference flag (from database), num: Integer -  what to set the flag to
        //     OUTPUT: updated event
        // sets the conferenceFlag to be 1 or 0
        Optional<Event> eventToUpdateOptional = this.eventRepository.findById(id);
        if (!eventToUpdateOptional.isPresent()){
            return null;
        }
        Event updateEvent = eventToUpdateOptional.get();
        if (num == 0){
            updateEvent.setConferenceFlag(0);
        }
        else{
            updateEvent.setConferenceFlag(1);
        }
        return this.eventRepository.save(updateEvent);
    }

    @PutMapping("/event/delete_{id}")
    @Operation(
        summary = "Deletes an event from the Events table",
        description = "Modifies the deleted column of the event based on the id provided to be 1"
    )
    public Event deleteEvent(@PathVariable("id") Integer id){
        // deleteEvent(id): bool
        //     INPUT: id: Integer - The id of the item to be deleted (from the database)
        //     OUTPUT: deleted event
        // sets the delete flag to be 1 
        Optional<Event> eventToDeleteOptional = this.eventRepository.findById(id);
        if (!eventToDeleteOptional.isPresent()){
            return null;
        }
        Event deleteEvent = eventToDeleteOptional.get();
        deleteEvent.setDeleted(1);
        return this.eventRepository.save(deleteEvent);
    }

    @PostMapping("/event_allocation_form")
    public Event createEventAllocationForm(){
        // CUSTOM
        // createEventAllocationForm(ExpenseID): bool
        //     Generates an Event request form
        //     OUTPUT: form

        // takes in the event id, extracts info for form, 
        // make calls to excel api to edit the excel file, 
        // then output the form
        return new Event();
    }

    @PostMapping("/conference_allocation_form")
    public Event createConferenceAllocationForm(){
        // custom
        // createConferenceAllocationForm(ExpenseID): bool
        //     Generate a conference request form
        //     OUTPUT: success or not

        // takes in the event id, extracts info for form, 
        // make calls to excel api to edit the excel file, 
        // then output the form
        return new Event();
    }
}
