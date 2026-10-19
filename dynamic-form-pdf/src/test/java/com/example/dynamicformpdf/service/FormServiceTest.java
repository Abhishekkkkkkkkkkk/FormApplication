package com.example.dynamicformpdf.service;

import com.example.dynamicformpdf.dto.FormRequestDTO;
import com.example.dynamicformpdf.dto.FormResponseDTO;
import com.example.dynamicformpdf.entity.FormEntity;
import com.example.dynamicformpdf.exception.ResourceNotFoundException;
import com.example.dynamicformpdf.repository.FormRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormServiceTest {

    @Mock
    private FormRepository repository;

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private FormService service;

    private FormEntity sampleEntity;
    private FormRequestDTO sampleRequest;

    @BeforeEach
    void setUp() {
        sampleEntity = FormEntity.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .contactNumber("9876543210")
                .address("123 Financial Way")
                .city("Mumbai")
                .state("Maharashtra")
                .pinCode("400001")
                .date(LocalDate.of(2026, 10, 19))
                .subject("Home Loan Application")
                .message("Please process my official home loan application.")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        sampleRequest = FormRequestDTO.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .contactNumber("9876543210")
                .address("123 Financial Way")
                .city("Mumbai")
                .state("Maharashtra")
                .pinCode("400001")
                .date(LocalDate.of(2026, 10, 19))
                .subject("Home Loan Application")
                .message("Please process my official home loan application.")
                .build();
    }

    @Test
    @DisplayName("Should successfully persist form and return FormResponseDTO")
    void shouldSaveFormSuccessfully() {
        when(repository.save(any(FormEntity.class))).thenReturn(sampleEntity);

        FormResponseDTO response = service.save(sampleRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("John Doe");
        assertThat(response.getEmail()).isEqualTo("john.doe@example.com");
        verify(repository, times(1)).save(any(FormEntity.class));
    }

    @Test
    @DisplayName("Should retrieve form by ID when found")
    void shouldGetFormById() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleEntity));

        FormResponseDTO response = service.getById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getSubject()).isEqualTo("Home Loan Application");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when form ID does not exist")
    void shouldThrowExceptionWhenFormNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Application Form not found with id: '99'");
    }

    @Test
    @DisplayName("Should return all forms as a list")
    void shouldReturnAllFormsList() {
        when(repository.findAll()).thenReturn(List.of(sampleEntity));

        List<FormResponseDTO> result = service.getAllList();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("Should return paged forms")
    void shouldReturnPagedForms() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<FormEntity> page = new PageImpl<>(List.of(sampleEntity), pageable, 1);
        when(repository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(page);

        Page<FormResponseDTO> result = service.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should delegate PDF generation when form exists")
    void shouldGeneratePdfWhenFormExists() {
        byte[] mockPdf = new byte[]{1, 2, 3};
        when(repository.findById(1L)).thenReturn(Optional.of(sampleEntity));
        when(pdfService.generate(sampleEntity)).thenReturn(mockPdf);

        byte[] result = service.generatePdf(1L);

        assertThat(result).isEqualTo(mockPdf);
        verify(pdfService, times(1)).generate(sampleEntity);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException during PDF generation if ID missing")
    void shouldThrowExceptionWhenGeneratingPdfForMissingId() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.generatePdf(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(pdfService, never()).generate(any());
    }
}
