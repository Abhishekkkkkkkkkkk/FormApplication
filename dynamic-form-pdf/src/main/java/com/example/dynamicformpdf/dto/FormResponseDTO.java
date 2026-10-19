package com.example.dynamicformpdf.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String contactNumber;
    private String address;
    private String city;
    private String state;
    private String pinCode;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String subject;
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
