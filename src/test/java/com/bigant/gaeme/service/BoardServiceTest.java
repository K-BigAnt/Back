package com.bigant.gaeme.service;

import com.bigant.gaeme.TestFixture;
import com.bigant.gaeme.dto.BoardCreateRequestDto;
import com.bigant.gaeme.dto.BoardCreateResponseDto;
import com.bigant.gaeme.dto.BoardDto;
import com.bigant.gaeme.dto.UserDto;
import com.bigant.gaeme.modelmapper.BoardToResponseDtoConverter;
import com.bigant.gaeme.repository.BoardRepository;
import com.bigant.gaeme.repository.BoardTreePathRepository;
import com.bigant.gaeme.repository.UserRepository;
import com.bigant.gaeme.repository.entity.Board;
import com.bigant.gaeme.repository.entity.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardServiceTest {

    private BoardRepository boardRepository;

    private BoardTreePathRepository boardTreePathRepository;

    private UserRepository userRepository;

    private BoardService boardService;

    @Autowired
    public BoardServiceTest(BoardRepository boardRepository, BoardTreePathRepository boardTreePathRepository, UserRepository userRepository) {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(new BoardToResponseDtoConverter());
        this.boardRepository = boardRepository;
        this.boardTreePathRepository = boardTreePathRepository;
        this.userRepository = userRepository;
        this.boardService = new BoardService(boardRepository, boardTreePathRepository, userRepository, modelMapper);
    }

    @Test
    void 루트_보드_생성_성공() {
        //given
        User testUser = TestFixture.getTestUser();
        userRepository.save(testUser);
        BoardCreateRequestDto dto = BoardCreateRequestDto.builder()
                .ancestorId(Optional.empty())
                .content("This is Test")
                .imageFiles(List.of())
                .build();

        //when
        BoardCreateResponseDto result = boardService.createBoard(testUser.getId(), dto);

        //then
        Assertions.assertEquals(BoardCreateResponseDto.builder()
                        .descendant(BoardDto.builder()
                                .user(UserDto.builder()
                                        .id(testUser.getId())
                                        .name(testUser.getName())
                                        .nickname(testUser.getNickname())
                                        .profileImg(testUser.getProfileImg())
                                        .address(testUser.getAddress())
                                        .email(testUser.getEmail())
                                        .phoneNumber(testUser.getPhoneNumber())
                                        .build())
                                .content(dto.getContent())
                                .likeCnt(0L)
                                .pictureUrls(List.of())
                                .updatedAt(result.getDescendant().getUpdatedAt())
                                .createdAt(result.getDescendant().getCreatedAt())
                                .build()
                        )
                .build(), result);
    }

    @Test
    void 자식_보드_생성_성공() {
        //given
        User testUser = TestFixture.getTestUser();
        userRepository.save(testUser);
        Board testBoard = TestFixture.getTestBoard(testUser);
        boardRepository.save(testBoard);

        BoardCreateRequestDto dto = BoardCreateRequestDto.builder()
                .ancestorId(Optional.of(testBoard.getId()))
                .content("This is Test Descendant")
                .imageFiles(List.of())
                .build();

        //when
        BoardCreateResponseDto result = boardService.createBoard(testUser.getId(), dto);

        //then
        Assertions.assertEquals(BoardCreateResponseDto.builder()
                .ancestor(
                        BoardDto.builder()
                                .user(UserDto.builder()
                                        .id(testUser.getId())
                                        .name(testUser.getName())
                                        .nickname(testUser.getNickname())
                                        .profileImg(testUser.getProfileImg())
                                        .address(testUser.getAddress())
                                        .email(testUser.getEmail())
                                        .phoneNumber(testUser.getPhoneNumber())
                                        .build())
                                .content("This is Test")
                                .likeCnt(12L)
                                .pictureUrls(List.of())
                                .updatedAt(testBoard.getUpdatedAt())
                                .createdAt(testBoard.getCreatedAt())
                                .build()
                )
                .descendant(BoardDto.builder()
                        .user(UserDto.builder()
                                .id(testUser.getId())
                                .name(testUser.getName())
                                .nickname(testUser.getNickname())
                                .profileImg(testUser.getProfileImg())
                                .address(testUser.getAddress())
                                .email(testUser.getEmail())
                                .phoneNumber(testUser.getPhoneNumber())
                                .build())
                        .content("This is Test Descendant")
                        .likeCnt(0L)
                        .pictureUrls(List.of())
                        .updatedAt(result.getDescendant().getUpdatedAt())
                        .createdAt(result.getDescendant().getCreatedAt())
                        .build()
                )
                .build(), result);
    }

}
