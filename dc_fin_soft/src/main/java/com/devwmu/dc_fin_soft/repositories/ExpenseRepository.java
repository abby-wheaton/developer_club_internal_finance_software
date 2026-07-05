package com.devwmu.dc_fin_soft.repositories;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import com.devwmu.dc_fin_soft.entities.Expense;
import java.util.List;


public interface ExpenseRepository extends CrudRepository<Expense, Integer>, JpaSpecificationExecutor<Expense> {
    List<Expense> findByEventIdAndFoodFlag(Integer eventId, Integer foodFlag);
} 
