package com.bigant.gaeme.modelmapper;

import com.bigant.gaeme.dto.BoardDto;
import com.bigant.gaeme.dto.UserDto;
import com.bigant.gaeme.repository.entity.Board;
import java.util.List;
import org.modelmapper.AbstractConverter;

public class BoardToResponseDtoConverter extends AbstractConverter<Board, BoardDto> {
    @Override
    protected BoardDto convert(Board board) {
        return BoardDto.builder()
                .user(UserDto.builder()
                        .id(board.getUser().getId())
                        .address(board.getUser().getAddress())
                        .email(board.getUser().getEmail())
                        .name(board.getUser().getName())
                        .nickname(board.getUser().getNickname())
                        .phoneNumber(board.getUser().getPhoneNumber())
                        .profileImg(board.getUser().getProfileImg())
                        .build()
                )
                .content(board.getContent())
                .likeCnt(board.getLikeCnt())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .pictureUrls(List.of())
                .build();
    }
}
