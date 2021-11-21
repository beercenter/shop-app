package com.beercenter.shop.core.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ShopProductInfo {

    public long id;
    public String title;
    public String body_html;
    public String vendor;
    public String product_type;
    public Date created_at;
    public String handle;
    public Date updated_at;
    public Date published_at;
    public String template_suffix;
    public String status;
    public String published_scope;
    public String tags;
    public String admin_graphql_api_id;
    public List<Variant> variants;
}
