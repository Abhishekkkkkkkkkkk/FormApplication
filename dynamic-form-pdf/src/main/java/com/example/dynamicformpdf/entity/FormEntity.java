package com.example.dynamicformpdf.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "form_data")
@Getter
@Setter
public class FormEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Column(name = "phone", nullable = false)
    private String contactNumber;

    private String address;

    private String city;

    private String state;

    @Column(name = "pin_code")
    private String pinCode;

    private LocalDate date;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
