package com.example.dynamicformpdf.service;

import com.example.dynamicformpdf.dto.FormRequestDTO;
import com.example.dynamicformpdf.dto.FormResponseDTO;
import com.example.dynamicformpdf.entity.FormEntity;
import com.example.dynamicformpdf.exception.ResourceNotFoundException;
import com.example.dynamicformpdf.repository.FormRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository repository;
    private final PdfService pdfService;

    @Transactional
    public FormResponseDTO save(FormRequestDTO dto) {
        log.info("Processing application submission for email: {}", dto.getEmail());

        FormEntity entity = FormEntity.builder()
                .name(dto.getName().trim())
                .email(dto.getEmail().trim().toLowerCase())
                .contactNumber(dto.getContactNumber().trim())
                .address(dto.getAddress().trim())
                .city(dto.getCity().trim())
                .state(dto.getState().trim())
                .pinCode(dto.getPinCode().trim())
                .date(dto.getDate())
                .subject(dto.getSubject().trim())
                .message(dto.getMessage().trim())
                .build();

        FormEntity saved = repository.save(entity);
        log.info("Application successfully persisted with ID: {}", saved.getId());

        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<FormResponseDTO> getAll(Pageable pageable) {
        return repository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public List<FormResponseDTO> getAllList() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FormResponseDTO getById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Application Form", "id", id));
    }

    @Transactional(readOnly = true)
    public byte[] generatePdf(Long id) {
        log.info("Generating PDF for form ID: {}", id);
        FormEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application Form", "id", id));

        return pdfService.generate(entity);
    }

    public FormResponseDTO mapToDTO(FormEntity entity) {
        if (entity == null) {
            return null;
        }
        return FormResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .contactNumber(entity.getContactNumber())
                .address(entity.getAddress())
                .city(entity.getCity())
                .state(entity.getState())
                .pinCode(entity.getPinCode())
                .date(entity.getDate())
                .subject(entity.getSubject())
                .message(entity.getMessage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
