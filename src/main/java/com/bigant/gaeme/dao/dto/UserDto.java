package com.bigant.gaeme.dao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private long id;

    private String name;

    private String nickname;

    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;

}
