package com.bigant.gaeme.dao.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class StockDto {

    @JsonAlias({"itmsNm", "name"})
    private String name;

    @JsonAlias({"srtnCd", "symbol"})
    private String symbol;

}
