package com.bigant.gaeme.dao;

import com.bigant.gaeme.dao.dto.StockDto;
import java.util.List;

public interface StockDao<T extends StockDto> {

    List<T> getStock();

    List<T> getEtf();

}
