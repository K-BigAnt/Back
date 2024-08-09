package com.bigant.gaeme.dao.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockPriceResponseDto {

    @JsonAlias({"prices", "output2"})
    private List<StockPriceDto> prices;

}
