package com.bigant.gaeme.dao.dto;

import com.bigant.gaeme.repository.enums.StockType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockSearchDto {

    private String name;

    private String symbol;

    private StockType type;

    private String country;

}