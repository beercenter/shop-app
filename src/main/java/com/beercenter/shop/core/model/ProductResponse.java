package com.beercenter.shop.core.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductResponse implements Serializable {

    private List<ShopProductInfo> products = new ArrayList<>();
}
