package com.bigant.gaeme.controller;

import com.bigant.gaeme.dao.dto.UserDto;
import com.bigant.gaeme.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping
    public UserDto patchUser(@RequestBody UserDto dto) {

        return userService.patchUser(dto);

    }

}
