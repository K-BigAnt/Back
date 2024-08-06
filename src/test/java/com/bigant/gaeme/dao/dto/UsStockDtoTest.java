package com.bigant.gaeme.dao.dto;

import com.bigant.gaeme.dao.dto.UsStockDto.UsStockBody;
import com.bigant.gaeme.dao.dto.UsStockDto.UsStockItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UsStockDtoTest {

    private final ObjectMapper objectMapper;

    public UsStockDtoTest() {
        this.objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    void 미국주식_Json_파싱_테스트() throws JsonProcessingException {
        String json = """
                {
                    "data": {
                        "asOf": null,
                        "headers": {
                            "symbol": "Symbol",
                            "name": "Name",
                            "lastsale": "Last Sale",
                            "netchange": "Net Change",
                            "pctchange": "% Change",
                            "marketCap": "Market Cap",
                            "country": "Country",
                            "ipoyear": "IPO Year",
                            "volume": "Volume",
                            "sector": "Sector",
                            "industry": "Industry",
                            "url": "Url"
                        },
                        "rows": [
                            {
                                "symbol": "A",
                                "name": "Agilent Technologies Inc. Common Stock",
                                "lastsale": "$142.86",
                                "netchange": "1.46",
                                "pctchange": "1.033%",
                                "volume": "2896435",
                                "marketCap": "41680958174.00",
                                "country": "United States",
                                "ipoyear": "1999",
                                "industry": "Biotechnology: Laboratory Analytical Instruments",
                                "sector": "Industrials",
                                "url": "/market-activity/stocks/a"
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

        UsStockDto result = objectMapper.readValue(json, UsStockDto.class);

        Assertions.assertEquals(UsStockDto.builder()
                .data(UsStockBody.builder().rows(
                        List.of(UsStockItem.builder()
                                .name("Agilent Technologies Inc. Common Stock")
                                .symbol("A")
                                .country("United States")
                                .build())
                ).build())
                .build(), result);

    }

}
