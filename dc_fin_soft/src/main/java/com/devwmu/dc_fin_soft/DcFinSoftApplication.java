package com.devwmu.dc_fin_soft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "WMU Developer Club's Financial API",
        version = "v1.0",
        description = "An API to interact with the backend of WMU Developer Club's database to manage finances and budgeting.",
        contact = @Contact(name = "API Support Technician", email = "bluewheatthin@gmail.com")
    )
)
public class DcFinSoftApplication {

    public static void main(String[] args) {
        SpringApplication.run(DcFinSoftApplication.class, args);
    }

}


