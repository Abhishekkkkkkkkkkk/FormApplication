package com.example.dynamicformpdf.repository;

import com.example.dynamicformpdf.entity.FormEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class FormRepositoryTest {

    @Autowired
    private FormRepository repository;

    @Test
    @DisplayName("Should persist and retrieve FormEntity with generated ID and timestamps")
    void shouldPersistAndRetrieveEntity() {
        FormEntity entity = FormEntity.builder()
                .name("Robert Brown")
                .email("robert.brown@example.com")
                .contactNumber("9876543210")
                .address("101 Banking Plaza")
                .city("Pune")
                .state("Maharashtra")
                .pinCode("411001")
                .date(LocalDate.of(2026, 10, 19))
                .subject("Business Loan Application")
                .message("Requesting term loan assessment for business expansion.")
                .build();

        FormEntity saved = repository.save(entity);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();

        FormEntity retrieved = repository.findById(saved.getId()).orElse(null);
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getName()).isEqualTo("Robert Brown");
        assertThat(retrieved.getContactNumber()).isEqualTo("9876543210");
    }

    @Test
    @DisplayName("Should find entries by email ordered by created date descending")
    void shouldFindByEmailOrdered() {
        FormEntity entity1 = FormEntity.builder()
                .name("User One")
                .email("duplicate@example.com")
                .contactNumber("9876543210")
                .address("Addr 1")
                .city("City")
                .state("State")
                .pinCode("123456")
                .date(LocalDate.now())
                .subject("First Subject")
                .message("First application message.")
                .build();

        FormEntity entity2 = FormEntity.builder()
                .name("User One")
                .email("duplicate@example.com")
                .contactNumber("9876543210")
                .address("Addr 2")
                .city("City")
                .state("State")
                .pinCode("123456")
                .date(LocalDate.now())
                .subject("Second Subject")
                .message("Second application message.")
                .build();

        repository.save(entity1);
        repository.save(entity2);

        List<FormEntity> results = repository.findByEmailOrderByCreatedAtDesc("duplicate@example.com");
        assertThat(results).hasSize(2);
    }

    @Test
    @DisplayName("Should check existence by email, subject, and date")
    void shouldCheckDuplicateSubmission() {
        LocalDate date = LocalDate.of(2026, 10, 19);
        FormEntity entity = FormEntity.builder()
                .name("Unique Applicant")
                .email("unique@example.com")
                .contactNumber("9876543210")
                .address("Addr")
                .city("City")
                .state("State")
                .pinCode("123456")
                .date(date)
                .subject("Unique Subject")
                .message("Unique message details.")
                .build();

        repository.save(entity);

        boolean exists = repository.existsByEmailAndSubjectAndDate("unique@example.com", "Unique Subject", date);
        assertThat(exists).isTrue();

        boolean nonExistent = repository.existsByEmailAndSubjectAndDate("unique@example.com", "Other Subject", date);
        assertThat(nonExistent).isFalse();
    }

    @Test
    @DisplayName("Should return paged entities ordered by createdAt desc")
    void shouldFindAllPaged() {
        for (int i = 0; i < 5; i++) {
            repository.save(FormEntity.builder()
                    .name("User " + i)
                    .email("user" + i + "@example.com")
                    .contactNumber("9876543210")
                    .address("Addr " + i)
                    .city("City")
                    .state("State")
                    .pinCode("123456")
                    .date(LocalDate.now())
                    .subject("Subject " + i)
                    .message("Application message for user " + i)
                    .build());
        }

        Page<FormEntity> page = repository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 3));
        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(5);
    }
}
