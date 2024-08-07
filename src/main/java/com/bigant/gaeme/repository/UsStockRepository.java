package com.bigant.gaeme.repository;

import com.bigant.gaeme.repository.entity.KrStock;
import com.bigant.gaeme.repository.entity.UsStock;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsStockRepository extends JpaRepository<UsStock, Long> {

    List<UsStock> findAllBySymbolIn(List<String> symbols);

}
