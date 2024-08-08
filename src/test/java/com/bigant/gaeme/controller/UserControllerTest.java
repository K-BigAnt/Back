package com.bigant.gaeme.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bigant.gaeme.dao.dto.UserDto;
import com.bigant.gaeme.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void patchUser() throws Exception {
        UserDto dto = UserDto.builder()
                .id(1L)
                .name("sj")
                .nickname("seongjki")
                .email("abc@cde.kr")
                .phoneNumber("01012345678")
                .address("jeju")
                .build();
        BDDMockito.given(userService.patchUser(BDDMockito.any())).willReturn(dto);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/v1/user")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andDo(getUserPatchHandler());

        BDDMockito.then(userService).should().patchUser(dto);
    }

    private RestDocumentationResultHandler getUserPatchHandler() {
        return document("user/patch",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                PayloadDocumentation.requestFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("유저 아이디"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름").optional(),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임").optional(),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일").optional(),
                        fieldWithPath("phone_number").type(JsonFieldType.STRING).description("전화번호").optional(),
                        fieldWithPath("address").type(JsonFieldType.STRING).description("주소").optional()
                )
        );
    }

}
