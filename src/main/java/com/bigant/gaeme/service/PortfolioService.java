package com.bigant.gaeme.service;

import com.bigant.gaeme.dao.dto.CreatePortfolioRequestDto;
import com.bigant.gaeme.repository.PortfolioRepository;
import com.bigant.gaeme.repository.PortfolioStockRepository;
import com.bigant.gaeme.repository.StockRepository;
import com.bigant.gaeme.repository.entity.Portfolio;
import com.bigant.gaeme.repository.entity.PortfolioStock;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    private final StockRepository stockRepository;

    private final PortfolioStockRepository portfolioStockRepository;

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

}
