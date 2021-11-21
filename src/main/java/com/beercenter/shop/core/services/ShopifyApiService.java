package com.beercenter.shop.core.services;

import com.beercenter.shop.core.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "shopify", url = "${shopify.url.base}")
public interface ShopifyApiService {

    @RequestMapping(method = RequestMethod.GET, value = "${shopify.url.products}", consumes = "application/json; charset=utf-8", headers = {"Authorization=Basic ${shopify.token}"})
    ProductResponse getShopProducts(@RequestParam("limit") int limit);

    @RequestMapping(method = RequestMethod.GET, value = "${shopify.url.products}", consumes = "application/json; charset=utf-8", headers = {"Authorization=Basic ${shopify.token}"})
    ProductResponse getShopProductsSinceId(@RequestParam("limit") int limit, @RequestParam("since_id") String sinceId);

    @RequestMapping(method = RequestMethod.PUT, value = "${shopify.url.variant}", consumes = "application/json; charset=utf-8", headers = {"Authorization=Basic ${shopify.token}"})
    String updateVariant(@RequestParam("id") Long variantId, @RequestBody VariantRequest variant);

    @RequestMapping(method = RequestMethod.GET, value = "${shopify.url.inventorylevel}", consumes = "application/json; charset=utf-8", headers = {"Authorization=Basic ${shopify.token}"})
    InventoryLevelResponse getInventoryLevels(@RequestParam("inventory_item_ids") String ids);

    @RequestMapping(method = RequestMethod.POST, value = "${shopify.url.inventoryleveladjust}", consumes = "application/json; charset=utf-8", headers = {"Authorization=Basic ${shopify.token}"})
    String adjustInventoryLevel(@RequestBody InventoryLevelRequest inventoryLevelRequest);
}
