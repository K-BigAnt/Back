package com.bigant.gaeme.repository.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum StockType {

    STOCK("stock"),
    ETF("etf");

    StockType(String name) {
        this.name = name;
    }

    private final String name;

}
