package com.bigant.gaeme.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bigant.gaeme.TestFixture;
import com.bigant.gaeme.component.JwtBuilder;
import com.bigant.gaeme.config.InterceptorTestConfig;
import com.bigant.gaeme.dto.*;
import com.bigant.gaeme.repository.entity.User;
import com.bigant.gaeme.service.BoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BoardController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(uriPort = 80)
@AutoConfigureDataJpa
@ExtendWith(RestDocumentationExtension.class)
@Import(InterceptorTestConfig.class)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BoardService boardService;

    @MockBean
    private JwtBuilder jwtBuilder;

    @Autowired
    private ObjectMapper objectMapper;

    private ModelMapper modelMapper = new ModelMapper();

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

    @Test
    void boardDeleteTest() throws Exception {
        //given
        User testUser = TestFixture.getTestUser();
        BoardDeleteRequestDto dto = BoardDeleteRequestDto.builder().boardId(1L).build();
        BDDMockito.given(jwtBuilder.decryptJwt(BDDMockito.any())).willReturn(testUser.getId());
        BDDMockito.given(boardService.deleteBoard(BDDMockito.any(), BDDMockito.any())).willReturn(BoardDto.builder()
                        .isDeleted(true)
                        .user(UserDto.builder()
                                .address(testUser.getAddress())
                                .phoneNumber(testUser.getPhoneNumber())
                                .profileImg(testUser.getProfileImg())
                                .nickname(testUser.getNickname())
                                .name(testUser.getName())
                                .email(testUser.getEmail())
                                .id(1L)
                                .build())
                        .content("content")
                        .createdAt(LocalDateTime.of(2024, 9, 1, 7, 14))
                        .likeCnt(12L)
                        .pictureUrls(List.of())
                        .updatedAt(LocalDateTime.of(2024, 9, 5, 7, 14))
                        .build()
        );

        //when
        mvc.perform(RestDocumentationRequestBuilders.delete("/v1/board")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(getBoardDeleteResultHandler());

        //then
        BDDMockito.then(boardService).should().deleteBoard(BDDMockito.any(), BDDMockito.any());
    }

    RestDocumentationResultHandler getBoardDeleteResultHandler() {
        return document("board/delete",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("삭제할 게시글의 아이디"))
        );
    }

    @Test
    void boardReadTest() throws Exception {
        //given
        User testUser = TestFixture.getTestUser();
        BDDMockito.given(boardService.readDefault(BDDMockito.any(), BDDMockito.any())).willReturn(
                new SliceImpl<>(
                        List.of(
                                BoardDto.builder()
                                        .user(modelMapper.map(testUser, UserDto.class))
                                        .id(2L)
                                        .content("cccccc")
                                        .likeCnt(0L)
                                        .createdAt(LocalDateTime.now())
                                        .updatedAt(LocalDateTime.now())
                                        .isDeleted(false)
                                        .pictureUrls(List.of())
                                        .build()
                        ),
                        PageRequest.of(1, 10, Sort.Direction.DESC, "createdAt"),
                        false
                )
        );

        //when
        mvc.perform(get("/v1/board")
                .param("pageNum", "1")
                .param("ancestorId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(getBoardGetResultHandler());

        //then
        BDDMockito.then(boardService).should().readDefault(BDDMockito.any(), BDDMockito.any());
    }

    private RestDocumentationResultHandler getBoardGetResultHandler() {
        return document("board/get",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(parameterWithName("pageNum").description("조회할 페이지 번호"),
                        parameterWithName("ancestorId").description("댓글 조회시 부모 보드 아이디"))
        );
    }

    @Test
    void updateBoardTest() throws Exception {
        //given
        User user = TestFixture.getTestUser();
        user.setId(1L);
        BoardUpdateRequestDto request = BoardUpdateRequestDto.builder()
                .id(user.getId())
                .likeCnt(100L)
                .content("update test")
                .build();
        BDDMockito.given(boardService.update(BDDMockito.any(), BDDMockito.any())).willReturn(
                BoardDto.builder()
                        .id(1L)
                        .user(modelMapper.map(user, UserDto.class))
                        .updatedAt(LocalDateTime.now())
                        .createdAt(LocalDateTime.now())
                        .isDeleted(false)
                        .pictureUrls(List.of())
                        .content("update test")
                        .likeCnt(100L)
                        .build()
        );

        //when
        mvc.perform(RestDocumentationRequestBuilders.patch("/v1/board")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(getBoardPatchResultHandler());

        //then
        BDDMockito.then(boardService).should().update(BDDMockito.any(), BDDMockito.any());
    }

    RestDocumentationResultHandler getBoardPatchResultHandler() {
        return document("board/patch",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 아이디"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
                        fieldWithPath("likeCnt").type(JsonFieldType.NUMBER).description("게시글 좋아요 수")
                )
        );
    }

}
