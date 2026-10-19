package com.example.dynamicformpdf.service;

import com.example.dynamicformpdf.entity.FormEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PdfServiceTest {

    private PdfService pdfService;

    @BeforeEach
    void setUp() {
        pdfService = new PdfService();
    }

    @Test
    @DisplayName("Should generate valid non-empty PDF byte array containing %PDF- magic bytes")
    void shouldGenerateValidPdfDocument() {
        FormEntity form = FormEntity.builder()
                .id(1L)
                .name("Jane Smith")
                .email("jane.smith@bank.example.com")
                .contactNumber("9123456789")
                .address("456 Central Avenue")
                .city("Bangalore")
                .state("Karnataka")
                .pinCode("560001")
                .date(LocalDate.of(2026, 10, 19))
                .subject("Personal Loan Request")
                .message("This is a detailed and authentic loan application message submitted for processing.")
                .createdAt(LocalDateTime.now())
                .build();

        byte[] pdfBytes = pdfService.generate(form);

        assertThat(pdfBytes).isNotNull();
        assertThat(pdfBytes.length).isGreaterThan(500);

        // Verify standard PDF header magic number %PDF-
        String header = new String(pdfBytes, 0, 5, StandardCharsets.US_ASCII);
        assertThat(header).isEqualTo("%PDF-");
    }

    @Test
    @DisplayName("Should handle missing or null fields gracefully without NullPointerException")
    void shouldHandleNullFieldsGracefully() {
        FormEntity partialForm = FormEntity.builder()
                .id(2L)
                .name("Partial Applicant")
                .email(null)
                .contactNumber(null)
                .address(null)
                .city(null)
                .state(null)
                .pinCode(null)
                .date(null)
                .subject("Missing Fields Application")
                .message(null)
                .build();

        byte[] pdfBytes = pdfService.generate(partialForm);

        assertThat(pdfBytes).isNotNull();
        assertThat(pdfBytes.length).isGreaterThan(500);
        String header = new String(pdfBytes, 0, 5, StandardCharsets.US_ASCII);
        assertThat(header).isEqualTo("%PDF-");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when passing null entity")
    void shouldThrowExceptionOnNullForm() {
        assertThatThrownBy(() -> pdfService.generate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot generate PDF from null FormEntity");
    }
}
