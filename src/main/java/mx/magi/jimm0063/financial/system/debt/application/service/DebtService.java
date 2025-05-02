package mx.magi.jimm0063.financial.system.debt.application.service;

import mx.magi.jimm0063.financial.system.debt.domain.dto.DebtModel;

import java.util.List;

public interface DebtService {
    DebtModel deleteDebt(String debtId);
    List<DebtModel> cleanCardDebt(String cardCode);
}
