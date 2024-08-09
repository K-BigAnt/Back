package com.bigant.gaeme.service;

import com.bigant.gaeme.dao.dto.UserDto;
import com.bigant.gaeme.repository.UserRepository;
import com.bigant.gaeme.repository.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {

    private UserRepository userRepository;

    private UserService userService;

    @Autowired
    public UserServiceTest(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userService = new UserService(userRepository);
    }

    @Test
    void 유저정보_수정_성공() {
        //given
        User user = userRepository.save(User.builder()
                        .oauthId("abc")
                .build());
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .nickname("seongjki")
                .name("sj")
                .phoneNumber("01012345678")
                .email("abc@cde.kr")
                .address("jeju")
                .build();

        //when
        UserDto result = userService.patchUser(userDto);

        //then
        Assertions.assertEquals(UserDto.builder()
                        .id(user.getId())
                        .nickname(user.getNickname())
                        .name(user.getName())
                        .phoneNumber(user.getPhoneNumber())
                        .email(user.getEmail())
                        .address(user.getAddress())
                .build(), result);
    }

}
