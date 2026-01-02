package com.example.dynamicformpdf.service;

import com.example.dynamicformpdf.entity.FormEntity;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generate(FormEntity form) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, out);

        document.open();

        // Fonts
        Font headingFont = new Font(Font.HELVETICA, 15, Font.BOLD);
        Font labelFont = new Font(Font.HELVETICA, 11, Font.BOLD);
        Font bodyFont = new Font(Font.HELVETICA, 11);
        Font footerFont = new Font(Font.HELVETICA, 10, Font.ITALIC);

        // Title
        Paragraph title = new Paragraph("OFFICIAL APPLICATION LETTER\n\n", headingFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // From Section
        document.add(new Paragraph("From,", labelFont));
        document.add(new Paragraph(form.getName(), bodyFont));
        document.add(new Paragraph(form.getAddress(), bodyFont));
        document.add(new Paragraph(
                form.getCity() + ", " + form.getState() + " - " + form.getPinCode(), bodyFont));
        document.add(new Paragraph("Email: " + form.getEmail(), bodyFont));
        document.add(new Paragraph("Contact: " + form.getContactNumber(), bodyFont));
        document.add(Chunk.NEWLINE);

        // To Section
        document.add(new Paragraph("To,", labelFont));
        document.add(new Paragraph("The Concerned Authority", bodyFont));
        document.add(Chunk.NEWLINE);

        // Date & Subject
        document.add(new Paragraph("Date: " + form.getDate(), bodyFont));
        document.add(Chunk.NEWLINE);

        Paragraph subject = new Paragraph("Subject: " + form.getSubject(), labelFont);
        document.add(subject);
        document.add(Chunk.NEWLINE);

        // Salutation
        document.add(new Paragraph("Respected Sir/Madam,", bodyFont));
        document.add(Chunk.NEWLINE);

        // Body
        Paragraph message = new Paragraph(form.getMessage(), bodyFont);
        message.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(message);
        document.add(Chunk.NEWLINE);

        // Declaration
        Paragraph declaration = new Paragraph(
                "I hereby declare that the information furnished above is true and correct "
                        + "to the best of my knowledge and belief.", bodyFont);
        declaration.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(declaration);
        document.add(Chunk.NEWLINE);

        // Closing
        document.add(new Paragraph("Thanking you,", bodyFont));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Yours sincerely,", bodyFont));
        document.add(new Paragraph(form.getName(), labelFont));

        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("__________________________", footerFont));
        document.add(new Paragraph("Signature", footerFont));

        document.close();
        return out.toByteArray();
    }
}
