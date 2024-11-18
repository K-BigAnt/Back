package com.bigant.gaeme.controller;

import com.bigant.gaeme.dto.BacktestDto;
import com.bigant.gaeme.dto.BacktestRequestDto;
import com.bigant.gaeme.dto.BacktestResponseDto;
import com.bigant.gaeme.dto.CreatePortfolioRequestDto;
import com.bigant.gaeme.service.PortfolioService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping
    public List<Long> createPortfolio(@RequestBody List<CreatePortfolioRequestDto> dto) {
        return portfolioService.createPortfolio(dto);
    }

    @GetMapping
    public BacktestResponseDto backtest(@RequestBody BacktestRequestDto dto) {
        System.out.println("dto: " + dto);
        return portfolioService.backtest(dto);
    }

}
