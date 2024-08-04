package com.bigant.gaeme.service;

import com.bigant.gaeme.dao.KrStockDao;
import com.bigant.gaeme.dao.StockDao;
import com.bigant.gaeme.dao.UsStockDao;
import com.bigant.gaeme.dao.dto.KrStockDto;
import com.bigant.gaeme.dao.dto.UsStockDto.UsStockItem;
import com.bigant.gaeme.repository.KrStockRepository;
import com.bigant.gaeme.repository.UsStockRepository;
import com.bigant.gaeme.repository.entity.KrStock;
import com.bigant.gaeme.repository.entity.UsStock;
import com.bigant.gaeme.repository.enums.StockType;
import com.bigant.gaeme.usecase.StockDataUsecase;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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

}
