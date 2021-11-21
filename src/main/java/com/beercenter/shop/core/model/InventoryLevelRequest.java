package com.beercenter.shop.core.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class InventoryLevelRequest implements Serializable {
    private Long location_id;
    private Long inventory_item_id;
    private Long available_adjustment;
}
