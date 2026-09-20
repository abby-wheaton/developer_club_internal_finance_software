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
public class NotifControllerTest {
    // web level testing - tests that path goes to correct function
    @Nested
    class checkAllNotificationsTest{
        // individual tests
    }

    @Nested
    class checkAllAlertsTest{
        // individual tests
    }


}


//@SpringBootTest
class NotifControllerTestInt {
    // integrated testing
    @Nested
    class checkAllNotificationsTest{
        // individual tests
	    
    }

    @Nested
    class checkAllAlertsTest{
        // individual tests
    }
}

