package com.devwmu.dc_fin_soft.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devwmu.dc_fin_soft.entities.Event;
import com.devwmu.dc_fin_soft.repositories.EventRepository;
import com.devwmu.dc_fin_soft.repositories.ExpenseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;



@WebMvcTest(EventController.class)
public class EventControllerTest {
    // web level testing - tests that path goes to correct function, HTTP codes, json serialization, and inputs
    @Autowired 
    private MockMvc mockMvc;

    @MockitoBean 
    private EventRepository eventRepository;

    @MockitoBean 
    private ExpenseRepository expenseRepository;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());

    // /filter events tests

    // /all
    @Test
    void allEventsSuccessTest() throws Exception{
        LocalDateTime datetime = LocalDateTime.of(2026, 5, 23, 5, 0, 0);
        Event event1 = new Event(0, datetime, 0, 15, 0, 1, "Floyd", "Testing Event", 0);

        Event event2 = new Event(1, datetime, 0, 25, 0, 2, "Rood Hall", "Testing Event 2", 0);

        List<Event> mockEvents = new ArrayList<>();
        mockEvents.add(event1);
        mockEvents.add(event2);

        // mock the return from find all call
        when(eventRepository.findAll()).thenReturn(mockEvents);

        // assert the actual pathing is returning what it should with the mocked arr above
        mockMvc.perform(get("/event/all")
            .accept(MediaType.APPLICATION_JSON))
            //check status code and that output is an array of correct length
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))

            //checl that the content of each event is correct
            // event 1
            .andExpect(jsonPath("$[0].conferenceFlag").value(0))
            .andExpect(jsonPath("$[0].date").value("2026-05-23T05:00:00"))
            .andExpect(jsonPath("$[0].deleted").value(0))
            .andExpect(jsonPath("$[0].estAttendance").value(15))
            .andExpect(jsonPath("$[0].feeFlag").value(0))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].location").value("Floyd"))
            .andExpect(jsonPath("$[0].name").value("Testing Event"))
            .andExpect(jsonPath("$[0].philanthropyFlag").value(0))

            // event 2
            .andExpect(jsonPath("$[1].conferenceFlag").value(1))
            .andExpect(jsonPath("$[1].date").value("2026-05-23T05:00:00"))
            .andExpect(jsonPath("$[1].deleted").value(0))
            .andExpect(jsonPath("$[1].estAttendance").value(25))
            .andExpect(jsonPath("$[1].feeFlag").value(0))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].location").value("Rood Hall"))
            .andExpect(jsonPath("$[1].name").value("Testing Event 2"))
            .andExpect(jsonPath("$[1].philanthropyFlag").value(0))
            ;
    }

    @Test
    void allEventsEmptyTest() throws Exception{
        List<Event> mockEvents = new ArrayList<>();

        // mock the return from find all call
        when(eventRepository.findAll()).thenReturn(mockEvents);

        // check that the array returned is empty and with 200 code
        mockMvc.perform(get("/event/all")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(0));

            
    }

    @Test
    void allEventsExceptionTest() throws Exception{
        // mock the return from find all call
        when(eventRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // check that the array returned is empty and with 500 code
        mockMvc.perform(get("/event/all")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("Error: unable to retrieve all events"));

            
    }



    // /create tests
    @Test
    void createSuccessTest() throws Exception{
        LocalDateTime datetime = LocalDateTime.of(2026, 5, 23, 5, 0, 0);
        Event event = new Event(0, datetime, 0, 15, 0, 1, "Floyd", "Testing Event", 0);

        // mock the return from find all call
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // assert the actual pathing is returning what it should with the mocked arr above
        mockMvc.perform(post("/event/create")
            .content(objectMapper.writeValueAsString(event))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            //check status code and that output is correct
            .andExpect(status().isOk())

            // event 
            .andExpect(jsonPath("$.conferenceFlag").value(0))
            .andExpect(jsonPath("$.date").value("2026-05-23T05:00:00"))
            .andExpect(jsonPath("$.deleted").value(0))
            .andExpect(jsonPath("$.estAttendance").value(15))
            .andExpect(jsonPath("$.feeFlag").value(0))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.location").value("Floyd"))
            .andExpect(jsonPath("$.name").value("Testing Event"))
            .andExpect(jsonPath("$.philanthropyFlag").value(0));
    }

    @Test
    void createFailureTest() throws Exception{
         LocalDateTime datetime = LocalDateTime.of(2026, 5, 23, 5, 0, 0);
        Event event = new Event(0, datetime, 0, 15, 0, 1, "Floyd", "Testing Event", 0);

        // mock the return from find all call
        when(eventRepository.save(any(Event.class))).thenThrow(new RuntimeException("Database Error"));

        // check that the array returned is empty and with 500 code
        mockMvc.perform(post("/event/create")
            .content(objectMapper.writeValueAsString(event))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string(startsWith("Error: unable to create event: ")));
    }

    // /edit tests
    // edit success test
    @Test
    void editSuccessTest() throws Exception{
        LocalDateTime datetime = LocalDateTime.of(2026, 5, 23, 5, 0, 0);
        Event event = new Event(0, datetime, 0, 15, 0, 1, "Floyd", "Testing Event", 0);
        Optional<Event> optEvent = Optional.ofNullable(event);

        // mock the return from find all call
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // mock the find by id
        when(eventRepository.findById(1)).thenReturn(optEvent);

        // check that the array returned is empty and with 500 code
        mockMvc.perform(put("/event/edit/id=1", 1)
            .content(objectMapper.writeValueAsString(event))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            // event 
            .andExpect(jsonPath("$.conferenceFlag").value(0))
            .andExpect(jsonPath("$.date").value("2026-05-23T05:00:00"))
            .andExpect(jsonPath("$.deleted").value(0))
            .andExpect(jsonPath("$.estAttendance").value(15))
            .andExpect(jsonPath("$.feeFlag").value(0))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.location").value("Floyd"))
            .andExpect(jsonPath("$.name").value("Testing Event"))
            .andExpect(jsonPath("$.philanthropyFlag").value(0));
    }

    // edit invalid id test
     @Test
    void editInvalidIdTest() throws Exception{
        LocalDateTime datetime = LocalDateTime.of(2026, 5, 23, 5, 0, 0);
        Event event = new Event(0, datetime, 0, 15, 0, 1, "Floyd", "Testing Event", 0);
        // mock the find by id
        when(eventRepository.findById(1)).thenReturn(Optional.empty());

        // check that the array returned is empty and with 500 code
        mockMvc.perform(put("/event/edit/id=1", 1)
            .content(objectMapper.writeValueAsString(event))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Error: Invalid event id: 1"));
    }
    // edit success w/ null values test
     @Test
    void editNullSuccessTest() throws Exception{
        Event event = new Event(0, null, null, 15, 0, 1, "Floyd", "Testing Event", 0);
        Optional<Event> optEvent = Optional.ofNullable(event);
        // mock the find by id
        when(eventRepository.findById(1)).thenReturn(optEvent);

        //mock the save
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // check that the array returned is empty and with 500 code
        mockMvc.perform(put("/event/edit/id=1", 1)
            .content(objectMapper.writeValueAsString(event))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            // event 
            .andExpect(jsonPath("$.conferenceFlag").value(0))
            .andExpect(jsonPath("$.date").value(nullValue()))
            .andExpect(jsonPath("$.deleted").value(nullValue()))
            .andExpect(jsonPath("$.estAttendance").value(15))
            .andExpect(jsonPath("$.feeFlag").value(0))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.location").value("Floyd"))
            .andExpect(jsonPath("$.name").value("Testing Event"))
            .andExpect(jsonPath("$.philanthropyFlag").value(0));
    }
    // edit failure test
    @Test
    void editFailureTest() throws Exception{
        Event event = new Event(0, null, null, 15, 0, 1, "Floyd", "Testing Event", 0);
        Optional<Event> optEvent = Optional.ofNullable(event);
        // mock the find by id
        when(eventRepository.findById(1)).thenReturn(optEvent);

        //mock the save
        when(eventRepository.save(any(Event.class))).thenThrow(new RuntimeException("Database Error"));

        // check that the array returned is empty and with 500 code
        mockMvc.perform(put("/event/edit/id=1", 1)
            .content(objectMapper.writeValueAsString(event))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            // event 
            .andExpect(content().string("Error: unable to update event"));
    }
    
    //feeFlagEventTest{
        // invididual tests
        // fee flag 0 success test
    @Test
    void feeFlagZeroSuccessTest() throws Exception{
        LocalDateTime datetime = LocalDateTime.of(2026, 5, 23, 5, 0, 0);
        Event event = new Event(0, datetime, 0, 15, 0, 1, "Floyd", "Testing Event", 0);
        Optional<Event> optEvent = Optional.ofNullable(event);
        // mock the find by id
        when(eventRepository.findById(1)).thenReturn(optEvent);

        //mock the save
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        mockMvc.perform(put("/event/fee_flag/id=1_val=0", 1, 0)
            .content(objectMapper.writeValueAsString(event))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            // event 
            .andExpect(jsonPath("$.conferenceFlag").value(0))
            .andExpect(jsonPath("$.date").value("2026-05-23T05:00:00"))
            .andExpect(jsonPath("$.deleted").value(0))
            .andExpect(jsonPath("$.estAttendance").value(15))
            .andExpect(jsonPath("$.feeFlag").value(0))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.location").value("Floyd"))
            .andExpect(jsonPath("$.name").value("Testing Event"))
            .andExpect(jsonPath("$.philanthropyFlag").value(0));
    }
        // fee flag 1 success test

    @Test
    void feeFlagOneSuccessTest() throws Exception{
        LocalDateTime datetime = LocalDateTime.of(2026, 5, 23, 5, 0, 0);
        Event event = new Event(0, datetime, 0, 15, 0, 1, "Floyd", "Testing Event", 0);
        Optional<Event> optEvent = Optional.ofNullable(event);
        // mock the find by id
        when(eventRepository.findById(1)).thenReturn(optEvent);

        //mock the save
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        mockMvc.perform(put("/event/fee_flag/id=1_val=1", 1, 1)
            .content(objectMapper.writeValueAsString(event))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            // event 
            .andExpect(jsonPath("$.conferenceFlag").value(0))
            .andExpect(jsonPath("$.date").value("2026-05-23T05:00:00"))
            .andExpect(jsonPath("$.deleted").value(0))
            .andExpect(jsonPath("$.estAttendance").value(15))
            .andExpect(jsonPath("$.feeFlag").value(1))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.location").value("Floyd"))
            .andExpect(jsonPath("$.name").value("Testing Event"))
            .andExpect(jsonPath("$.philanthropyFlag").value(0));
    }
        // fee invalid id test
        // fee failure test



    

    
    //philFlagEventTest{
        // invididual tests
    

    
    //confFlagEventTest{
        // invididual tests
    

    
    //safeDeleteEventTest{
        // invididual tests
    
    //deleteEventTest{
        // invididual tests
    
    //createEventAllocationFormTest{
        // individual tests
    

    
    //createConferenceAllocationFormTest{
        // individual tests
    

}


// Needs integrated testing and standard testing (add integration testing later)

//@SpringBootTest
class EventControllerTestInt {
    // standard testing
    
    //filterEventsTest{
        // invididual tests
    

    
    //getAllEventsTest{
        // invididual tests
    

    
    //createEventTest{
        // invididual tests
    

    
    //editEventTest{
        // invididual tests
    

    
    //feeFlagEventTest{
        // invididual tests
    

    
    //philFlagEventTest{
        // invididual tests
    

    
    //confFlagEventTest{
        // invididual tests
    

    
    //deleteEventTest{
        // invididual tests
    

    
    //createEventAllocationFormTest{
        // individual tests
    

    
    //createConferenceAllocationFormTest{
        // individual tests
    

}