package com.beercenter.shop.core.services;

import com.beercenter.shop.core.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl {

    private final ShopifyApiService shopifyApiService;

    public Set<Variant> getShopProducts() {
        final List<ShopProductInfo> shopProductInfoList = shopifyApiService.getShopProducts().getProducts();

        return shopProductInfoList.stream().map(ShopProductInfo::getVariants).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public String updateProductVariant(final Variant variant) {

        return shopifyApiService.updateVariant(variant.getId(), VariantRequest.builder().variant(variant).build());
    }

    public Map<Long, InventoryLevel> getVariantInventoryList(final List<Variant> variants) {
        final List<String> inventoryIds = variants.stream().map(variant -> String.valueOf(variant.getInventory_item_id())).collect(Collectors.toList());
        final String inventoryIdsString = String.join(",", inventoryIds);

        return shopifyApiService.getInventoryLevels(inventoryIdsString).getInventory_levels().stream().collect(Collectors.toMap(InventoryLevel::getInventory_item_id, value -> value));
    }

    public String adjustVariantInventory(final InventoryLevel inventoryLevel, final Long stock) {

        return shopifyApiService.adjustInventoryLevel(InventoryLevelRequest.builder().inventory_item_id(inventoryLevel.getInventory_item_id()).location_id(inventoryLevel.getLocation_id()).available_adjustment(stock).build());
    }
}