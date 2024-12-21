package com.bigant.gaeme.service;

import com.bigant.gaeme.dto.*;
import com.bigant.gaeme.repository.BoardRepository;
import com.bigant.gaeme.repository.BoardTreePathRepository;
import com.bigant.gaeme.repository.UserRepository;
import com.bigant.gaeme.repository.entity.Board;
import com.bigant.gaeme.repository.entity.BoardTreePath;
import com.bigant.gaeme.repository.entity.User;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final BoardTreePathRepository boardTreePathRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public BoardCreateResponseDto createBoard(Long requestId, BoardCreateRequestDto dto) {
        //TODO: 이미지 파일 s3에 저장하는 로직 필요

        User user = userRepository.findById(requestId).orElseThrow();

        Board board = boardRepository.save(Board.builder()
                        .content(dto.getContent())
                        .user(user)
                        .likeCnt(0L)
                .build());

        if (dto.getAncestorId().isPresent()) {
            BoardTreePath boardTreePath = createBoardTreePath(dto.getAncestorId().get(), board);

            return BoardCreateResponseDto.builder()
                    .ancestor(modelMapper.map(boardTreePath.getAncestor(), BoardDto.class))
                    .descendant(modelMapper.map(boardTreePath.getDescendant(), BoardDto.class))
                    .build();
        }

        return BoardCreateResponseDto.builder()
                .descendant(modelMapper.map(board, BoardDto.class))
                .build();
    }

    private BoardTreePath createBoardTreePath(Long ancestorId, Board board) {
        Board ancestor = boardRepository.findById(ancestorId).orElseThrow();
        BoardTreePath boardTreePath = BoardTreePath.builder()
                .ancestor(ancestor)
                .descendant(board)
                .build();

        return boardTreePathRepository.save(boardTreePath);
    }

    @Transactional
    public BoardDto deleteBoard(Long requestId, BoardDeleteRequestDto dto) {
        User user = userRepository.findById(requestId).orElseThrow();
        Board board = boardRepository.findById(dto.getBoardId()).orElseThrow();

        if (board.getUser() != user) {
            throw new IllegalArgumentException("요청을 보낸 유저가 해당 보드의 글쓴이가 아닙니다.");
        }

        board.setDeleted(true);
        board.setUpdatedAt(LocalDateTime.now());
        return modelMapper.map(board, BoardDto.class);
    }

    public Slice<BoardDto> readDefault(Pageable pageable, Optional<Long> ancestorId) {
        if (ancestorId.isPresent()) {
            return readDescendant(pageable, ancestorId.get());
        }
        return recentBoard(pageable);
    }

    public Slice<BoardDto> readMine(Pageable pageable, Long requestId) {
        Slice<Board> boards = boardRepository.findAllByUser_Id(requestId, pageable);

        return new SliceImpl<>(boards.stream().map(board -> modelMapper.map(board, BoardDto.class)).toList(), pageable, boards.hasNext());
    }

    private Slice<BoardDto> readDescendant(Pageable pageable, Long ancestorId) {
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Slice<BoardTreePath> boardTreePaths = boardTreePathRepository.findAllByAncestor_Id(ancestorId, newPageable);

        return new SliceImpl<>(boardTreePaths.map(boardTreePath -> modelMapper.map(boardTreePath.getDescendant(), BoardDto.class)).toList(),
                newPageable,
                boardTreePaths.hasNext());
    }

    private Slice<BoardDto> recentBoard(Pageable pageable) {
        Slice<Board> boards = boardRepository.findAllBy(pageable);

        return new SliceImpl<>(boards.stream().map(board -> modelMapper.map(board, BoardDto.class)).toList(), pageable, boards.hasNext());
    }

    @Transactional
    public BoardDto update(Long requesterId, BoardUpdateRequestDto dto) {
        Board board = boardRepository.findById(dto.getId()).orElseThrow();

        if (!Objects.equals(requesterId, board.getUser().getId())) {
            throw new IllegalArgumentException("해당 리소스에 접근 권한이 없습니다.");
        }

        board.update(dto);
        return modelMapper.map(board, BoardDto.class);
    }

}
