package com.bigant.gaeme.repository.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bigant.gaeme.repository.enums.StockType;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
public class KrStockTest {

    @Test
    void krStock_생성_테스트() {
        //given, when
        KrStock result = KrStock.builder().id(1L).isDelisting(true).isinCode("abc").symbol("1234").name("name")
                .type(StockType.ETF)
                .build();

        //then
        assertEquals(KrStock.builder().id(1L).isDelisting(true).isinCode("abc").symbol("1234").name("name")
                .type(StockType.ETF)
                .build(), result);
    }

}
