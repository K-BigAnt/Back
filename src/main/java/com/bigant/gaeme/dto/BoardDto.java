package com.bigant.gaeme.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDto {

    private UserDto user;

    private String content;

    private List<String> pictureUrls;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long likeCnt;

}
