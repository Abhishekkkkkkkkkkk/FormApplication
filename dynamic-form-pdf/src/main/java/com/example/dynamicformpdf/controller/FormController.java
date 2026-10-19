package com.example.dynamicformpdf.controller;

import com.example.dynamicformpdf.dto.FormRequestDTO;
import com.example.dynamicformpdf.dto.FormResponseDTO;
import com.example.dynamicformpdf.service.FormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Loan Form & Application Management", description = "Endpoints for submitting, managing, and generating documents for loan applications")
public class FormController {

    private final FormService service;

    @GetMapping("/")
    @Operation(summary = "Root redirect to Swagger UI", hidden = true)
    public ResponseEntity<Void> rootRedirect() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/swagger-ui.html"))
                .build();
    }

    @PostMapping({"/api/forms", "/api/form/submit"})
    @Operation(summary = "Submit a new application form", description = "Validates and persists applicant data, returning the created application record.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Application submitted successfully",
                    content = @Content(schema = @Schema(implementation = FormResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload or field validation failed")
    })
    public ResponseEntity<FormResponseDTO> submit(
            @Valid @RequestBody FormRequestDTO dto) {

        log.info("Received application submission for: {}", dto.getEmail());
        FormResponseDTO response = service.save(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/forms/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/api/forms")
    @Operation(summary = "Retrieve submitted applications", description = "Returns submitted applications. Supports optional pagination parameters (page, size).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List or page of applications successfully retrieved")
    })
    public ResponseEntity<?> getAll(
            @Parameter(description = "Zero-based page index")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Size of the page to return")
            @RequestParam(required = false) Integer size) {

        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(Math.max(0, page), Math.min(100, Math.max(1, size)));
            Page<FormResponseDTO> pagedResult = service.getAll(pageable);
            return ResponseEntity.ok(pagedResult);
        }

        List<FormResponseDTO> allList = service.getAllList();
        return ResponseEntity.ok(allList);
    }

    @GetMapping("/api/forms/{id}")
    @Operation(summary = "Get application by ID", description = "Retrieves details of a specific loan application by its primary ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application found"),
            @ApiResponse(responseCode = "404", description = "Application not found with given ID")
    })
    public ResponseEntity<FormResponseDTO> getById(
            @Parameter(description = "Primary ID of the application")
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/api/forms/pdf/preview/{id}")
    @Operation(summary = "Preview application PDF document", description = "Generates and streams the official PDF document inline for in-browser preview.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "PDF stream delivered", content = @Content(mediaType = "application/pdf")),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    public ResponseEntity<byte[]> previewPdf(
            @Parameter(description = "Application ID")
            @PathVariable Long id) {

        byte[] pdf = service.generatePdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"Application_Letter_" + id + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping({"/api/forms/pdf/download/{id}", "/api/forms/{id}/pdf", "/api/form/pdf"})
    @Operation(summary = "Download official application PDF", description = "Generates and returns the official application PDF as an attachment download.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "PDF file attachment delivered", content = @Content(mediaType = "application/pdf")),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    public ResponseEntity<byte[]> downloadPdf(
            @Parameter(description = "Application ID (path variable or query parameter)")
            @PathVariable(required = false) Long id,
            @RequestParam(name = "id", required = false) Long queryId) {

        Long targetId = id != null ? id : queryId;
        if (targetId == null) {
            return ResponseEntity.badRequest().build();
        }

        byte[] pdf = service.generatePdf(targetId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"Application_Letter_" + targetId + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
