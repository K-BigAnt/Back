package com.bigant.gaeme.dao;

import com.bigant.gaeme.dao.dto.KrStockDto;
import java.net.URISyntaxException;
import java.util.List;

public interface StockDao {

    List<KrStockDto> getStock() throws URISyntaxException;

    List<KrStockDto> getEtf();

}
