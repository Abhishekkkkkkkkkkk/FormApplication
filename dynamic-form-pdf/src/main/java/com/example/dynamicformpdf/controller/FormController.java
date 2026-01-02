package com.example.dynamicformpdf.controller;

import com.example.dynamicformpdf.dto.FormRequestDTO;
import com.example.dynamicformpdf.entity.FormEntity;
import com.example.dynamicformpdf.service.FormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
public class FormController {

    private final FormService service;

    @PostMapping
    public ResponseEntity<FormEntity> submit(
            @Valid @RequestBody FormRequestDTO dto) {

        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping
    public List<FormEntity> getAll() {
        return service.getAll();
    }

    @GetMapping("/pdf/preview/{id}")
    public ResponseEntity<byte[]> previewPdf(@PathVariable Long id) {

        byte[] pdf = service.generatePdf(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/pdf/download/{id}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {

        byte[] pdf = service.generatePdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=Application_Letter.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
