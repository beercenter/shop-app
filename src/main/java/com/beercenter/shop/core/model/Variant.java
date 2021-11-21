package com.beercenter.shop.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Variant implements Serializable {

    public Long id;
    public Long product_id;
    public String title;
    public String price;
    public String sku;
    public Long position;
    public String inventory_policy;
    public String compare_at_price;
    public String fulfillment_service;
    public String inventory_management;
    public String option1;
    public String option2;
    public String option3;
    public Date created_at;
    public Date updated_at;
    public Boolean taxable;
    public String barcode;
    public Long grams;
    public Long image_id;
    public Double weight;
    public String weight_unit;
    public Long inventory_item_id;
    public Long inventory_quantity;
    public Boolean requires_shipping;
    public String admin_graphql_api_id;
}
