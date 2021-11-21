package com.beercenter.shop.core.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class InventoryLevelResponse implements Serializable {

    private List<InventoryLevel> inventory_levels = new ArrayList<>();
}
