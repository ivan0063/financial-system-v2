package mx.magi.jimm0063.financial.system.debt.application.service.impl;

import mx.magi.jimm0063.financial.system.debt.application.service.AccountStatementService;
import mx.magi.jimm0063.financial.system.debt.domain.dto.DebtModel;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("RAPPI")
public class RappiAccountStatementServiceImpl implements AccountStatementService {
    @Override
    public List<DebtModel> extractDebt(byte[] pdfFile) throws IOException {
        List<DebtModel> debtModels = new ArrayList<>();

        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(pdfFile))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            String[] lines = text.split("\n");
            boolean isInstallmentSection = false;

            // Pattern for "Compras a meses"
            Pattern installmentPattern = Pattern.compile(
                    "^(\\d{4}-\\d{2}-\\d{2})\\s+(.+?)\\s+\\$\\s*([\\d,.]+)\\s+\\$\\s*([\\d,.]+)\\s+\\$\\s*([\\d,.]+)\\s+(\\d+)\\s+de\\s+(\\d+)\\s+\\$\\s*([\\d,.]+)$"
            );

            for (String line : lines) {
                line = line.trim();

                if (line.equalsIgnoreCase("Compras a meses")) {
                    isInstallmentSection = true;
                    continue;
                }

                if (isInstallmentSection) {
                    if (line.isEmpty() || line.toLowerCase().startsWith("subtotal")) {
                        break; // End of section
                    }

                    Matcher matcher = installmentPattern.matcher(line);
                    if (matcher.matches()) {
                        debtModels.add(DebtModel.builder()
                                .operationDate(matcher.group(1).trim())
                                .name(matcher.group(2).trim())
                                .initialDebtAmount(Double.parseDouble(matcher.group(3).replace(",", "")))
                                .debtPaid(Double.parseDouble(matcher.group(4).replace(",", "")))
                                .monthsPaid(Integer.parseInt(matcher.group(6)))
                                .monthsFinanced(Integer.parseInt(matcher.group(7)))
                                .monthAmount(Double.parseDouble(matcher.group(8).replace(",", "")))
                                .build());
                    }
                }
            }
        }

        return debtModels;
    }
}