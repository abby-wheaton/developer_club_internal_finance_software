package com.devwmu.dc_fin_soft.repositories;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import com.devwmu.dc_fin_soft.entities.Event;

public interface EventRepository extends CrudRepository<Event, Integer>, JpaSpecificationExecutor<Event> {
} 