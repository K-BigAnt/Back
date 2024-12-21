package com.bigant.gaeme.controller;

import com.bigant.gaeme.component.JwtBuilder;
import com.bigant.gaeme.dto.*;
import com.bigant.gaeme.service.BoardService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
        return boardService.deleteBoard(jwtBuilder.decryptJwt(token), dto);
    }

    @GetMapping
    public Slice<BoardDto> read(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Optional<Long> ancestorId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) Optional<String> token
    ) {
        if (token.isPresent()) {
            return boardService.readMine(pageable, ancestorId, jwtBuilder.decryptJwt(token.get()));
        }
        return boardService.readDefault(pageable, ancestorId);
    }

    @PatchMapping
    public BoardDto update(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody BoardUpdateRequestDto dto
    ) {
        return boardService.update(jwtBuilder.decryptJwt(token), dto);
    }

}
