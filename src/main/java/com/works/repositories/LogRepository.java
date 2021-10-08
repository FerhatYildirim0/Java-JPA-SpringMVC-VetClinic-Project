package com.works.repositories;

import com.works.entities.Logger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Logger, Integer> {
}
