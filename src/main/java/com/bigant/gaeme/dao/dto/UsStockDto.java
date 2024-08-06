package com.bigant.gaeme.dao.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsStockDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UsStockItem {

        private String symbol;

        private String name;

        private String country;

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
