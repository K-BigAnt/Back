package com.bigant.gaeme.service;

import com.bigant.gaeme.dao.StockDao;
import com.bigant.gaeme.dao.dto.KrStockDto;
import com.bigant.gaeme.dao.dto.UsStockDto.UsStockItem;
import com.bigant.gaeme.repository.KrStockRepository;
import com.bigant.gaeme.repository.UsStockRepository;
import com.bigant.gaeme.repository.entity.KrStock;
import com.bigant.gaeme.repository.entity.UsStock;
import com.bigant.gaeme.repository.enums.StockType;
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

    private final StockDao<KrStockDto> krStockDao;

    private final StockDao<UsStockItem> usStockDao;

    private final KrStockRepository krStockRepository;

    private final UsStockRepository usStockRepository;

    @Scheduled(cron = "* * 23 * * 3")
    @Transactional
    public void saveKrStockData() {
        saveKrStock(krStockDao::getStock, StockType.STOCK);
        saveKrStock(krStockDao::getEtf, StockType.ETF);
    }

    @Scheduled(cron = "* * 11 * * 3")
    @Transactional
    public void saveUsStockData() {
        saveUsStock(usStockDao::getStock, StockType.STOCK);
        saveUsStock(usStockDao::getEtf, StockType.ETF);
    }

    private void saveKrStock(Supplier<List<KrStockDto>> dataFunc, StockType type) {
        List<KrStockDto> newStocks = dataFunc.get();
        Set<KrStockDto> exists = krStockRepository.findAll().stream()
                .filter(stock -> stock.getType() == type)
                .map(KrStock::toDto)
                .collect(Collectors.toSet());

        List<KrStockDto> addStocks = newStocks.stream().filter(item -> !exists.contains(item)).toList();
        List<KrStockDto> delistingStocks = exists.stream().filter(item -> !newStocks.contains(item)).toList();

        krStockRepository.saveAll(addStocks.stream().map(stock -> stock.toEntity(type)).toList());

        delistStocks(delistingStocks);
    }

    private void saveUsStock(Supplier<List<UsStockItem>> dataFunc, StockType type) {
        List<UsStockItem> newStocks = dataFunc.get();
        Set<UsStockItem> exists = usStockRepository.findAll().stream()
                .filter(stock -> stock.getType() == type)
                .map(UsStock::toDto)
                .collect(Collectors.toSet());

        List<UsStockItem> addStocks = newStocks.stream().filter(item -> !exists.contains(item)).toList();
        List<UsStockItem> delistingStocks = exists.stream().filter(item -> !newStocks.contains(item)).toList();

        usStockRepository.saveAll(addStocks.stream().map(stock -> stock.toEntity(type)).toList());
        delistUsStocks(delistingStocks);
    }

    private void delistStocks(List<KrStockDto> stocks) {
        List<KrStock> delistingStocks = krStockRepository.findAllBySymbolIn(stocks.stream().map(KrStockDto::getSymbol).toList());

        delistingStocks.forEach(delistingStock -> delistingStock.setDelisting(true));

        krStockRepository.saveAll(delistingStocks);
    }

    private void delistUsStocks(List<UsStockItem> stocks) {
        List<UsStock> delistingStocks = usStockRepository.findAllBySymbolIn(stocks.stream().map(UsStockItem::getSymbol).toList());

        delistingStocks.forEach(delistingStock -> delistingStock.setDelisting(true));

        usStockRepository.saveAll(delistingStocks);
    }

}
