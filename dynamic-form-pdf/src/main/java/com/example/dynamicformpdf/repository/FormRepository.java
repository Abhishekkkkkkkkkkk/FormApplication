package com.example.dynamicformpdf.repository;

import com.example.dynamicformpdf.entity.FormEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormRepository extends JpaRepository<FormEntity, Long> {
}
