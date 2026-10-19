package com.example.dynamicformpdf.controller;

import com.example.dynamicformpdf.dto.FormRequestDTO;
import com.example.dynamicformpdf.dto.FormResponseDTO;
import com.example.dynamicformpdf.exception.GlobalExceptionHandler;
import com.example.dynamicformpdf.exception.ResourceNotFoundException;
import com.example.dynamicformpdf.service.FormService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FormController.class)
@Import(GlobalExceptionHandler.class)
class FormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FormService service;

    private FormRequestDTO validRequest;
    private FormResponseDTO mockResponse;

    @BeforeEach
    void setUp() {
        validRequest = FormRequestDTO.builder()
                .name("Alice Wonder")
                .email("alice@banking.example.com")
                .contactNumber("9876543210")
                .address("789 Commercial Road")
                .city("Chennai")
                .state("Tamil Nadu")
                .pinCode("600001")
                .date(LocalDate.of(2026, 10, 19))
                .subject("Home Mortgage Application")
                .message("This is a comprehensive request for home mortgage pre-approval.")
                .build();

        mockResponse = FormResponseDTO.builder()
                .id(101L)
                .name("Alice Wonder")
                .email("alice@banking.example.com")
                .contactNumber("9876543210")
                .address("789 Commercial Road")
                .city("Chennai")
                .state("Tamil Nadu")
                .pinCode("600001")
                .date(LocalDate.of(2026, 10, 19))
                .subject("Home Mortgage Application")
                .message("This is a comprehensive request for home mortgage pre-approval.")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("POST /api/forms with valid payload should return 201 Created and FormResponseDTO")
    void shouldSubmitValidForm() throws Exception {
        when(service.save(any(FormRequestDTO.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/forms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/forms/101")))
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.name").value("Alice Wonder"))
                .andExpect(jsonPath("$.email").value("alice@banking.example.com"));
    }

    @Test
    @DisplayName("POST /api/form/submit (alias) should also accept submissions and return 201 Created")
    void shouldSupportSubmitAlias() throws Exception {
        when(service.save(any(FormRequestDTO.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/form/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(101));
    }

    @Test
    @DisplayName("POST /api/forms with invalid email, phone, PIN, and short message returns 400 Bad Request with field errors")
    void shouldRejectInvalidFields() throws Exception {
        FormRequestDTO invalidRequest = FormRequestDTO.builder()
                .name("")
                .email("not-an-email")
                .contactNumber("123") // Not 10 digits
                .address("")
                .city("")
                .state("")
                .pinCode("12") // Not 6 digits
                .date(null)
                .subject("")
                .message("too short") // Under 10 characters
                .build();

        mockMvc.perform(post("/api/forms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors.email").exists())
                .andExpect(jsonPath("$.validationErrors.contactNumber").exists())
                .andExpect(jsonPath("$.validationErrors.pinCode").exists())
                .andExpect(jsonPath("$.validationErrors.name").exists())
                .andExpect(jsonPath("$.validationErrors.date").exists());
    }

    @Test
    @DisplayName("POST /api/forms with malformed JSON body returns 400 Bad Request")
    void shouldHandleMalformedJson() throws Exception {
        mockMvc.perform(post("/api/forms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{malformed-json-payload"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(containsString("Malformed JSON request payload")));
    }

    @Test
    @DisplayName("GET /api/forms returns list of all submitted forms")
    void shouldGetAllFormsList() throws Exception {
        when(service.getAllList()).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/forms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Alice Wonder"));
    }

    @Test
    @DisplayName("GET /api/forms with page and size parameters returns paged response")
    void shouldGetPagedForms() throws Exception {
        when(service.getAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(mockResponse)));

        mockMvc.perform(get("/api/forms?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(101));
    }

    @Test
    @DisplayName("GET /api/forms/{id} returns application when found")
    void shouldGetFormById() throws Exception {
        when(service.getById(101L)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/forms/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.subject").value("Home Mortgage Application"));
    }

    @Test
    @DisplayName("GET /api/forms/{id} returns 404 Not Found when ID does not exist")
    void shouldReturn404WhenNotFound() throws Exception {
        when(service.getById(999L)).thenThrow(new ResourceNotFoundException("Application Form", "id", 999L));

        mockMvc.perform(get("/api/forms/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(containsString("Application Form not found with id: '999'")));
    }

    @Test
    @DisplayName("GET /api/forms/pdf/preview/{id} returns PDF stream with inline content disposition")
    void shouldPreviewPdf() throws Exception {
        byte[] pdfContent = "%PDF-1.7 mock preview bytes".getBytes();
        when(service.generatePdf(101L)).thenReturn(pdfContent);

        mockMvc.perform(get("/api/forms/pdf/preview/101"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "inline; filename=\"Application_Letter_101.pdf\""))
                .andExpect(content().bytes(pdfContent));
    }

    @Test
    @DisplayName("GET /api/forms/pdf/download/{id} returns PDF attachment")
    void shouldDownloadPdf() throws Exception {
        byte[] pdfContent = "%PDF-1.7 mock download bytes".getBytes();
        when(service.generatePdf(101L)).thenReturn(pdfContent);

        mockMvc.perform(get("/api/forms/pdf/download/101"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"Application_Letter_101.pdf\""))
                .andExpect(content().bytes(pdfContent));
    }

    @Test
    @DisplayName("GET /api/form/pdf?id=101 backward compatible endpoint works")
    void shouldSupportDownloadAliasWithQueryParam() throws Exception {
        byte[] pdfContent = "%PDF-1.7 mock download bytes".getBytes();
        when(service.generatePdf(101L)).thenReturn(pdfContent);

        mockMvc.perform(get("/api/form/pdf?id=101"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"Application_Letter_101.pdf\""));
    }
}
