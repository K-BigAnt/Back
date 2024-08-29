package com.bigant.gaeme.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bigant.gaeme.config.InterceptorTestConfig;
import com.bigant.gaeme.dto.StockSearchDto;
import com.bigant.gaeme.repository.enums.StockType;
import com.bigant.gaeme.service.StockService;
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
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StockController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
@Import(InterceptorTestConfig.class)
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService stockService;

    @Test
    @WithMockUser
    void getStockPrices() throws Exception {
        //given

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/stock/price")
                .queryParam("symbol", "symbol")
                .queryParam("start_date", "2022-07-01")
                .queryParam("end_date", "2024-07-01"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(getStockPricesGetResultHandler());

        //then
    }

    RestDocumentationResultHandler getStockPricesGetResultHandler() {
        return document("stock-price/get",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("symbol").description("주식 구분 심볼"),
                        parameterWithName("start_date").description("검색 시작 일자"),
                        parameterWithName("end_date").description("검색 종료 일자")
                    )
            );
    }

    @Test
    @WithMockUser
    void searchStock() throws Exception {
        //given
        BDDMockito.given(stockService.searchStock(BDDMockito.any())).willReturn(
                List.of(StockSearchDto.builder()
                        .name("abcd")
                        .symbol("abcd")
                        .type(StockType.ETF)
                        .country("South Korea")
                        .build(),
                StockSearchDto.builder()
                        .name("erery")
                        .symbol("abcd")
                        .type(StockType.STOCK)
                        .country("China")
                        .build())
        );

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.get("/v1/stock")
                .queryParam("query", "bc"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(getStockSearchGetResultHandler());

        //then
        BDDMockito.then(stockService).should().searchStock(BDDMockito.any());
    }

    private RestDocumentationResultHandler getStockSearchGetResultHandler() {
        return document("stock-search/get",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(parameterWithName("query").description("검색어"))
        );
    }

}
