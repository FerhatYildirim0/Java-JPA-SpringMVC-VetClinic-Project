package com.works.repositories;

import com.works.entities.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Integer> {

    @Query(value = "select * from Agenda order by agid DESC", nativeQuery = true)
    List<Agenda> findByOrderByAgidDesc();

}
