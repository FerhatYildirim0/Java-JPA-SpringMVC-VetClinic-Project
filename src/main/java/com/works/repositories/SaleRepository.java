package com.works.repositories;

import com.works.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
    List<Sale> getByFaturaNoEquals(int faturaNo);

    List<Sale> findByStatusEquals(boolean status);





}
