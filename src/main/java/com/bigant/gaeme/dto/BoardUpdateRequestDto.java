package com.bigant.gaeme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardUpdateRequestDto {

    private Long id;

    private String content;

    private Long likeCnt;

}
