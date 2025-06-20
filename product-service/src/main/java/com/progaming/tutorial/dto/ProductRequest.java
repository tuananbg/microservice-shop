package com.progaming.tutorial.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest{
    String id;
    String name;
    String description;
    String skuCode;
    BigDecimal price;
}
