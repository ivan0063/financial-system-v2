package mx.magi.jimm0063.financial.system.debt.application.service.impl;

import mx.magi.jimm0063.financial.system.debt.application.service.DebtService;
import mx.magi.jimm0063.financial.system.debt.domain.dto.DebtModel;
import mx.magi.jimm0063.financial.system.financial.catalog.domain.entity.Card;
import mx.magi.jimm0063.financial.system.financial.catalog.domain.entity.Debt;
import mx.magi.jimm0063.financial.system.financial.catalog.domain.repository.CardRepository;
import mx.magi.jimm0063.financial.system.financial.catalog.domain.repository.DebtRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultDebtService implements DebtService {
    private final DebtRepository debtRepository;
    private final CardRepository cardRepository;

    public DefaultDebtService(DebtRepository debtRepository, CardRepository cardRepository) {
        this.debtRepository = debtRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    public DebtModel deleteDebt(String debtId) {
        Debt debt = debtRepository.findById(debtId).orElseThrow(() -> new RuntimeException(String.format("Debt with id %s not found", debtId)));

        debtRepository.delete(debt);

        return DebtModel.builder()
                .debtId(debtId)
                .name(debt.getName())
                .monthsPaid(debt.getMonthsPaid())
                .monthsFinanced(debt.getMonthsFinanced())
                .monthAmount(debt.getMonthAmount())
                .initialDebtAmount(debt.getInitialDebtAmount())
                .debtPaid(debt.getDebtPaid())
                .build();
    }

    @Override
    public List<DebtModel> cleanCardDebt(String cardCode) {
        Card card = cardRepository.findById(cardCode)
                .orElseThrow(() -> new RuntimeException(String.format("Card with code %s not found", cardCode)));

        List<Debt> cardDebts = card.getDebts();

        debtRepository.deleteAllByIdInBatch(cardDebts.stream().map(Debt::getDebtId).toList());

        return cardDebts.stream()
                .map(debt -> DebtModel.builder()
                        .debtId(debt.getDebtId())
                        .name(debt.getName())
                        .monthsPaid(debt.getMonthsPaid())
                        .monthsFinanced(debt.getMonthsFinanced())
                        .monthAmount(debt.getMonthAmount())
                        .initialDebtAmount(debt.getInitialDebtAmount())
                        .debtPaid(debt.getDebtPaid())
                        .build())
                .toList();
    }
}
