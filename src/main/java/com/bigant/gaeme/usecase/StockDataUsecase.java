package com.bigant.gaeme.usecase;

import com.bigant.gaeme.repository.entity.Stock;
import com.bigant.gaeme.repository.enums.StockType;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class StockDataUsecase<T extends Stock, R extends JpaRepository<T, Long>> {

    private final R stockRepository;

    public void saveStock(List<T> newStocks, StockType type) {
        Map<String, T> exists = stockRepository.findAll().stream()
                .filter(stock -> stock.getType() == type)
                .collect(Collectors.toMap(Stock::getSymbol, item -> item));

        Map<String, T> newStocksBySymbol = newStocks.stream()
                .collect(Collectors.toMap(Stock::getSymbol, item -> item));

        List<T> addStocks = newStocksBySymbol.entrySet().stream()
                .filter(item -> !exists.containsKey(item.getKey()))
                .map(Entry::getValue)
                .toList();
        List<T> delistingStocks = exists.entrySet().stream()
                .filter(item -> newStocksBySymbol.containsKey(item.getKey()))
                .map(Entry::getValue)
                .toList();

        stockRepository.saveAll(addStocks);
        delistStocks(delistingStocks);
    }

    private void delistStocks(List<T> stocks) {
        stocks.forEach(stock -> stock.setDelisting(true));
        stockRepository.saveAll(stocks);
    }

}
