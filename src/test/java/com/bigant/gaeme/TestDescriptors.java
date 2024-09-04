package com.bigant.gaeme;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import java.util.List;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class TestDescriptors {

    public static List<FieldDescriptor> getUserDescriptors() {
        return List.of(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("유저 아이디"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("이름").optional(),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임").optional(),
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일").optional(),
                fieldWithPath("phone_number").type(JsonFieldType.STRING).description("전화번호").optional(),
                fieldWithPath("address").type(JsonFieldType.STRING).description("주소").optional(),
                fieldWithPath("profileImg").type(JsonFieldType.STRING).description("프로필 이미지 주소").optional()
        );
    }

}
