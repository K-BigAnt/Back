package com.bigant.gaeme.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bigant.gaeme.dao.dto.CreatePortfolioRequestDto;
import com.bigant.gaeme.service.PortfolioService;
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
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebMvcTest(PortfolioController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
public class PortfolioControllerTest {

    @MockBean
    private PortfolioService portfolioService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPortfolioTest() throws Exception {
        //given
        List<CreatePortfolioRequestDto> dtos = List.of(
                CreatePortfolioRequestDto.builder()
                        .name("port1")
                        .stocks(List.of(
                                CreatePortfolioRequestDto.SimpleStockDto.builder()
                                        .symbol("stock1")
                                        .rate(10)
                                        .build(),
                                CreatePortfolioRequestDto.SimpleStockDto.builder()
                                        .symbol("stock2")
                                        .rate(20)
                                        .build()
                        )).build(),
                CreatePortfolioRequestDto.builder()
                        .name("port2")
                        .stocks(List.of(
                                CreatePortfolioRequestDto.SimpleStockDto.builder()
                                        .symbol("stock1")
                                        .rate(30)
                                        .build(),
                                CreatePortfolioRequestDto.SimpleStockDto.builder()
                                        .symbol("stock3")
                                        .rate(40)
                                        .build()
                        )).build()
        );
        BDDMockito.given(portfolioService.createPortfolio(BDDMockito.anyList())).willReturn(List.of(1L, 2L));

        //when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/v1/portfolio")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(dtos)))
                .andExpect(status().isOk())
                .andDo(getPortfolioPostResultHandler())
                .andDo(print());

    }

    RestDocumentationResultHandler getPortfolioPostResultHandler() {
        return document("portfolio/post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(fieldWithPath("[].name").type(JsonFieldType.STRING).description("포트폴리오 이름"),
                        fieldWithPath("[].stocks.[]").type(JsonFieldType.ARRAY).description("주식 리스트"),
                        fieldWithPath("[].stocks.[].symbol").type(JsonFieldType.STRING).description("주식 심볼"),
                        fieldWithPath("[].stocks.[].rate").type(JsonFieldType.NUMBER).description("주식 비율"))
            );
    }

}
