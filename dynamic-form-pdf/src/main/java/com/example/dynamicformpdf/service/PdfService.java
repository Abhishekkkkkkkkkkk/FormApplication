package com.example.dynamicformpdf.service;

import com.example.dynamicformpdf.entity.FormEntity;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class PdfService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] generate(FormEntity form) {
        if (form == null) {
            throw new IllegalArgumentException("Cannot generate PDF from null FormEntity");
        }

        log.debug("Generating official PDF document for form ID: {}", form.getId());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            writer.setPdfVersion(PdfWriter.VERSION_1_7);

            // Document Metadata
            document.addTitle("Official Application Letter - " + safe(form.getName()));
            document.addSubject("Application Form / Letter: " + safe(form.getSubject()));
            document.addAuthor("Banking Loan Application & Document Processing System");
            document.addCreator("Dynamic Form PDF Service");

            document.open();

            // Professional Fonts
            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD, new Color(24, 43, 73));
            Font sectionHeaderFont = new Font(Font.HELVETICA, 11, Font.BOLD, new Color(40, 70, 120));
            Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.DARK_GRAY);
            Font bodyFont = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);
            Font footerFont = new Font(Font.HELVETICA, 9, Font.ITALIC, Color.GRAY);
            Font signatureFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK);

            // Header Banner / Title
            Paragraph title = new Paragraph("OFFICIAL APPLICATION LETTER\n", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(5f);
            document.add(title);

            // Decorative Horizontal Rule
            LineSeparator separator = new LineSeparator(1.5f, 100, new Color(40, 70, 120), Element.ALIGN_CENTER, -2);
            document.add(new Chunk(separator));
            document.add(new Paragraph("\n", bodyFont));

            // From Section
            Paragraph fromHeader = new Paragraph("FROM:", sectionHeaderFont);
            fromHeader.setSpacingAfter(4f);
            document.add(fromHeader);

            document.add(new Paragraph(safe(form.getName()), labelFont));
            document.add(new Paragraph(safe(form.getAddress()), bodyFont));
            document.add(new Paragraph(
                    safe(form.getCity()) + ", " + safe(form.getState()) + " - " + safe(form.getPinCode()),
                    bodyFont));
            document.add(new Paragraph("Email: " + safe(form.getEmail()), bodyFont));
            document.add(new Paragraph("Contact Number: " + safe(form.getContactNumber()), bodyFont));
            document.add(new Paragraph("\n", bodyFont));

            // To Section
            Paragraph toHeader = new Paragraph("TO:", sectionHeaderFont);
            toHeader.setSpacingAfter(4f);
            document.add(toHeader);
            document.add(new Paragraph("The Branch Manager / Concerned Authority", labelFont));
            document.add(new Paragraph("Loan & Applications Processing Department", bodyFont));
            document.add(new Paragraph("Authorized Financial Services", bodyFont));
            document.add(new Paragraph("\n", bodyFont));

            // Date & Subject Section
            String formattedDate = form.getDate() != null ? form.getDate().format(DATE_FORMATTER) : "N/A";
            Paragraph datePara = new Paragraph("Date: " + formattedDate, labelFont);
            datePara.setSpacingAfter(4f);
            document.add(datePara);

            Paragraph subjectPara = new Paragraph("Subject: " + safe(form.getSubject()), sectionHeaderFont);
            subjectPara.setSpacingAfter(10f);
            document.add(subjectPara);

            // Salutation
            Paragraph salutation = new Paragraph("Respected Sir / Madam,", bodyFont);
            salutation.setSpacingAfter(8f);
            document.add(salutation);

            // Message Body
            Paragraph message = new Paragraph(safe(form.getMessage()), bodyFont);
            message.setAlignment(Element.ALIGN_JUSTIFIED);
            message.setLeading(14f);
            message.setSpacingAfter(14f);
            document.add(message);

            // Formal Declaration
            Paragraph declaration = new Paragraph(
                    "Declaration: I hereby solemnly declare that all the information furnished in this application "
                            + "is true, complete, and correct to the best of my knowledge and belief. I understand that any "
                            + "misrepresentation or omission of facts may lead to the rejection of this application.",
                    footerFont);
            declaration.setAlignment(Element.ALIGN_JUSTIFIED);
            declaration.setLeading(12f);
            declaration.setSpacingAfter(16f);
            document.add(declaration);

            // Closing & Sign-off
            Paragraph closing = new Paragraph("Thanking you,\n\nYours sincerely,", bodyFont);
            closing.setSpacingAfter(25f);
            document.add(closing);

            // Signature block
            Paragraph signLine = new Paragraph("____________________________________", footerFont);
            Paragraph applicantName = new Paragraph("Applicant: " + safe(form.getName()), signatureFont);
            Paragraph contactInfo = new Paragraph("Mobile: " + safe(form.getContactNumber()) + " | Email: " + safe(form.getEmail()), footerFont);

            document.add(signLine);
            document.add(applicantName);
            document.add(contactInfo);

            // System Footer
            document.add(new Paragraph("\n", bodyFont));
            LineSeparator footSep = new LineSeparator(0.5f, 100, Color.LIGHT_GRAY, Element.ALIGN_CENTER, -2);
            document.add(new Chunk(footSep));
            Paragraph sysFooter = new Paragraph("Generated automatically by Banking Form Management & Document Processing System", footerFont);
            sysFooter.setAlignment(Element.ALIGN_CENTER);
            sysFooter.setSpacingBefore(4f);
            document.add(sysFooter);

        } catch (DocumentException e) {
            log.error("DocumentException during PDF generation for form ID {}: {}", form.getId(), e.getMessage());
            throw new RuntimeException("Failed to generate PDF document: " + e.getMessage(), e);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }

        return out.toByteArray();
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
