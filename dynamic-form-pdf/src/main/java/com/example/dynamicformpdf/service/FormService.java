package com.example.dynamicformpdf.service;

import com.example.dynamicformpdf.dto.FormRequestDTO;
import com.example.dynamicformpdf.entity.FormEntity;
import com.example.dynamicformpdf.repository.FormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository repository;
    private final PdfService pdfService;

    public FormEntity save(FormRequestDTO dto) {

        try {
            FormEntity entity = new FormEntity();

            entity.setName(dto.getName());
            entity.setEmail(dto.getEmail());
            entity.setContactNumber(dto.getContactNumber());
            entity.setAddress(dto.getAddress());
            entity.setCity(dto.getCity());
            entity.setState(dto.getState());
            entity.setPinCode(dto.getPinCode());
            entity.setDate(dto.getDate());
            entity.setSubject(dto.getSubject());
            entity.setMessage(dto.getMessage());

            return repository.save(entity);

        } catch (Exception e) {
            e.printStackTrace(); // shows real cause in console
            throw e;
        }
    }

    public List<FormEntity> getAll() {
        return repository.findAll();
    }

    public byte[] generatePdf(Long id) {
        FormEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Form not found"));
        return pdfService.generate(entity);
    }
}
