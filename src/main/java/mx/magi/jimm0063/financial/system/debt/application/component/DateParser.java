package mx.magi.jimm0063.financial.system.debt.application.component;

import mx.magi.jimm0063.financial.system.debt.domain.enums.PdfExtractorTypes;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

@Component
public class DateParser {
    public LocalDate parse(String date, PdfExtractorTypes pdfExtractorTypes) {
        LocalDate parsedDate = null;

        if (Objects.nonNull(date) && (pdfExtractorTypes.equals(PdfExtractorTypes.UNIVERSAL) || pdfExtractorTypes.equals(PdfExtractorTypes.MANUAL))) {
            String correctedDate = date.replaceAll("(?<=\\d{2}-\\p{Alpha}{3})-", ".-");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", new Locale("es", "MX"));
            parsedDate = LocalDate.parse(correctedDate, formatter);
        }

        if (Objects.nonNull(date) && pdfExtractorTypes.equals(PdfExtractorTypes.RAPPI))
            parsedDate = LocalDate.parse(date);

        return parsedDate;
    }
}
