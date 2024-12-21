package com.bigant.gaeme.service;

import com.bigant.gaeme.TestFixture;
import com.bigant.gaeme.dto.*;
import com.bigant.gaeme.modelmapper.BoardToResponseDtoConverter;
import com.bigant.gaeme.repository.BoardRepository;
import com.bigant.gaeme.repository.BoardTreePathRepository;
import com.bigant.gaeme.repository.UserRepository;
import com.bigant.gaeme.repository.entity.Board;
import com.bigant.gaeme.repository.entity.BoardTreePath;
import com.bigant.gaeme.repository.entity.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardServiceTest {

    private BoardRepository boardRepository;

    private BoardTreePathRepository boardTreePathRepository;

    private UserRepository userRepository;

    private BoardService boardService;

    private ModelMapper modelMapper;

    @Autowired
    public BoardServiceTest(BoardRepository boardRepository, BoardTreePathRepository boardTreePathRepository, UserRepository userRepository) {
        this.modelMapper = new ModelMapper();
        this.modelMapper.addConverter(new BoardToResponseDtoConverter());
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
                                .id(result.getDescendant().getId())
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
                                .id(result.getAncestor().getId())
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
                        .id(result.getDescendant().getId())
                        .content("This is Test Descendant")
                        .likeCnt(0L)
                        .pictureUrls(List.of())
                        .updatedAt(result.getDescendant().getUpdatedAt())
                        .createdAt(result.getDescendant().getCreatedAt())
                        .build()
                )
                .build(), result);
    }

    @Test
    void 보드_삭제_성공() {
        //given
        User testUser = TestFixture.getTestUser();
        userRepository.save(testUser);
        Board testBoard = TestFixture.getTestBoard(testUser);
        boardRepository.save(testBoard);

        //when
        BoardDto result = boardService.deleteBoard(testUser.getId(), BoardDeleteRequestDto.builder()
                .boardId(testBoard.getId())
                .build());

        //then
        Assertions.assertEquals(BoardDto.builder()
                        .isDeleted(true)
                        .user(UserDto.builder()
                                .address(testUser.getAddress())
                                .phoneNumber(testUser.getPhoneNumber())
                                .profileImg(testUser.getProfileImg())
                                .nickname(testUser.getNickname())
                                .name(testUser.getName())
                                .email(testUser.getEmail())
                                .id(testUser.getId())
                                .build())
                        .id(testBoard.getId())
                        .content(testBoard.getContent())
                        .createdAt(testBoard.getCreatedAt())
                        .likeCnt(12L)
                        .pictureUrls(List.of())
                        .updatedAt(testBoard.getUpdatedAt())
                .build(), result);
    }

    @Test
    void 보드_최신순_조회_성공() {
        //given
        User testUser = TestFixture.getTestUser();
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
        userRepository.save(testUser);
        boardRepository.save(Board.builder()
                                .user(testUser)
                                .content("aaaaa")
                                .likeCnt(0L)
                                .build());
        boardRepository.save(Board.builder()
                        .user(testUser)
                        .content("bbbbb")
                        .likeCnt(0L)
                        .build());
        boardRepository.save(Board.builder()
                        .user(testUser)
                        .content("cccccc")
                        .likeCnt(0L)
                        .build());

        //when
        Slice<BoardDto> result = boardService.readDefault(pageable, Optional.empty());

        //then
        Assertions.assertEquals(List.of(
                BoardDto.builder()
                        .user(modelMapper.map(testUser, UserDto.class))
                        .id(result.getContent().get(0).getId())
                        .createdAt(result.getContent().get(0).getCreatedAt())
                        .updatedAt(result.getContent().get(0).getUpdatedAt())
                        .content("cccccc")
                        .likeCnt(0L)
                        .pictureUrls(List.of())
                        .isDeleted(false)
                        .build(),
                BoardDto.builder()
                        .user(modelMapper.map(testUser, UserDto.class))
                        .id(result.getContent().get(1).getId())
                        .createdAt(result.getContent().get(1).getCreatedAt())
                        .updatedAt(result.getContent().get(1).getUpdatedAt())
                        .content("bbbbb")
                        .likeCnt(0L)
                        .pictureUrls(List.of())
                        .isDeleted(false)
                        .build(),
                BoardDto.builder()
                        .user(modelMapper.map(testUser, UserDto.class))
                        .id(result.getContent().get(2).getId())
                        .createdAt(result.getContent().get(2).getCreatedAt())
                        .updatedAt(result.getContent().get(2).getUpdatedAt())
                        .content("aaaaa")
                        .likeCnt(0L)
                        .pictureUrls(List.of())
                        .isDeleted(false)
                        .build()
                ), result.getContent());
    }

    @Test
    void 보드_댓글_조회_성공() {
        //given
        User testUser = TestFixture.getTestUser();
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
        userRepository.save(testUser);
        boardRepository.save(Board.builder()
                .user(testUser)
                .content("aaaaa")
                .likeCnt(0L)
                .build());
        Board b = boardRepository.save(Board.builder()
                .user(testUser)
                .content("bbbbb")
                .likeCnt(0L)
                .build());
        Board c = boardRepository.save(Board.builder()
                .user(testUser)
                .content("cccccc")
                .likeCnt(0L)
                .build());
        boardTreePathRepository.save(BoardTreePath.builder()
                        .ancestor(b)
                        .descendant(c)
                .build());

        //when
        Slice<BoardDto> result = boardService.readDefault(pageable, Optional.of(b.getId()));

        //then
        Assertions.assertEquals(List.of(
                BoardDto.builder()
                        .user(modelMapper.map(testUser, UserDto.class))
                        .id(c.getId())
                        .content("cccccc")
                        .likeCnt(0L)
                        .createdAt(c.getCreatedAt())
                        .updatedAt(c.getUpdatedAt())
                        .isDeleted(false)
                        .pictureUrls(List.of())
                        .build()
        ), result.getContent());
    }

    @Test
    void 보드_업데이트_성공() {
        //given

        User user = TestFixture.getTestUser();
        Board board = TestFixture.getTestBoard(user);

        userRepository.save(user);
        boardRepository.save(board);

        BoardUpdateRequestDto request = BoardUpdateRequestDto.builder()
                .id(board.getId())
                .content("update")
                .likeCnt(100L)
                .build();

        //when
        BoardDto result = boardService.update(user.getId(), request);

        //then
        Assertions.assertEquals(result, BoardDto.builder()
                        .createdAt(result.getCreatedAt())
                        .updatedAt(result.getUpdatedAt())
                        .pictureUrls(List.of())
                        .likeCnt(100L)
                        .content("update")
                        .id(result.getId())
                        .isDeleted(false)
                        .user(modelMapper.map(user, UserDto.class))
                .build());
    }

    @Test
    void 본인_게시글_조회_성공() {
        //given
        User mine = TestFixture.getTestUser();
        User another = TestFixture.getTestUser();
        another.setName("tester2");
        Board testBoard = TestFixture.getTestBoard(another);
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");
        userRepository.save(mine);
        userRepository.save(another);
        boardRepository.save(Board.builder()
                .user(mine)
                .content("aaaaa")
                .likeCnt(0L)
                .build());
        boardRepository.save(Board.builder()
                .user(mine)
                .content("bbbbb")
                .likeCnt(0L)
                .build());
        boardRepository.save(Board.builder()
                .user(another)
                .content("cccccc")
                .likeCnt(0L)
                .build());
        boardRepository.save(testBoard);

        //when
        Slice<BoardDto> result = boardService.readMine(pageable, mine.getId());

        //then
        Assertions.assertEquals(List.of(
                BoardDto.builder()
                        .user(modelMapper.map(mine, UserDto.class))
                        .id(result.getContent().get(0).getId())
                        .createdAt(result.getContent().get(0).getCreatedAt())
                        .updatedAt(result.getContent().get(0).getUpdatedAt())
                        .content("bbbbb")
                        .likeCnt(0L)
                        .pictureUrls(List.of())
                        .isDeleted(false)
                        .build(),
                BoardDto.builder()
                        .user(modelMapper.map(mine, UserDto.class))
                        .id(result.getContent().get(1).getId())
                        .createdAt(result.getContent().get(1).getCreatedAt())
                        .updatedAt(result.getContent().get(1).getUpdatedAt())
                        .content("aaaaa")
                        .likeCnt(0L)
                        .pictureUrls(List.of())
                        .isDeleted(false)
                        .build()
        ), result.getContent());
    }

}
