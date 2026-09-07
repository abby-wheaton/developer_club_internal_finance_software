package com.devwmu.dc_fin_soft.repositories;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import com.devwmu.dc_fin_soft.entities.Request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends CrudRepository<Request, Integer>, JpaSpecificationExecutor<Request> {
    List<Request> findByDeadlineBetweenAndApprovalIsNull(LocalDateTime date1, LocalDateTime date2);
} 