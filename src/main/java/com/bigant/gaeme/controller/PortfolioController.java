package com.bigant.gaeme.controller;

import com.bigant.gaeme.dao.dto.CreatePortfolioRequestDto;
import com.bigant.gaeme.service.PortfolioService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping
    public List<Long> createPortfolio(@RequestBody List<CreatePortfolioRequestDto> dto) {
        return portfolioService.createPortfolio(dto);
    }

}
