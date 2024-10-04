package com.bigant.gaeme.dao;

import com.bigant.gaeme.dto.StockPriceResponseDto;
import com.bigant.gaeme.repository.StockRepository;
import com.bigant.gaeme.repository.entity.Stock;
import com.bigant.gaeme.repository.entity.UsStock;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StockPriceDaoTest {

    @Autowired
    StockPriceDao stockPriceDao;

    @Autowired
    StockRepository stockRepository;

    @Test
    @Disabled
    void getKrStockPriceTest() {
        StockPriceResponseDto dto = stockPriceDao.getKrStockPrice(LocalDate.of(2022, 7,1),
                LocalDate.of(2024, 7, 1), "A000050");

        System.out.println(dto);
    }

    @Test
    @Disabled
    void getUsStockPriceTest() {
        List<Stock> stocks = stockRepository.findAllByIsDelisting(false).stream().filter(stock -> stock instanceof UsStock).toList();

        stocks.forEach(stock -> {
            StockPriceResponseDto dto = stockPriceDao.getUsStockPrice(LocalDate.of(2022, 7,1),
                    LocalDate.of(2024, 7, 1), stock.getSymbol());

            System.out.println(dto);
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }

}
