package com.bigant.gaeme.dao.dto;

import com.bigant.gaeme.repository.entity.KrStock;
import com.bigant.gaeme.repository.enums.StockType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class KrStockDto extends StockDto {

    @JsonProperty("isinCd")
    private String isinCode;

    @Override
    public KrStock toEntity(StockType type) {
        return KrStock.builder()
                .name(this.getName())
                .symbol(this.getSymbol())
                .isinCode(this.isinCode)
                .isDelisting(false)
                .type(type)
                .build();
    }

}
