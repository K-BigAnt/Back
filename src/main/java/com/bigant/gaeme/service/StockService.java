package com.bigant.gaeme.service;

import com.bigant.gaeme.dto.StockSearchDto;
import com.bigant.gaeme.repository.StockRepository;
import com.bigant.gaeme.repository.entity.Stock;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    private final ModelMapper modelMapper;

    public List<StockSearchDto> searchStock(String query) {
        List<Stock> stocks = stockRepository.searchByNameOrSymbol(query);

        return stocks.stream().map(stock -> modelMapper.map(stock, StockSearchDto.class)).toList();
    }

}
