package com.bigant.gaeme.dao.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockPriceDto {

    @JsonAlias({"date", "stck_bsop_date"})
    private String date;

    @JsonAlias({"close_price", "stck_clpr"})
    private String closePrice;

    @JsonAlias({"open_price", "stck_oprc"})
    private String openPrice;

    @JsonAlias({"highest_price", "stck_hgpr"})
    private String highestPrice;

    @JsonAlias({"lowest_price", "stck_lwpr"})
    private String lowestPrice;

    @JsonAlias({"accumulate_volume", "acml_vol"})
    private String accumulateVolume;

    @JsonAlias({"accumilate_trading_amount", "acml_tr_pbmn"})
    private String accumulateTradingAmount;

}
