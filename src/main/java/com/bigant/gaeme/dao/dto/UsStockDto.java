package com.bigant.gaeme.dao.dto;

import com.bigant.gaeme.repository.entity.UsStock;
import com.bigant.gaeme.repository.enums.StockType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsStockDto {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UsStockItem extends StockDto {

        private String country;

        @Override
        public UsStock toEntity(StockType type) {
            return UsStock.builder()
                    .name(this.getName())
                    .country(this.getCountry())
                    .isDelisting(false)
                    .symbol(this.getSymbol())
                    .type(type)
                    .build();
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UsStockBody {

        private List<UsStockItem> rows;

    }

    private UsStockBody data;

}
