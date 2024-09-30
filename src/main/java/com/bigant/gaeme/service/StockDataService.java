package com.bigant.gaeme.service;

import com.bigant.gaeme.dao.KrStockDao;
import com.bigant.gaeme.dao.UsStockDao;
import com.bigant.gaeme.repository.KrStockRepository;
import com.bigant.gaeme.repository.UsStockRepository;
import com.bigant.gaeme.repository.entity.KrStock;
import com.bigant.gaeme.repository.entity.UsStock;
import com.bigant.gaeme.repository.enums.StockType;
import com.bigant.gaeme.usecase.StockDataUsecase;
import com.bigant.gaeme.usecase.StockPriceDataUsecase;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockDataService {

    private final StockDataUsecase<KrStock, KrStockRepository> krStockDataUsecase;

    private final StockDataUsecase<UsStock, UsStockRepository> usStockDataUsecase;

    private final KrStockDao krStockDao;

    private final UsStockDao usStockDao;

    private final StockPriceDataUsecase stockPriceDataUsecase;

    @Scheduled(cron = "* * 23 * * 3")
    @Transactional
    public void saveKrStockData() {
        krStockDataUsecase.saveStock(krStockDao.getStock().stream().map(dto -> dto.toEntity(StockType.STOCK)).toList(), StockType.STOCK);
        krStockDataUsecase.saveStock(krStockDao.getEtf().stream().map(dto -> dto.toEntity(StockType.ETF)).toList(), StockType.ETF);
    }

    @Scheduled(cron = "* * 11 * * 3")
    @Transactional
    public void saveUsStockData() {
        usStockDataUsecase.saveStock(usStockDao.getStock().stream().map(dto -> dto.toEntity(StockType.STOCK)).toList(), StockType.STOCK);
        usStockDataUsecase.saveStock(usStockDao.getEtf().stream().map(dto -> dto.toEntity(StockType.ETF)).toList(), StockType.ETF);
    }

    @Scheduled(cron = "0 0 11 1 */1 *")
    @Transactional
    public void saveStockPrice() {
        stockPriceDataUsecase.saveStockPrice(LocalDate.now().minusYears(1), LocalDate.now());
    }

}
