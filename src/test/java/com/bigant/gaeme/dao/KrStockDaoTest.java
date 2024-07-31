package com.bigant.gaeme.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.bigant.gaeme.dao.dto.KrStockDto;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SuppressWarnings("NonAsciiCharacters")
@Disabled
@SpringBootTest
public class KrStockDaoTest {

    @Autowired
    private KrStockDao krStockDao;

    @Test
    void 한국_주식_조회_테스트() {
        //given
        KrStockDto stock = KrStockDto.builder()
                .isinCode("KR7000020008")
                .name("동화약품")
                .shortenCode("A000020")
                .build();

        //when
        List<KrStockDto> result = krStockDao.getStock();

        //then
        assertTrue(result.contains(stock));
    }

    @Test
    void 한국_etf_조회_테스트() {
        //given
        KrStockDto etf = KrStockDto.builder()
                .isinCode("KR7069500007")
                .name("KODEX 200")
                .shortenCode("069500")
                .build();

        //when
        List<KrStockDto> result = krStockDao.getEtf();

        //then
        assertTrue(result.contains(etf));
    }

}
