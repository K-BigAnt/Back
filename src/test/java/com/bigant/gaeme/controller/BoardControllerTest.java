package com.bigant.gaeme.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bigant.gaeme.component.JwtBuilder;
import com.bigant.gaeme.config.InterceptorTestConfig;
import com.bigant.gaeme.dto.BoardCreateResponseDto;
import com.bigant.gaeme.dto.BoardDto;
import com.bigant.gaeme.dto.UserDto;
import com.bigant.gaeme.service.BoardService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BoardController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
@Import(InterceptorTestConfig.class)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BoardService boardService;

    @MockBean
    private JwtBuilder jwtBuilder;

    @Test
    void createBoardTest() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile("files", "test.jpg", "image/jpeg", "test".getBytes());
        MockMultipartFile content = new MockMultipartFile("content", "This is Test Descendant".getBytes());
        MockMultipartFile ancestorId = new MockMultipartFile("ancestorId", "1".getBytes());

        BDDMockito.given(boardService.createBoard(BDDMockito.any(), BDDMockito.any())).willReturn(BoardCreateResponseDto.builder()
                .ancestor(
                        BoardDto.builder()
                                .user(UserDto.builder()
                                        .id(1L)
                                        .name("testUser")
                                        .nickname("testUser")
                                        .profileImg("testImg")
                                        .address("jeju")
                                        .email("seongjkI@gmail.com")
                                        .phoneNumber("01012345678")
                                        .build())
                                .content("This is Test")
                                .likeCnt(12L)
                                .pictureUrls(List.of())
                                .updatedAt(LocalDateTime.now())
                                .createdAt(LocalDateTime.now())
                                .build()
                )
                .descendant(BoardDto.builder()
                        .user(UserDto.builder()
                                .id(2L)
                                .name("testUser2")
                                .nickname("testUser2")
                                .profileImg("testImg")
                                .address("seoul")
                                .email("gaeme@gamil.com")
                                .phoneNumber("01056788901")
                                .build())
                        .content("This is Test Descendant")
                        .likeCnt(0L)
                        .pictureUrls(List.of())
                        .updatedAt(LocalDateTime.now())
                        .createdAt(LocalDateTime.now())
                        .build()
                )
                .build());
        BDDMockito.given(jwtBuilder.decryptJwt(BDDMockito.any())).willReturn(2L);

        //when
        mvc.perform(RestDocumentationRequestBuilders.multipart("/v1/board")
                .file(file)
                .file(content)
                .file(ancestorId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(getBoardPostResultHandler());

        //then
        BDDMockito.then(boardService).should().createBoard(BDDMockito.any(), BDDMockito.any());
    }

    private RestDocumentationResultHandler getBoardPostResultHandler() {
        return document("board/post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParts(partWithName("files").description("첨부할 파일들"),
                        partWithName("content").description("게시글 내용"),
                        partWithName("ancestorId").description("부모 게시글 id"))
        );
    }

}
