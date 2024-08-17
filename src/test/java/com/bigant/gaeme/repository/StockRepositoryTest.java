package com.bigant.gaeme.repository;

import com.bigant.gaeme.repository.entity.KrStock;
import com.bigant.gaeme.repository.entity.Stock;
import com.bigant.gaeme.repository.entity.UsStock;
import com.bigant.gaeme.repository.enums.StockType;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private KrStockRepository krStockRepository;

    @Autowired
    private UsStockRepository usStockRepository;

    @Test
    void searchTest() {
        //given
        List<KrStock> krStocks = List.of(
                KrStock.builder()
                        .name("abcd")
                        .symbol("abcd")
                        .type(StockType.ETF)
                        .isinCode("isin")
                        .isDelisting(false)
                        .build(),
                KrStock.builder()
                        .name("erty")
                        .symbol("erty")
                        .type(StockType.STOCK)
                        .isinCode("isin2")
                        .isDelisting(false)
                        .build()
        );

        List<UsStock> usStocks = List.of(
                UsStock.builder()
                        .name("erery")
                        .symbol("abcd")
                        .type(StockType.STOCK)
                        .country("China")
                        .isDelisting(false)
                        .build()
        );

        krStockRepository.saveAll(krStocks);
        usStockRepository.saveAll(usStocks);

        //when
        List<Stock> stocks = stockRepository.searchByNameOrSymbol("bc");

        //then
        Assertions.assertEquals(List.of(
                krStocks.get(0),
                usStocks.get(0)
        ), stocks);

    }

}
