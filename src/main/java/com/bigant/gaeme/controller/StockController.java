package com.bigant.gaeme.controller;

import com.bigant.gaeme.dao.dto.StockPriceResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/stock")
@RequiredArgsConstructor
public class StockController {

    private final ObjectMapper objectMapper;

    @GetMapping("/price")
    public StockPriceResponseDto getStockPrices(
            @RequestParam String symbol,
            @RequestParam("start_date") LocalDate startDate,
            @RequestParam("end_date") LocalDate endDate
    ) {
        String json = """
                {
                    "output1": {
                        "prdy_vrss": "70",
                        "prdy_vrss_sign": "2",
                        "prdy_ctrt": "1.13",
                        "stck_prdy_clpr": "6170",
                        "acml_vol": "3560",
                        "acml_tr_pbmn": "21982410",
                        "hts_kor_isnm": "경방",
                        "stck_prpr": "6240",
                        "stck_shrn_iscd": "000050",
                        "prdy_vol": "3752",
                        "stck_mxpr": "8020",
                        "stck_llam": "4320",
                        "stck_oprc": "6170",
                        "stck_hgpr": "6250",
                        "stck_lwpr": "6120",
                        "stck_prdy_oprc": "6130",
                        "stck_prdy_hgpr": "6200",
                        "stck_prdy_lwpr": "6020",
                        "askp": "6240",
                        "bidp": "6200",
                        "prdy_vrss_vol": "-192",
                        "vol_tnrt": "0.01",
                        "stck_fcam": "500",
                        "lstn_stcn": "27415270",
                        "cpfn": "137",
                        "hts_avls": "1711",
                        "per": "-12.71",
                        "eps": "-491.00",
                        "pbr": "0.22",
                        "itewhol_loan_rmnd_ratem name": "0.18"
                    },
                    "output2": [
                        {
                            "stck_bsop_date": "20240628",
                            "stck_clpr": "7780",
                            "stck_oprc": "7740",
                            "stck_hgpr": "8050",
                            "stck_lwpr": "7450",
                            "acml_vol": "120364",
                            "acml_tr_pbmn": "935475490",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "2",
                            "prdy_vrss": "110",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20240531",
                            "stck_clpr": "7670",
                            "stck_oprc": "7600",
                            "stck_hgpr": "8330",
                            "stck_lwpr": "7450",
                            "acml_vol": "89110",
                            "acml_tr_pbmn": "699483220",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "2",
                            "prdy_vrss": "40",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20240430",
                            "stck_clpr": "7630",
                            "stck_oprc": "8390",
                            "stck_hgpr": "8510",
                            "stck_lwpr": "7370",
                            "acml_vol": "107802",
                            "acml_tr_pbmn": "853424480",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "5",
                            "prdy_vrss": "-760",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20240329",
                            "stck_clpr": "8390",
                            "stck_oprc": "8630",
                            "stck_hgpr": "8920",
                            "stck_lwpr": "8270",
                            "acml_vol": "111622",
                            "acml_tr_pbmn": "954119990",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "5",
                            "prdy_vrss": "-270",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20240229",
                            "stck_clpr": "8660",
                            "stck_oprc": "8550",
                            "stck_hgpr": "9380",
                            "stck_lwpr": "8500",
                            "acml_vol": "74224",
                            "acml_tr_pbmn": "661085630",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "2",
                            "prdy_vrss": "100",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20240131",
                            "stck_clpr": "8560",
                            "stck_oprc": "8840",
                            "stck_hgpr": "9160",
                            "stck_lwpr": "8200",
                            "acml_vol": "132707",
                            "acml_tr_pbmn": "1165794380",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "5",
                            "prdy_vrss": "-280",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20231228",
                            "stck_clpr": "8840",
                            "stck_oprc": "8640",
                            "stck_hgpr": "9380",
                            "stck_lwpr": "8420",
                            "acml_vol": "87945",
                            "acml_tr_pbmn": "778100610",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "2",
                            "prdy_vrss": "200",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20231130",
                            "stck_clpr": "8640",
                            "stck_oprc": "7960",
                            "stck_hgpr": "8950",
                            "stck_lwpr": "7900",
                            "acml_vol": "83760",
                            "acml_tr_pbmn": "698221500",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "2",
                            "prdy_vrss": "680",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20231031",
                            "stck_clpr": "7960",
                            "stck_oprc": "8490",
                            "stck_hgpr": "9500",
                            "stck_lwpr": "7620",
                            "acml_vol": "295109",
                            "acml_tr_pbmn": "2463930970",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "5",
                            "prdy_vrss": "-520",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20230927",
                            "stck_clpr": "8480",
                            "stck_oprc": "8830",
                            "stck_hgpr": "9290",
                            "stck_lwpr": "8480",
                            "acml_vol": "516827",
                            "acml_tr_pbmn": "4609287470",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "5",
                            "prdy_vrss": "-410",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20230831",
                            "stck_clpr": "8890",
                            "stck_oprc": "9580",
                            "stck_hgpr": "9800",
                            "stck_lwpr": "8620",
                            "acml_vol": "261405",
                            "acml_tr_pbmn": "2385909810",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "5",
                            "prdy_vrss": "-720",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20230731",
                            "stck_clpr": "9610",
                            "stck_oprc": "9940",
                            "stck_hgpr": "9990",
                            "stck_lwpr": "9300",
                            "acml_vol": "139421",
                            "acml_tr_pbmn": "1336416370",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "5",
                            "prdy_vrss": "-330",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20230630",
                            "stck_clpr": "9940",
                            "stck_oprc": "10370",
                            "stck_hgpr": "10580",
                            "stck_lwpr": "9850",
                            "acml_vol": "106294",
                            "acml_tr_pbmn": "1075132000",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "5",
                            "prdy_vrss": "-430",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20230531",
                            "stck_clpr": "10370",
                            "stck_oprc": "10560",
                            "stck_hgpr": "10840",
                            "stck_lwpr": "10250",
                            "acml_vol": "61065",
                            "acml_tr_pbmn": "637497380",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "5",
                            "prdy_vrss": "-190",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20230428",
                            "stck_clpr": "10560",
                            "stck_oprc": "10710",
                            "stck_hgpr": "11220",
                            "stck_lwpr": "10360",
                            "acml_vol": "116735",
                            "acml_tr_pbmn": "1246953890",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "5",
                            "prdy_vrss": "-150",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20230331",
                            "stck_clpr": "10710",
                            "stck_oprc": "10980",
                            "stck_hgpr": "11470",
                            "stck_lwpr": "10450",
                            "acml_vol": "113617",
                            "acml_tr_pbmn": "1231297950",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "5",
                            "prdy_vrss": "-270",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20230228",
                            "stck_clpr": "10980",
                            "stck_oprc": "10720",
                            "stck_hgpr": "11200",
                            "stck_lwpr": "10650",
                            "acml_vol": "111195",
                            "acml_tr_pbmn": "1208731720",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "2",
                            "prdy_vrss": "280",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20230131",
                            "stck_clpr": "10700",
                            "stck_oprc": "10900",
                            "stck_hgpr": "11050",
                            "stck_lwpr": "10450",
                            "acml_vol": "135064",
                            "acml_tr_pbmn": "1437696520",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "5",
                            "prdy_vrss": "-100",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20221229",
                            "stck_clpr": "10800",
                            "stck_oprc": "11050",
                            "stck_hgpr": "11650",
                            "stck_lwpr": "10800",
                            "acml_vol": "98080",
                            "acml_tr_pbmn": "1088797300",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "5",
                            "prdy_vrss": "-250",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20221130",
                            "stck_clpr": "11050",
                            "stck_oprc": "10950",
                            "stck_hgpr": "11450",
                            "stck_lwpr": "10700",
                            "acml_vol": "120491",
                            "acml_tr_pbmn": "1318979450",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "2",
                            "prdy_vrss": "100",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20221031",
                            "stck_clpr": "10950",
                            "stck_oprc": "11350",
                            "stck_hgpr": "11750",
                            "stck_lwpr": "10400",
                            "acml_vol": "75556",
                            "acml_tr_pbmn": "826894800",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "5",
                            "prdy_vrss": "-500",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20220930",
                            "stck_clpr": "11450",
                            "stck_oprc": "12350",
                            "stck_hgpr": "12800",
                            "stck_lwpr": "10950",
                            "acml_vol": "113242",
                            "acml_tr_pbmn": "1359705850",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "5",
                            "prdy_vrss": "-1100",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20220831",
                            "stck_clpr": "12550",
                            "stck_oprc": "12950",
                            "stck_hgpr": "13450",
                            "stck_lwpr": "12000",
                            "acml_vol": "102439",
                            "acml_tr_pbmn": "1306507950",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "5",
                            "prdy_vrss": "-600",
                            "revl_issu_reas": ""
                        },
                        {
                            "stck_bsop_date": "20220729",
                            "stck_clpr": "13150",
                            "stck_oprc": "12300",
                            "stck_hgpr": "13950",
                            "stck_lwpr": "11500",
                            "acml_vol": "105673",
                            "acml_tr_pbmn": "1326359650",
                            "flng_cls_code": "00",
                            "prtt_rate": "0.00",
                            "mod_yn": "N",
                            "prdy_vrss_sign": "2",
                            "prdy_vrss": "900",
                            "revl_issu_reas": ""
                        }
                    ],
                    "rt_cd": "0",
                    "msg_cd": "MCA00000",
                    "msg1": "정상처리 되었습니다."
                }
                """;

        try {
            return objectMapper.readValue(json, StockPriceResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("json 변환 실패");
        }
    }

}
