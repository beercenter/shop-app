package com.beercenter.shop.core.model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

@Data
public class ProductStockInfo {

    @CsvBindByPosition(position = 1)
    private String sku;

    @CsvBindByPosition(position = 3)
    private String description;

    @CsvBindByPosition(position = 4)
    private Long stock;
}
