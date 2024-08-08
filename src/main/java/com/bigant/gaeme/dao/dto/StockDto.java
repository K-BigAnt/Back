package com.bigant.gaeme.dao.dto;

import com.bigant.gaeme.repository.entity.Stock;
import com.bigant.gaeme.repository.enums.StockType;
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

    @JsonAlias({"itmsNm", "name", "companyName"})
    private String name;

    @JsonAlias({"srtnCd", "symbol"})
    private String symbol;

    public abstract Stock toEntity(StockType type);

}
