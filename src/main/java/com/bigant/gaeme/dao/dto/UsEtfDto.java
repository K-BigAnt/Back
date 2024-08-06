package com.bigant.gaeme.dao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsEtfDto {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UsEtfItem extends StockDto {

        private String symbol;

        @JsonProperty("companyName")
        private String name;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UsEtfBody {

        private List<UsEtfItem> rows;

    }

    private UsEtfBody data;

}
