package com.bigant.gaeme.dao.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KrStockDto {

    @JsonProperty("srtnCd")
    private String shortenCode;

    @JsonProperty("isinCd")
    private String isinCode;

    @JsonProperty("itmsNm")
    private String name;

}
