package com.bigant.gaeme.controller;

import com.bigant.gaeme.component.JwtBuilder;
import com.bigant.gaeme.dto.*;
import com.bigant.gaeme.service.PortfolioService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    private final JwtBuilder jwtBuilder;

    @PostMapping
    public List<Long> createPortfolio(@RequestBody List<CreatePortfolioRequestDto> dto) {
        return portfolioService.createPortfolio(dto);
    }

    @GetMapping
    public BacktestResponseDto backtest(@RequestBody BacktestRequestDto dto) {
        System.out.println("dto: " + dto);
        return portfolioService.backtest(dto);
    }

    @GetMapping(params = "type=my")
    public List<PortfolioDto> getMine(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return portfolioService.getMine(jwtBuilder.decryptJwt(token));
    }

}
