package com.bigant.gaeme.service;

import static org.junit.jupiter.api.Assertions.*;

import com.bigant.gaeme.dao.KrStockDao;
import com.bigant.gaeme.dao.dto.KrStockDto;
import com.bigant.gaeme.repository.KrStockRepository;
import com.bigant.gaeme.repository.entity.KrStock;
import com.bigant.gaeme.repository.enums.StockType;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@Transactional
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class StockDataServiceTest {

    @Autowired
    private KrStockRepository krStockRepository;

    private KrStockDao krStockDao;

    private StockDataService stockDataService;

    @BeforeEach
    void setup() {
        krStockDao = Mockito.mock(KrStockDao.class);
        stockDataService = new StockDataService(krStockDao, krStockRepository);
    }

    @Test
    void 기존_데이터_없을때_주식_데이터_저장_테스트() {
        //given
        Mockito.doReturn(List.of(KrStockDto.builder()
                .name("stock")
                .symbol("abc")
                .isinCode("abcd")
                .build())).when(krStockDao).getStock();
        Mockito.doReturn(List.of(KrStockDto.builder()
                .name("etf")
                .symbol("def")
                .isinCode("defg")
                .build())).when(krStockDao).getEtf();

        //when
        stockDataService.saveData();
        List<KrStock> result = krStockRepository.findAll();

        //then
        assertEquals(
                List.of(
                        KrStock.builder()
                                .id(result.get(0).getId())
                                .name("stock")
                                .symbol("abc")
                                .isinCode("abcd")
                                .type(StockType.STOCK)
                                .isDelisting(false)
                                .build(),
                        KrStock.builder()
                                .id(result.get(1).getId())
                                .name("etf")
                                .symbol("def")
                                .isinCode("defg")
                                .type(StockType.ETF)
                                .isDelisting(false)
                                .build()
                ),
                result
        );
    }

    @Test
    void 기존_데이터_존재할때_데이터_추가() {
        //given
        krStockRepository.save(KrStock.builder()
                .name("stock1")
                .isinCode("abc")
                .symbol("abc")
                .isDelisting(false)
                .type(StockType.STOCK)
                .build());
        Mockito.doReturn(List.of(KrStockDto.builder()
                        .name("stock2")
                        .symbol("def")
                        .isinCode("defg")
                        .build(),
                KrStockDto.builder()
                        .name("stock1")
                        .isinCode("abc")
                        .symbol("abc")
                        .build()
        )).when(krStockDao).getStock();

        //when
        stockDataService.saveData();
        List<KrStock> result = krStockRepository.findAll();


        //then
        assertEquals(
                List.of(
                        KrStock.builder()
                                .id(result.get(0).getId())
                                .name("stock1")
                                .symbol("abc")
                                .isinCode("abc")
                                .type(StockType.STOCK)
                                .isDelisting(false)
                                .build(),
                        KrStock.builder()
                                .id(result.get(1).getId())
                                .name("stock2")
                                .symbol("def")
                                .isinCode("defg")
                                .type(StockType.STOCK)
                                .isDelisting(false)
                                .build()
                ),
                result
        );
    }

    @Test
    void 기존_데이터_존재할때_상장폐지_되는_경우() {
        //given
        krStockRepository.save(KrStock.builder()
                .name("stock1")
                .isinCode("abc")
                .symbol("abc")
                .isDelisting(false)
                .type(StockType.STOCK)
                .build());
        Mockito.doReturn(List.of(KrStockDto.builder()
                        .name("stock2")
                        .symbol("def")
                        .isinCode("defg")
                        .build()
        )).when(krStockDao).getStock();

        //when
        stockDataService.saveData();
        List<KrStock> result = krStockRepository.findAll();

        //then
        assertEquals(
                List.of(
                        KrStock.builder()
                                .id(result.get(0).getId())
                                .name("stock1")
                                .symbol("abc")
                                .isinCode("abc")
                                .type(StockType.STOCK)
                                .isDelisting(true)
                                .build(),
                        KrStock.builder()
                                .id(result.get(1).getId())
                                .name("stock2")
                                .symbol("def")
                                .isinCode("defg")
                                .type(StockType.STOCK)
                                .isDelisting(false)
                                .build()
                ),
                result
        );
    }


}
