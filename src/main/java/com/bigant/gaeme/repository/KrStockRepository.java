package com.bigant.gaeme.repository;

import com.bigant.gaeme.repository.entity.KrStock;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KrStockRepository extends JpaRepository<KrStock, Long> {

    List<KrStock> findAllBySymbolIn(List<String> symbols);

}
