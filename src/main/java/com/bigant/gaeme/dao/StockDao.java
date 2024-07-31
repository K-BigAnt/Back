package com.bigant.gaeme.dao;

import com.bigant.gaeme.dao.dto.KrStockDto;
import java.util.List;

public interface StockDao {

    List<KrStockDto> getStock();

    List<KrStockDto> getEtf();

}
