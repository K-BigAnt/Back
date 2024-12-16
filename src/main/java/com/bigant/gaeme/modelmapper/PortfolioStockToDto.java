package com.bigant.gaeme.modelmapper;

import com.bigant.gaeme.dto.PortfolioDto;
import com.bigant.gaeme.repository.entity.PortfolioStock;
import org.modelmapper.AbstractConverter;

public class PortfolioStockToDto extends AbstractConverter<PortfolioStock, PortfolioDto.PortfolioStockDto> {

    @Override
    protected PortfolioDto.PortfolioStockDto convert(PortfolioStock portfolioStock) {
        return PortfolioDto.PortfolioStockDto.builder()
                .symbol(portfolioStock.getStock().getSymbol())
                .rate(portfolioStock.getRate())
                .build();
    }

}
