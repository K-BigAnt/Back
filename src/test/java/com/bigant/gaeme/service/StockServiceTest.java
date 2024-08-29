package com.bigant.gaeme.service;

import com.bigant.gaeme.dto.StockSearchDto;
import com.bigant.gaeme.modelmapper.KrStockToStockSearchDtoConverter;
import com.bigant.gaeme.repository.KrStockRepository;
import com.bigant.gaeme.repository.StockRepository;
import com.bigant.gaeme.repository.UsStockRepository;
import com.bigant.gaeme.repository.entity.KrStock;
import com.bigant.gaeme.repository.entity.UsStock;
import com.bigant.gaeme.repository.enums.StockType;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class StockServiceTest {

    private final StockService stockService;

    private final KrStockRepository krStockRepository;

    private final UsStockRepository usStockRepository;

    @Autowired
    public StockServiceTest(StockRepository stockRepository, KrStockRepository krStockRepository,
                            UsStockRepository usStockRepository) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(new KrStockToStockSearchDtoConverter());

        this.stockService = new StockService(stockRepository, modelMapper);
        this.krStockRepository = krStockRepository;
        this.usStockRepository = usStockRepository;
    }

    @Test
    void 주식_검색_성공_테스트() {
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
        List<StockSearchDto> result = stockService.searchStock("bc");

        //then
        Assertions.assertEquals(
                List.of(StockSearchDto.builder()
                                .name("abcd")
                                .symbol("abcd")
                                .type(StockType.ETF)
                                .country("South Korea")
                                .build(),
                        StockSearchDto.builder()
                                .name("erery")
                                .symbol("abcd")
                                .type(StockType.STOCK)
                                .country("China")
                                .build()),
                result);
    }

}
