package com.beercenter.shop.core.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InventoryPolicy {
    REGULAR("deny") , ONLY_AT_STORE("continue"), MANAGEMENT_SHOPIFY("Shopify");

    private String value;
}
