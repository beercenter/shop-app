package com.beercenter.shop.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopProductInfo implements Serializable {

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
