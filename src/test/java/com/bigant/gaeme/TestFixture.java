package com.bigant.gaeme;

import com.bigant.gaeme.repository.entity.Board;
import com.bigant.gaeme.repository.entity.User;
import java.util.List;

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

}
