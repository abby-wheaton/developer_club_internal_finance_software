package com.devwmu.dc_fin_soft.controllers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devwmu.dc_fin_soft.entities.Request;


@ExtendWith(SpringExtension.class)
@WebMvcTest(Request.class)
public class RequestControllerTest {
    // web level testing - tests that path goes to correct function
    
    @Nested
    class getAllRequestsTest{
        // individual tests
    }

    @Nested
    class filterRequestsTest{
        // individual tests
    }

    @Nested
    class createRequestTest{
        // individual tests
    }

    @Nested
    class editRequestTest{
        // individual tests
    }

    @Nested
    class deleteRequestTest{
        // individual tests
    }

    @Nested
    class approveRequestTest{
        // individual tests
    }

    @Nested
    class newRequestNotifyTest{
        // individual tests
    }

    @Nested
    class requestStatusUpdatedNotifyTest{
        // individual tests
    }

}


// Needs integrated testing and standard testing (add integration testing later)
//@SpringBootTest
class RequestControllerTestInt {
    // web level testing - tests that path goes to correct function
    
    @Nested
    class getAllRequestsTest{
        // individual test
    }

    @Nested
    class filterRequestsTest{
        // individual tests
    }

    @Nested
    class createRequestTest{
        // individual tests
    }

    @Nested
    class editRequestTest{
        // individual tests
    }

    @Nested
    class deleteRequestTest{
        // individual tests
    }

    @Nested
    class approveRequestTest{
        // individual tests
    }

    @Nested
    class newRequestNotifyTest{
        // individual tests
    }

    @Nested
    class requestStatusUpdatedNotifyTest{
        // individual tests
    }

}