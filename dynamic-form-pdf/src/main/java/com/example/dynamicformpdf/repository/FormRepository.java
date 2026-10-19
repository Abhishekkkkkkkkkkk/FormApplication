package com.example.dynamicformpdf.repository;

import com.example.dynamicformpdf.entity.FormEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FormRepository extends JpaRepository<FormEntity, Long> {

    List<FormEntity> findByEmailOrderByCreatedAtDesc(String email);

    Page<FormEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    boolean existsByEmailAndSubjectAndDate(String email, String subject, LocalDate date);
}
