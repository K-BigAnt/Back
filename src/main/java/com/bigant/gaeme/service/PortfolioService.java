package com.bigant.gaeme.service;

import com.bigant.gaeme.dto.*;
import com.bigant.gaeme.repository.*;
import com.bigant.gaeme.repository.entity.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    private final StockRepository stockRepository;

    private final PortfolioStockRepository portfolioStockRepository;

    private final StockPriceRepository stockPriceRepository;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public List<Long> createPortfolio(List<CreatePortfolioRequestDto> dtos) {
        //TODO: 로직 ModelMapper 사용해서 리팩터링 필요
        Map<String, List<CreatePortfolioRequestDto.SimpleStockDto>> stockDtoListByPortfolioName = dtos.stream()
                .collect(Collectors.toMap(CreatePortfolioRequestDto::getName, CreatePortfolioRequestDto::getStocks));
        List<Portfolio> portfolios = dtos.stream().map(dto -> Portfolio.builder().name(dto.getName()).build()).toList();

        portfolioRepository.saveAll(portfolios);

        for (Portfolio portfolio : portfolios) {
            List<CreatePortfolioRequestDto.SimpleStockDto> stockDtos = stockDtoListByPortfolioName.getOrDefault(portfolio.getName(), List.of());

            List<PortfolioStock> portfolioStocks = stockDtos.stream().map(simpleStock -> PortfolioStock.builder()
                    .portfolio(portfolio)
                    .rate(simpleStock.getRate())
                    .stock(stockRepository.findBySymbol(simpleStock.getSymbol()).orElseThrow())
                    .build()).toList();

            portfolioStockRepository.saveAll(portfolioStocks);
        }

        return portfolios.stream().map(Portfolio::getId).toList();
    }

    public BacktestResponseDto backtest(BacktestRequestDto dto) {
        List<String> symbols = dto.getPortfolio().getStocks().stream().map(PortfolioDto.PortfolioStockDto::getSymbol).toList();
        List<Stock> stocks = stockRepository.findAllBySymbolIn(symbols);

        Map<String, List<StockPrice>> pricesByStock = stocks.stream().collect(
                Collectors.toMap(Stock::getSymbol, stockPriceRepository::findAllByStock));

        List<BacktestDto> backtests = new ArrayList<>();

        for (PortfolioDto.PortfolioStockDto stock : dto.getPortfolio().getStocks()) {
            List<BacktestDto.BacktestPriceDto> results = calculateBacktest(stock, pricesByStock.get(stock.getSymbol()), dto.getInitialAmount());
            backtests.add(BacktestDto.builder()
                    .stock(stock)
                    .earns(results)
                    .build());
        }

        return BacktestResponseDto.builder()
                .result(backtests)
                .build();
    }

    private List<BacktestDto.BacktestPriceDto> calculateBacktest(
            PortfolioDto.PortfolioStockDto stockDto, List<StockPrice> prices, Long initialAmount
    ) {
        Long partialInitialAmount = (long) (initialAmount * (stockDto.getRate() * 0.01));
        List<Double> earnRates = new ArrayList<>(List.of(0.0));

        for (int i = 1; i < prices.size(); i++) {
            earnRates.add((double) prices.get(i).getClosePrice() / prices.get(i - 1).getClosePrice());
        }

        System.out.println("earnRates: " + earnRates);

        List<Long> earns = new ArrayList<>();
        for (int i = 0; i < earnRates.size(); i++) {
            if (i == 0) {
                earns.add(partialInitialAmount);
                continue;
            }
            Long earn = (long) (earns.get(i - 1) * earnRates.get(i));
            earns.add(earn);
        }

        List<BacktestDto.BacktestPriceDto> results = new ArrayList<>();
        for (int i = 0; i < prices.size(); i++) {
            results.add(BacktestDto.BacktestPriceDto.builder()
                    .date(prices.get(i).getBusinessDate())
                    .amount(earns.get(i))
                    .build());
        }
        return results;
    }

    @Transactional(readOnly = true)
    public List<PortfolioDto> getMine(Long requestId) {
        User user = userRepository.findById(requestId).orElseThrow();

        List<Portfolio> portfolios = portfolioRepository.findAllByUser_Id(user.getId());
        Map<Portfolio, List<PortfolioStock>> portfolioStocksByPortfolio = portfolios.stream().collect(Collectors.toMap(
                Function.identity(), portfolio -> portfolioStockRepository.findAllByPortfolio_Id(portfolio.getId())
        ));

        Function<Map.Entry<Portfolio, List<PortfolioStock>>, PortfolioDto> convertDto = (entry) -> {
            Portfolio portfolio = entry.getKey();
            List<PortfolioStock> portfolioStocks = entry.getValue();

            return PortfolioDto.builder()
                    .name(portfolio.getName())
                    .stocks(portfolioStocks.stream().map(stocks -> modelMapper.map(stocks, PortfolioDto.PortfolioStockDto.class)).toList())
                    .build();
        };

        return portfolioStocksByPortfolio.entrySet().stream().map(convertDto).toList();
    }
}
