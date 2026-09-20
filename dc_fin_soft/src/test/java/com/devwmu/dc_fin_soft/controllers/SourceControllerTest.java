package com.devwmu.dc_fin_soft.controllers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devwmu.dc_fin_soft.entities.Source;


@ExtendWith(SpringExtension.class)
@WebMvcTest(Source.class)
@AutoConfigureRestTestClient
public class SourceControllerTest {
    // web level testing - tests that path goes to correct function
    @Nested
    class getAllSourcesTest{
        // individual tests
    }

    @Nested
    class filterSourcesTest{
        // individual tests
    }

    @Nested
    class createSourceTest{
        // individual tests
    }

    @Nested
    class editSourceTest{
        // individual tests
    }

    @Nested
    class deleteSourceTest{
        // individual tests
    }

}


// Needs integrated testing and standard testing (add integration testing later)
//@SpringBootTest
class SourceControllerTestInt {
    // web level testing - tests that path goes to correct function
    @Nested
    class getAllSourcesTest{
        // individual tests
    }

    @Nested
    class filterSourcesTest{
        // individual tests
    }

    @Nested
    class createSourceTest{
        // individual tests
    }

    @Nested
    class editSourceTest{
        // individual tests
    }

    @Nested
    class deleteSourceTest{
        // individual tests
    }

}