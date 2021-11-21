package com.beercenter.shop.core.model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

@Data
public class ProductStockInfo {

    @CsvBindByPosition(position = 0)
    private String sku;

    @CsvBindByPosition(position = 4)
    private String description;

    @CsvBindByPosition(position = 11)
    private Long stock;
}
