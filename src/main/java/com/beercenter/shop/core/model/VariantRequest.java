package com.beercenter.shop.core.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class VariantRequest implements Serializable {

    private Variant variant;
}
