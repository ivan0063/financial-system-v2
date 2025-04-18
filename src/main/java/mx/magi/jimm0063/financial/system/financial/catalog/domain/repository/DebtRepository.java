package mx.magi.jimm0063.financial.system.financial.catalog.domain.repository;

import mx.magi.jimm0063.financial.system.financial.catalog.domain.entity.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "debt", path = "debt")
public interface DebtRepository extends JpaRepository<Debt, String> {
    Optional<Debt> findByDebtIdAndOperationDate(String debtId, LocalDate operationDate);
    List<Debt> findAllByDisabledFalse();
}
