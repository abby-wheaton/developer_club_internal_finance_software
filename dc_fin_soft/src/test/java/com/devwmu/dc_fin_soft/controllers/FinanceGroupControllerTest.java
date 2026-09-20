package com.devwmu.dc_fin_soft.controllers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devwmu.dc_fin_soft.entities.FinanceGroup;


@ExtendWith(SpringExtension.class)
@WebMvcTest(FinanceGroup.class)
@AutoConfigureJsonTesters
public class FinanceGroupControllerTest {
    // web level testing - tests that path goes to correct function
    @Nested
    class getAllFinanceGroupsTest{
        // individual tests
    }

    @Nested
    class filterFinanceGroupsTest{
        // individual tests
    }

    @Nested
    class addUserToGroupTest{
        // individual tests
    }

    @Nested
    class removeUserFromGroupTest{
        // individual tests
    }

    @Nested
    class CreateGroupTest{
        // individual tests
    }

    @Nested
    class removeGroupTest{
        // individual tests
    }
}


// Needs integrated testing and standard testing (add integration testing later)
//@SpringBootTest
class FinanceGroupControllerTestInt {
    // web level testing - tests that path goes to correct function
    @Nested
    class getAllFinanceGroupsTest{
        // individual tests
    }

    @Nested
    class filterFinanceGroupsTest{
        // individual tests
    }

    @Nested
    class addUserToGroupTest{
        // individual tests
    }

    @Nested
    class removeUserFromGroupTest{
        // individual tests
    }

    @Nested
    class CreateGroupTest{
        // individual tests
    }

    @Nested
    class removeGroupTest{
        // individual tests
    }
}