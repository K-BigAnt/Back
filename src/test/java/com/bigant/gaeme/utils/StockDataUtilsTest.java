package com.bigant.gaeme.utils;

import com.bigant.gaeme.service.StockDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StockDataUtilsTest {

    @Autowired
    private StockDataService stockDataService;

    @Test
    void insertStockDateInDatabase() {
        stockDataService.saveKrStockData();
        stockDataService.saveUsStockData();
    }

}
