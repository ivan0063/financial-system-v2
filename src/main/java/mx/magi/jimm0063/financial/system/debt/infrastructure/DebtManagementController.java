package mx.magi.jimm0063.financial.system.debt.infrastructure;

import mx.magi.jimm0063.financial.system.debt.application.service.DebtService;
import mx.magi.jimm0063.financial.system.debt.domain.dto.DebtModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/debt")
public class DebtManagementController {
    private final DebtService debtService;

    public DebtManagementController(DebtService debtService) {
        this.debtService = debtService;
    }

    @DeleteMapping("/delete/{debtId}")
    public ResponseEntity<DebtModel> deleteDebt(@PathVariable String debtId) {
        return ResponseEntity.ok(debtService.deleteDebt(debtId));
    }

    @GetMapping("/card/clean/{cardCode}")
    public ResponseEntity<List<DebtModel>> cleanDebt(@PathVariable String cardCode) {
        return ResponseEntity.ok(debtService.cleanCardDebt(cardCode));
    }
}
