package com.wbw.eventcoordinator.repository;

import com.wbw.eventcoordinator.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByName(String name);

}
