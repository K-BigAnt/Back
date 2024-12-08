package com.bigant.gaeme.service;

import com.bigant.gaeme.TestFixture;
import com.bigant.gaeme.dto.*;
import com.bigant.gaeme.modelmapper.PortfolioStockToDto;
import com.bigant.gaeme.repository.*;
import com.bigant.gaeme.repository.entity.*;
import com.bigant.gaeme.repository.enums.StockType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PortfolioServiceTest {

    private final PortfolioRepository portfolioRepository;

    private final StockRepository stockRepository;

    private final PortfolioStockRepository portfolioStockRepository;

    private final PortfolioService portfolioService;

    private final StockPriceRepository stockPriceRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public PortfolioServiceTest(
            PortfolioRepository portfolioRepository,
            StockRepository stockRepository,
            PortfolioStockRepository portfolioStockRepository,
            StockPriceRepository stockPriceRepository,
            UserRepository userRepository
    ) {
        this.portfolioRepository = portfolioRepository;
        this.stockRepository = stockRepository;
        this.portfolioStockRepository = portfolioStockRepository;
        this.stockPriceRepository = stockPriceRepository;
        this.userRepository = userRepository;
        this.modelMapper = new ModelMapper();
        this.modelMapper.addConverter(new PortfolioStockToDto());
        this.portfolioService = new PortfolioService(
                portfolioRepository,
                stockRepository,
                portfolioStockRepository,
                stockPriceRepository,
                userRepository,
                modelMapper
        );
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

    @Test
    void 백테스트_성공() {
        //given
        BacktestRequestDto request = BacktestRequestDto.builder()
                .startDate(LocalDate.of(2023, 10, 1))
                .endDate(LocalDate.of(2024, 11, 1))
                .portfolio(PortfolioDto.builder()
                        .name("test")
                        .stocks(List.of(
                                PortfolioDto.PortfolioStockDto.builder()
                                        .symbol("APPL")
                                        .rate(100)
                                        .build()
                        ))
                        .build())
                .initialAmount(10000L)
                .build();

        Stock apple = UsStock.builder()
                .symbol("APPL")
                .isDelisting(false)
                .name("APPLE")
                .country("USA")
                .type(StockType.STOCK)
                .build();

        List<StockPrice> prices = List.of(
                StockPrice.builder()
                        .stock(apple)
                        .businessDate(LocalDate.of(2023, 11, 30))
                        .closePrice(1000L)
                        .build(),
                StockPrice.builder()
                        .stock(apple)
                        .businessDate(LocalDate.of(2023, 12, 31))
                        .closePrice(1200L)
                        .build(),
                StockPrice.builder()
                        .stock(apple)
                        .businessDate(LocalDate.of(2024, 1, 31))
                        .closePrice(1400L)
                        .build()
        );

        stockRepository.save(apple);
        stockPriceRepository.saveAll(prices);

        //when
        BacktestResponseDto result = portfolioService.backtest(request);

        //then
        Assertions.assertEquals(
                BacktestResponseDto.builder()
                        .result(List.of(
                                BacktestDto.builder()
                                        .stock(PortfolioDto.PortfolioStockDto.builder()
                                                .symbol("APPL")
                                                .rate(100)
                                                .build())
                                        .earns(
                                                List.of(
                                                        BacktestDto.BacktestPriceDto.builder()
                                                                .date(LocalDate.of(2023, 11, 30))
                                                                .amount(10000L)
                                                                .build(),
                                                        BacktestDto.BacktestPriceDto.builder()
                                                                .date(LocalDate.of(2023, 12, 31))
                                                                .amount(12000L)
                                                                .build(),
                                                        BacktestDto.BacktestPriceDto.builder()
                                                                .date(LocalDate.of(2024, 1, 31))
                                                                .amount(14000L)
                                                                .build()
                                                )
                                        )
                                        .build()
                        ))
                        .build(), result
        );
    }

    @Test
    void 내포트폴리오_조회_성공_포트폴리오_없음() {
        //given
        User user = TestFixture.getTestUser();
        userRepository.save(user);

        //when
        List<Portfolio> result = portfolioRepository.findAllByUser_Id(user.getId());

        //then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void 내포트폴리오_조회_성공_포트폴리오_있음() {
        //given
        User user = TestFixture.getTestUser();
        Portfolio portfolio = TestFixture.getTestPortfolio(user);
        Stock stock = TestFixture.getTestStock();

        portfolioRepository.save(portfolio);
        stockRepository.save(stock);
        portfolioStockRepository.save(PortfolioStock.builder()
                        .portfolio(portfolio)
                        .stock(stock)
                        .rate(100)
                .build());
        userRepository.save(user);

        //when
        List<PortfolioDto> result = portfolioService.getMine(user.getId());

        //then
        Assertions.assertEquals(List.of(
                PortfolioDto.builder()
                        .name("test-portfolio")
                        .stocks(List.of(
                                PortfolioDto.PortfolioStockDto.builder()
                                        .symbol(stock.getSymbol())
                                        .rate(100)
                                        .build()
                        ))
                        .build()
        ), result);
    }

    @Test
    void 포트폴리오_삭제_성공() {
        //given
        User user = TestFixture.getTestUser();
        Portfolio portfolio = TestFixture.getTestPortfolio(user);
        Stock stock = TestFixture.getTestStock();

        userRepository.save(user);
        portfolioRepository.save(portfolio);
        stockRepository.save(stock);
        portfolioStockRepository.save(PortfolioStock.builder()
                        .rate(100)
                        .portfolio(portfolio)
                        .stock(stock)
                .build());

        //when
        PortfolioDto result = portfolioService.delete(user.getId(), portfolio.getId());

        //then
        Assertions.assertEquals(
                PortfolioDto.builder()
                        .name("test-portfolio")
                        .stocks(List.of())
                        .isDeleted(true)
                        .build(), result
        );
    }

}
