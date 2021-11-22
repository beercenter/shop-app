package com.beercenter.shop.core.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ProductRequest implements Serializable {

    private ShopProductInfo product;
}
