package com.bigant.gaeme.usecase;

import com.bigant.gaeme.dao.StockPriceDao;
import com.bigant.gaeme.dto.StockPriceDto;
import com.bigant.gaeme.repository.StockPriceRepository;
import com.bigant.gaeme.repository.StockRepository;
import com.bigant.gaeme.repository.entity.Stock;
import com.bigant.gaeme.repository.entity.StockPrice;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockPriceDataUsecase {

    private final StockPriceDao stockPriceDao;

    private final StockRepository stockRepository;

    private final StockPriceRepository stockPriceRepository;

    public void saveKrStockPrice(LocalDate startDate, LocalDate endDate) {
        List<Stock> stocks = stockRepository.findAllByIsDelisting(false);

        for (Stock stock : stocks) {
            try {
                saveStockPrice(startDate, endDate, stock);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void saveStockPrice(LocalDate startDate, LocalDate endDate, Stock stock) throws InterruptedException {
        List<StockPriceDto> results = stockPriceDao.getKrStockPrice(startDate, endDate, stock.getSymbol()).getPrices();
        List<StockPrice> prices = results.stream().filter(stockPriceDto -> stockPriceDto.getBusinessDate() != null)
                .map(stockPriceDto -> stockPriceDto.toEntity(stock))
                .toList();

        stockPriceRepository.saveAll(prices);
        Thread.sleep(300);
    }

}
