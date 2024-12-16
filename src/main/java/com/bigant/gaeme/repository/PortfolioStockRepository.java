package com.bigant.gaeme.repository;

import com.bigant.gaeme.repository.entity.PortfolioStock;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioStockRepository extends JpaRepository<PortfolioStock, Long> {

    List<PortfolioStock> findAllByPortfolio_Id(Long portfolioId);

}
