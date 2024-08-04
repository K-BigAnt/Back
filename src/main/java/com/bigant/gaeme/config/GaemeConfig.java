package com.bigant.gaeme.config;

import com.bigant.gaeme.dao.KrStockDao;
import com.bigant.gaeme.dao.UsStockDao;
import com.bigant.gaeme.repository.KrStockRepository;
import com.bigant.gaeme.repository.UsStockRepository;
import com.bigant.gaeme.repository.entity.KrStock;
import com.bigant.gaeme.repository.entity.UsStock;
import com.bigant.gaeme.usecase.StockDataUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
@RequiredArgsConstructor
public class GaemeConfig {

    private final UsStockRepository usStockRepository;

    private final KrStockRepository krStockRepository;

    @Bean
    public StockDataUsecase<UsStock, UsStockRepository> usStockDataUsecase() {
        return new StockDataUsecase<>(usStockRepository);
    }

    @Bean
    public StockDataUsecase<KrStock, KrStockRepository> krStockDataUsecase() {
        return new StockDataUsecase<>(krStockRepository);
    }

}
