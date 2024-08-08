package com.bigant.gaeme.usecase;

import com.bigant.gaeme.repository.KrStockRepository;
import com.bigant.gaeme.repository.entity.KrStock;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StockDataUsecaseTest {

    private StockDataUsecase<KrStock, KrStockRepository> stockDataUsecase;

    private KrStockRepository krStockRepository;

    @Autowired
    public StockDataUsecaseTest(KrStockRepository krStockRepository) {
        this.krStockRepository = krStockRepository;
        this.stockDataUsecase = new StockDataUsecase<>(krStockRepository);
    }

    @Test
    void test() {

        krStockRepository.save(KrStock.builder()
                        .name("Abc")
                        .symbol("abc")
                        .isinCode("abc")
                        .isDelisting(false)
                .build());

        List<KrStock> result = krStockRepository.findAll();

        Assertions.assertEquals(KrStock.builder()
                .id(result.get(0).getId())
                .name("Abc")
                .symbol("abc")
                .isinCode("abc")
                .isDelisting(false)
                .build(), result.get(0));
    }

}
