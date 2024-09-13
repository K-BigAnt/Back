package com.bigant.gaeme.dao;

import com.bigant.gaeme.dto.StockPriceResponseDto;
import com.bigant.gaeme.dto.TokenResponseDto;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StockPriceDaoTest {

    @Autowired
    StockPriceDao stockPriceDao;

    @Test
    void getKrStockPriceTest() {
        StockPriceResponseDto dto = stockPriceDao.getKrStockPrice(LocalDate.of(2022, 7,1),
                LocalDate.of(2024, 7, 1), "A000050");

        System.out.println(dto);
    }

}
