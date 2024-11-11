package com.bigant.gaeme.repository;

import com.bigant.gaeme.repository.entity.Stock;
import com.bigant.gaeme.repository.entity.StockPrice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {
    List<StockPrice> findAllByStock(Stock stock);
}
