package com.springBoot.ecommerce.payload;

import com.springBoot.ecommerce.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long orderItemId;
    private Product product;
    private Integer quantity;
    private Double discount;
    private  Double orderedProductPrice;
}
