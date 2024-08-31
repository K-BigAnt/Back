package com.bigant.gaeme.service;

import com.bigant.gaeme.dto.BoardCreateRequestDto;
import com.bigant.gaeme.dto.BoardCreateResponseDto;
import com.bigant.gaeme.dto.BoardDto;
import com.bigant.gaeme.repository.BoardRepository;
import com.bigant.gaeme.repository.BoardTreePathRepository;
import com.bigant.gaeme.repository.UserRepository;
import com.bigant.gaeme.repository.entity.Board;
import com.bigant.gaeme.repository.entity.BoardTreePath;
import com.bigant.gaeme.repository.entity.User;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    public BoardDto deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow();

        board.setDeleted(true);
        board.setUpdatedAt(LocalDateTime.now());
        return modelMapper.map(board, BoardDto.class);
    }

}
