package com.bigant.gaeme.controller;

import com.bigant.gaeme.component.JwtBuilder;
import com.bigant.gaeme.dto.BoardCreateRequestDto;
import com.bigant.gaeme.dto.BoardCreateResponseDto;
import com.bigant.gaeme.dto.BoardDeleteRequestDto;
import com.bigant.gaeme.dto.BoardDto;
import com.bigant.gaeme.service.BoardService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    private final JwtBuilder jwtBuilder;

    @PostMapping
    public BoardCreateResponseDto create(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestPart List<MultipartFile> files,
            @RequestPart String content,
            @RequestPart Optional<String> ancestorId) {
        return boardService.createBoard(jwtBuilder.decryptJwt(token), BoardCreateRequestDto.builder()
                        .imageFiles(files)
                        .content(content)
                        .ancestorId(ancestorId.map(Long::parseLong))
                .build());
    }

    @DeleteMapping
    public BoardDto delete(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody BoardDeleteRequestDto dto
    ) {
        return boardService.deleteBoard(dto);
    }

}
