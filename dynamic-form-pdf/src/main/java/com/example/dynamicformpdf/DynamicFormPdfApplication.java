package com.example.dynamicformpdf;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DynamicFormPdfApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicFormPdfApplication.class, args);
    }

    @Bean
    public OpenAPI bankingFormOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Banking Loan Form Management & Document Processing System API")
                        .description("REST API services for submitting loan application forms, validating customer details, managing applications, and generating dynamic official PDF documents.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Banking Technology Services")
                                .email("support@banking-forms.example.com"))
                        .license(new License().name("Proprietary - Banking Operations")));
    }
}
