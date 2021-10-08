package com.works.repositories;

import com.works.entities.CalendarInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarInfoRepository extends JpaRepository<CalendarInfo, Integer> {
}
