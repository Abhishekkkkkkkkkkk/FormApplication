package com.example.dynamicformpdf.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Schema(description = "Full name of the applicant", example = "John Doe")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a well-formed email address")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be exactly 10 digits")
    @Schema(description = "10-digit mobile contact number", example = "9876543210")
    private String contactNumber;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    @Schema(description = "Street address", example = "123 Banking Street")
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must not exceed 50 characters")
    @Schema(description = "City", example = "Mumbai")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 50, message = "State must not exceed 50 characters")
    @Schema(description = "State", example = "Maharashtra")
    private String state;

    @NotBlank(message = "PIN code is required")
    @Pattern(regexp = "^[0-9]{6}$", message = "PIN code must be exactly 6 digits")
    @Schema(description = "6-digit postal PIN code", example = "400001")
    private String pinCode;

    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Application date in YYYY-MM-DD format", example = "2026-10-19")
    private LocalDate date;

    @NotBlank(message = "Subject is required")
    @Size(max = 200, message = "Subject must not exceed 200 characters")
    @Schema(description = "Subject of the application", example = "Application for Personal Loan Verification")
    private String subject;

    @NotBlank(message = "Message is required")
    @Size(min = 10, max = 5000, message = "Message must be between 10 and 5000 characters")
    @Schema(description = "Detailed statement or message body", example = "I hereby submit my official application for verification.")
    private String message;
}
