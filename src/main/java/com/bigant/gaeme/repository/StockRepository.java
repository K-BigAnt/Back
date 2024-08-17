package com.bigant.gaeme.repository;

import com.bigant.gaeme.repository.entity.KrStock;
import com.bigant.gaeme.repository.entity.Stock;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("SELECT s FROM Stock s WHERE s.name LIKE %:query% OR s.symbol LIKE %:query%")
    List<Stock> searchByNameOrSymbol(String query);

}
