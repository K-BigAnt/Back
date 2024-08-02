package com.bigant.gaeme.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.bigant.gaeme.dao.dto.UsStockDto.UsStockItem;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
public class UsStockDaoTest {

    @Autowired
    private UsStockDao usStockDao;

    @Test
    void 미국_주식_데이터_가져오기_테스트() {
        //given
        UsStockItem expected = UsStockItem.builder()
                .name("Apple Inc. Common Stock")
                .country("United States")
                .symbol("AAPL")
                .build();

        //when
        List<UsStockItem> result = usStockDao.getStock();

        //then
        assertTrue(result.contains(expected));
    }

}