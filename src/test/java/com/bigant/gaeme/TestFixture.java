package com.bigant.gaeme;

import com.bigant.gaeme.repository.entity.*;
import java.util.List;
import java.util.Random;

public class TestFixture {

    public static User getTestUser() {
        return User.builder()
                .name("tester")
                .nickname("tester")
                .address("jeju")
                .email("seongjki@gmail.com")
                .phoneNumber("01042005063")
                .profileImg("img")
                .build();
    }

    public static Board getTestBoard(User user) {
        return Board.builder()
                .user(user)
                .likeCnt(12L)
                .content("This is Test")
                .build();
    }

    public static Portfolio getTestPortfolio(User user) {
        return Portfolio.builder()
                .user(user)
                .name("test-portfolio")
                .build();
    }

    public static Stock getTestStock() {
        Random random = new Random();
        int randomInt = random.nextInt();
        String name = "stock" + randomInt;

        if (randomInt % 2 == 0) {
            return KrStock.builder()
                    .name(name)
                    .symbol(name)
                    .isDelisting(false)
                    .isinCode(name)
                    .build();
        }
        return UsStock.builder()
                .name(name)
                .symbol(name)
                .country("United State")
                .isDelisting(false)
                .build();
    }
}
