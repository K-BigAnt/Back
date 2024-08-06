package com.bigant.gaeme.dao.dto;

import com.bigant.gaeme.dao.dto.UsEtfDto.UsEtfBody;
import com.bigant.gaeme.dao.dto.UsEtfDto.UsEtfItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UsEtfDtoTest {

    private final ObjectMapper objectMapper;

    public UsEtfDtoTest() {
        this.objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
:wq:    void 미국_ETF_Json_파싱() throws JsonProcessingException {
        String json = """
                {
                    "data": {
                        "asOf": null,
                        "headers": {
                                        "symbol": "SYMBOL",
                                        "companyName": "NAME",
                                        "lastSalePrice": "LAST PRICE",
                                        "netChange": "NET CHANGE",
                                        "percentageChange": "% CHANGE",
                                        "deltaIndicator": "DELTA",
                                        "oneYearPercentage": "1 yr % CHANGE"
                        },
                        "rows": [
                            {
                                "oneYearPercentage": "",
                                "symbol": "DDLS",
                                "companyName": "WisdomTree Dynamic Currency Hedged International SmallCap Equi",
                                "lastSalePrice": "$34.7500",
                                "netChange": "0.4400",
                                "percentageChange": "1.2328383300644439%",
                                "deltaIndicator": "up"
                            }
                        ]
                    },
                    "message": null,
                    "status": {
                        "rCode": 200,
                        "bCodeMessage": null,
                        "developerMessage": null
                    }
                }
                """;

        UsEtfDto result = objectMapper.readValue(json, UsEtfDto.class);

        Assertions.assertEquals(UsEtfDto.builder()
                        .data(UsEtfBody.builder()
                                .rows(List.of(
                                        UsEtfItem.builder()
                                                .name("WisdomTree Dynamic Currency Hedged International SmallCap Equi")
                                                .symbol("DDLS")
                                                .build()
                                ))
                                .build())
                .build(), result);

    }

}
