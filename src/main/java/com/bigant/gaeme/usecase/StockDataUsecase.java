package com.bigant.gaeme.usecase;

import com.bigant.gaeme.repository.entity.Stock;
import com.bigant.gaeme.repository.enums.StockType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class StockDataUsecase<T extends Stock, R extends JpaRepository<T, Long>> {

    private final R stockRepository;

    public void saveStock(List<T> newStocks, StockType type) {
        Set<T> exists = stockRepository.findAll().stream()
                .filter(stock -> stock.getType() == type)
                .collect(Collectors.toSet());

        List<T> addStocks = newStocks.stream().filter(item -> exists.stream().noneMatch(item2 -> item.getSymbol().equals(item2.getSymbol()))).toList();
        List<T> delistingStocks = exists.stream().filter(item -> newStocks.stream().anyMatch(item2 -> item.getSymbol().equals(item2.getSymbol()))).toList();

        stockRepository.saveAll(addStocks);
        delistStocks(delistingStocks);
    }

    private void delistStocks(List<T> stocks) {
        stocks.forEach(stock -> stock.setDelisting(true));
        stockRepository.saveAll(stocks);
    }

}
