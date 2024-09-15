package com.bigant.gaeme.modelmapper;

import com.bigant.gaeme.dto.StockPriceDto;
import com.bigant.gaeme.repository.entity.StockPrice;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.modelmapper.AbstractConverter;

public class StockPriceDtoToStockPriceConverter extends AbstractConverter<StockPriceDto, StockPrice> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    protected StockPrice convert(StockPriceDto stockPriceDto) {
        return StockPrice.builder()
                .businessDate(LocalDate.parse(stockPriceDto.getBusinessDate(), formatter))
                .openPrice(Long.parseLong(stockPriceDto.getOpenPrice()))
                .closePrice(Long.parseLong(stockPriceDto.getClosePrice()))
                .highestPrice(Long.parseLong(stockPriceDto.getHighestPrice()))
                .lowestPrice(Long.parseLong(stockPriceDto.getLowestPrice()))
                .accumulatedTradingAmount(Long.parseLong(stockPriceDto.getAccumulatedTradingAmount()))
                .accumulatedVolume(Long.parseLong(stockPriceDto.getAccumulatedVolume()))
                .previousDayContrastTodaySign(stockPriceDto.getPreviousDayContrastTodaySign())
                .previousDayContrastTodayPrice(Long.parseLong(stockPriceDto.getPreviousDayContrastTodayPrice()))
                .build();
    }
}
