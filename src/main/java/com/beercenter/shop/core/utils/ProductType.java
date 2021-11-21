package com.beercenter.shop.core.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductType {
    BEER("Cerveza");

    private String value;
}
