package com.devwmu.dc_fin_soft.repositories;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import com.devwmu.dc_fin_soft.entities.Expense;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;



public interface ExpenseRepository extends CrudRepository<Expense, Integer>, JpaSpecificationExecutor<Expense> {
    List<Expense> findByEventIdAndFoodFlag(Integer eventId, Integer foodFlag);
    List<Expense> findByItemDeadlineBetween(LocalDateTime date1, LocalDateTime date2);
    List<Expense> findByAllocationDeadlineBetween(LocalDateTime date1, LocalDateTime date2);
    List<Expense> findByDeliberationDeadlineBetween(LocalDateTime date1, LocalDateTime date2);
    List<Expense> findByReimbursementDeadlineBetween(LocalDateTime date1, LocalDateTime date2);
    List<Expense> findByMoneyRemainingGreaterThan(BigDecimal amt);

} 
