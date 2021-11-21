package com.beercenter.shop.core.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class InventoryLevel implements Serializable {
    private long inventory_item_id;
    private long location_id;
    private int available;
    private Date updated_at;
    private String admin_graphql_api_id;

}
