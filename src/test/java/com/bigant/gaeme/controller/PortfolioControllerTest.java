package com.bigant.gaeme.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bigant.gaeme.component.JwtBuilder;
import com.bigant.gaeme.config.InterceptorTestConfig;
import com.bigant.gaeme.dto.*;
import com.bigant.gaeme.service.PortfolioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PortfolioController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(uriPort = 80)
@AutoConfigureDataJpa
@ExtendWith(RestDocumentationExtension.class)
@Import(InterceptorTestConfig.class)
public class PortfolioControllerTest {

    @MockBean
    private PortfolioService portfolioService;

    @MockBean
    private JwtBuilder jwtBuilder;

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

    @Test
    void getBacktestTest() throws Exception {
        //given
        BacktestRequestDto request = BacktestRequestDto.builder()
                .startDate(LocalDate.of(2023, 10, 1))
                .endDate(LocalDate.of(2024, 11, 1))
                .portfolio(PortfolioDto.builder()
                        .name("test")
                        .stocks(List.of(
                                PortfolioDto.PortfolioStockDto.builder()
                                        .symbol("APPL")
                                        .rate(100)
                                        .build()
                        ))
                        .build())
                .initialAmount(10000L)
                .build();

        BDDMockito.given(portfolioService.backtest(BDDMockito.any())).willReturn(
                BacktestResponseDto.builder()
                        .result(List.of(
                                BacktestDto.builder()
                                        .stock(PortfolioDto.PortfolioStockDto.builder()
                                                .symbol("APPL")
                                                .rate(100)
                                                .build())
                                        .earns(
                                                List.of(
                                                        BacktestDto.BacktestPriceDto.builder()
                                                                .date(LocalDate.of(2023, 11, 30))
                                                                .amount(10000L)
                                                                .build(),
                                                        BacktestDto.BacktestPriceDto.builder()
                                                                .date(LocalDate.of(2023, 12, 31))
                                                                .amount(12000L)
                                                                .build(),
                                                        BacktestDto.BacktestPriceDto.builder()
                                                                .date(LocalDate.of(2024, 1, 31))
                                                                .amount(14000L)
                                                                .build()
                                                )
                                        )
                                        .build()
                        )).build()
        );


        //when
        mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/portfolio")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(getBacktestGetResultHandler())
                .andDo(print());
    }

    RestDocumentationResultHandler getBacktestGetResultHandler() {
        return document("portfolio/get",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(fieldWithPath("portfolio").type(JsonFieldType.OBJECT).description("포트폴리오"),
                        fieldWithPath("portfolio.name").type(JsonFieldType.STRING).description("포트폴리오 이름"),
                        fieldWithPath("portfolio.stocks").type(JsonFieldType.ARRAY).description("보유 주식"),
                        fieldWithPath("portfolio.deleted").type(JsonFieldType.BOOLEAN).description("삭제 여부"),
                        fieldWithPath("portfolio.stocks.[].symbol").type(JsonFieldType.STRING).description("주식 심볼"),
                        fieldWithPath("portfolio.stocks.[].rate").type(JsonFieldType.NUMBER).description("주식 비중"),
                        fieldWithPath("startDate").type(JsonFieldType.STRING).description("시작 날짜"),
                        fieldWithPath("endDate").type(JsonFieldType.STRING).description("종료 날짜"),
                        fieldWithPath("initialAmount").type(JsonFieldType.NUMBER).description("초기 투자금"),
                        fieldWithPath("rebalanced").type(JsonFieldType.BOOLEAN).description("리밸런싱 여부")
                )
        );
    }

    @Test
    void getMine() throws Exception {
        //given
        BDDMockito.given(portfolioService.getMine(BDDMockito.any())).willReturn(
                List.of(
                        PortfolioDto.builder()
                                .name("test-portfolio")
                                .stocks(List.of(
                                        PortfolioDto.PortfolioStockDto.builder()
                                                .symbol("test")
                                                .rate(100)
                                                .build()
                                ))
                                .build()
                )
        );

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/portfolio?type=my")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(getMyPortfolioGetHandler());

        //then
        BDDMockito.then(portfolioService).should().getMine(BDDMockito.any());
    }

    RestDocumentationResultHandler getMyPortfolioGetHandler() {
        return document("my-portfolio/get",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
        );
    }

    @Test
    void delete() throws Exception {
        //given
        BDDMockito.given(portfolioService.delete(BDDMockito.any(), BDDMockito.any())).willReturn(
            PortfolioDto.builder()
                    .name("test-portfolio")
                    .stocks(List.of())
                    .isDeleted(true)
                    .build()
        );

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/v1/portfolio?id=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(getPortfolioDeleteHandler());

        //then
        BDDMockito.then(portfolioService).should().delete(BDDMockito.any(), BDDMockito.any());
    }

    RestDocumentationResultHandler getPortfolioDeleteHandler() {
        return document("portfolio/delete",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
        );
    }

}
