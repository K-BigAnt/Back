package com.bigant.gaeme.dao.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
public class KrStockDtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    void krStockDto_Json_국내주식_변환_테스트() throws JsonProcessingException {
        String json = """
               {
                    "basDt": "20240724",
                    "srtnCd": "A000020",
                    "isinCd": "KR7000020008",
                    "mrktCtg": "KOSPI",
                    "itmsNm": "동화약품",
                    "crno": "1101110043870",
                    "corpNm": "동화약품(주)"
               }
                """;

        KrStockDto result = objectMapper.readValue(json, KrStockDto.class);

        assertEquals(KrStockDto.builder()
                .name("동화약품")
                .isinCode("KR7000020008")
                .symbol("A000020").build(), result);
    }

    @Test
    void krStockDto_json_국내etf_변환_테스트() throws JsonProcessingException {
        String json = """
                {
                    "basDt": "20240729",
                    "srtnCd": "091160",
                    "isinCd": "KR7091160002",
                    "itmsNm": "KODEX 반도체",
                    "clpr": "38835",
                    "vs": "160",
                    "fltRt": ".41",
                    "nav": "38925.21",
                    "mkp": "38900",
                    "hipr": "39045",
                    "lopr": "38640",
                    "trqu": "203492",
                    "trPrc": "7895569625",
                    "mrktTotAmt": "510680250000",
                    "stLstgCnt": "13150000",
                    "bssIdxIdxNm": "KRX 반도체",
                    "bssIdxClpr": "4037.2",
                    "nPptTotAmt": "513812734310"
                }
                """;

        KrStockDto result = objectMapper.readValue(json, KrStockDto.class);

        assertEquals(KrStockDto.builder()
                .name("KODEX 반도체")
                .isinCode("KR7091160002")
                .symbol("091160").build(), result);
    }

}
