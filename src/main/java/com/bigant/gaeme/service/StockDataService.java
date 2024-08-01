package com.bigant.gaeme.service;

import com.bigant.gaeme.dao.StockDao;
import com.bigant.gaeme.dao.dto.KrStockDto;
import com.bigant.gaeme.repository.KrStockRepository;
import com.bigant.gaeme.repository.entity.KrStock;
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

    private final StockDao krStockDao;

    private final KrStockRepository krStockRepository;

    @Scheduled(cron = "* * 23 * * 3")
    @Transactional
    public void saveData() {
        saveStockData(krStockDao::getStock, StockType.STOCK);
        saveStockData(krStockDao::getEtf, StockType.ETF);
    }

    private void saveStockData(Supplier<List<KrStockDto>> dataFunc, StockType type) {
        List<KrStockDto> newStocks = dataFunc.get();
        Set<KrStockDto> exists = krStockRepository.findAll().stream()
                .filter(stock -> stock.getType() == type)
                .map(this::toKrStockDto)
                .collect(Collectors.toSet());
        List<KrStockDto> addStocks = newStocks.stream().filter(item -> !exists.contains(item)).toList();
        List<KrStockDto> delistingStocks = exists.stream().filter(item -> !newStocks.contains(item)).toList();

        krStockRepository.saveAll(addStocks.stream().map(stock -> toKrStock(stock, false, type)).toList());

        delistStocks(delistingStocks);
    }

    private void delistStocks(List<KrStockDto> stocks) {
        List<KrStock> delistingStocks = krStockRepository.findAllBySymbolIn(stocks.stream().map(KrStockDto::getShortenCode).toList());

        delistingStocks.forEach(delistingStock -> delistingStock.setDelisting(true));

        krStockRepository.saveAll(delistingStocks);
    }

    private KrStockDto toKrStockDto(KrStock stock) {
        return KrStockDto.builder()
                .name(stock.getName())
                .shortenCode(stock.getSymbol())
                .isinCode(stock.getIsinCode()).build();
    }

    private KrStock toKrStock(KrStockDto dto, boolean isDelisting, StockType type) {
        return KrStock.builder()
                .name(dto.getName())
                .symbol(dto.getShortenCode())
                .isinCode(dto.getIsinCode())
                .isDelisting(isDelisting)
                .type(type)
                .build();
    }

}
