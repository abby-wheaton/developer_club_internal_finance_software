package com.devwmu.dc_fin_soft.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.doReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devwmu.dc_fin_soft.entities.Event;
import com.devwmu.dc_fin_soft.repositories.EventRepository;
import com.devwmu.dc_fin_soft.repositories.ExpenseRepository;


@ExtendWith(SpringExtension.class)
@WebMvcTest(EventController.class)
@AutoConfigureJsonTesters
public class EventControllerTest {
    // web level testing - tests that path goes to correct function, HTTP codes, json serialization, and inputs
    @Autowired 
    private MockMvc mockMvc;

    @MockitoBean 
    private EventRepository eventRepository;

    @MockitoBean 
    private ExpenseRepository expenseRepository;

    @Nested
    class filterEventsTest{
        // invididual tests


    }

    @Nested
    class getAllEventsTest{
        // invididual tests
        @Test
        void allEventsSuccessTest() throws Exception{
            LocalDateTime datetime = LocalDateTime.of(2026, 5, 23, 5, 0, 0);
            Event event1 = new Event(0, datetime, 0, 15, 0, 1, "Floyd", "Testing Event", 0);

            Event event2 = new Event(1, datetime, 0, 25, 0, 2, "Rood Hall", "Testing Event 2", 0);

            List<Event> mockEvents = new ArrayList<>();
            mockEvents.add(event1);
            mockEvents.add(event2);

            // mock the return from find all call
            doReturn(mockEvents).when(eventRepository).findAll();

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
    }

    @Nested
    class createEventTest{
        // invididual tests
    }

    @Nested
    class editEventTest{
        // invididual tests
    }

    @Nested
    class feeFlagEventTest{
        // invididual tests
    }

    @Nested
    class philFlagEventTest{
        // invididual tests
    }

    @Nested
    class confFlagEventTest{
        // invididual tests
    }

    @Nested
    class deleteEventTest{
        // invididual tests
    }

    @Nested
    class createEventAllocationFormTest{
        // individual tests
    }

    @Nested
    class createConferenceAllocationFormTest{
        // individual tests
    }

}


// Needs integrated testing and standard testing (add integration testing later)

//@SpringBootTest
class EventControllerTestInt {
    // standard testing
    @Nested
    class filterEventsTest{
        // invididual tests
    }

    @Nested
    class getAllEventsTest{
        // invididual tests
    }

    @Nested
    class createEventTest{
        // invididual tests
    }

    @Nested
    class editEventTest{
        // invididual tests
    }

    @Nested
    class feeFlagEventTest{
        // invididual tests
    }

    @Nested
    class philFlagEventTest{
        // invididual tests
    }

    @Nested
    class confFlagEventTest{
        // invididual tests
    }

    @Nested
    class deleteEventTest{
        // invididual tests
    }

    @Nested
    class createEventAllocationFormTest{
        // individual tests
    }

    @Nested
    class createConferenceAllocationFormTest{
        // individual tests
    }

}