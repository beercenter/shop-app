package com.beercenter.shop.core.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductStatus {
    ACTIVE("active"),
    INACTIVE("draft");

    private String value;
}
