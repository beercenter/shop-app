package com.beercenter.shop.core.services;

import com.beercenter.shop.core.model.*;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.beercenter.shop.core.utils.ProductType.BEER;


@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl {

    private static final int QUERY_LIMIT = 250;
    private static final int SLEEP_TIME = 200;
    private final ShopifyApiService shopifyApiService;

    public Set<Variant> getShopProducts() {
        final List<ShopProductInfo> shopProductInfoList = shopifyApiService.getShopProducts(QUERY_LIMIT).getProducts();
        if (shopProductInfoList.size() == QUERY_LIMIT) {
            do {
                shopProductInfoList.addAll(shopifyApiService.getShopProductsSinceId(QUERY_LIMIT, String.valueOf(CollectionUtils.lastElement(shopProductInfoList).getId())).getProducts());
            } while (shopProductInfoList.size() % QUERY_LIMIT == 0);
        }

        return shopProductInfoList.stream().filter(product -> product.getProduct_type().equalsIgnoreCase(BEER.getValue())).map(ShopProductInfo::getVariants).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public String updateProductVariant(final Variant variant) throws InterruptedException {
        Thread.sleep(SLEEP_TIME);
        return shopifyApiService.updateVariant(variant.getId(), VariantRequest.builder().variant(variant).build());
    }

    public Map<Long, InventoryLevel> getVariantInventoryList(final List<Variant> variants) {
        final List<String> inventoryIds = variants.stream().map(variant -> String.valueOf(variant.getInventory_item_id())).collect(Collectors.toList());
        final String inventoryIdsString = String.join(",", inventoryIds);

        return shopifyApiService.getInventoryLevels(inventoryIdsString).getInventory_levels().stream().collect(Collectors.toMap(InventoryLevel::getInventory_item_id, value -> value));
    }

    public String adjustVariantInventory(final InventoryLevel inventoryLevel, final Long stock) throws InterruptedException {
        Thread.sleep(SLEEP_TIME);
        return shopifyApiService.adjustInventoryLevel(InventoryLevelRequest.builder().inventory_item_id(inventoryLevel.getInventory_item_id()).location_id(inventoryLevel.getLocation_id()).available_adjustment(stock).build());
    }
}