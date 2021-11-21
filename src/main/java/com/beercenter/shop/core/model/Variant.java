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

    private Long id;
    private Long product_id;
    private String title;
    private String price;
    private String sku;
    private Long position;
    private String inventory_policy;
    private String compare_at_price;
    private String fulfillment_service;
    private String inventory_management;
    private String option1;
    private String option2;
    private String option3;
    private Date created_at;
    private Date updated_at;
    private Boolean taxable;
    private String barcode;
    private Long grams;
    private Long image_id;
    private Double weight;
    private String weight_unit;
    private Long inventory_item_id;
    private Long inventory_quantity;
    private Boolean requires_shipping;
    private String admin_graphql_api_id;
    private Long stockAdjust;
}
