package com.bigant.gaeme.modelmapper;

import com.bigant.gaeme.dao.dto.StockSearchDto;
import com.bigant.gaeme.repository.entity.KrStock;
import org.modelmapper.AbstractConverter;

public class KrStockToStockSearchDtoConverter extends AbstractConverter<KrStock, StockSearchDto> {

    @Override
    protected StockSearchDto convert(KrStock source) {
        return StockSearchDto.builder()
                .name(source.getName())
                .symbol(source.getSymbol())
                .type(source.getType())
                .country("South Korea")
                .build();
    }

}
