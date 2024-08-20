package com.bigant.gaeme.service;

import com.bigant.gaeme.dao.dto.CreatePortfolioRequestDto;
import com.bigant.gaeme.repository.PortfolioRepository;
import com.bigant.gaeme.repository.PortfolioStockRepository;
import com.bigant.gaeme.repository.StockRepository;
import com.bigant.gaeme.repository.entity.KrStock;
import com.bigant.gaeme.repository.entity.Portfolio;
import com.bigant.gaeme.repository.entity.UsStock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PortfolioServiceTest {

    private final PortfolioRepository portfolioRepository;

    private final StockRepository stockRepository;

    private final PortfolioStockRepository portfolioStockRepository;

    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioServiceTest(PortfolioRepository portfolioRepository, StockRepository stockRepository, PortfolioStockRepository portfolioStockRepository) {
        this.portfolioRepository = portfolioRepository;
        this.stockRepository = stockRepository;
        this.portfolioStockRepository = portfolioStockRepository;
        this.portfolioService = new PortfolioService(portfolioRepository, stockRepository, portfolioStockRepository);
    }

    @Test
    void 포트폴리오_생성_성공() {
        //given
        List<CreatePortfolioRequestDto> dtos = List.of(
                CreatePortfolioRequestDto.builder()
                        .name("port1")
                        .stocks(List.of(
                                CreatePortfolioRequestDto.SimpleStockDto.builder()
                                        .symbol("stock1")
                                        .rate(10)
                                        .build(),
                                CreatePortfolioRequestDto.SimpleStockDto.builder()
                                        .symbol("stock2")
                                        .rate(20)
                                        .build()
                        ))
                        .build()
        );

        stockRepository.saveAll(List.of(
                KrStock.builder()
                        .name("stock1")
                        .symbol("stock1")
                        .isDelisting(false)
                        .isinCode("stock1")
                        .build(),
                UsStock.builder()
                        .name("stock2")
                        .symbol("stock2")
                        .isDelisting(false)
                        .country("US")
                        .build()
                ));

        //when
        List<Long> ids = portfolioService.createPortfolio(dtos);
        Portfolio result = portfolioRepository.findById(ids.get(0)).orElseThrow();

        //then
        Assertions.assertEquals("port1", result.getName());
    }

}
