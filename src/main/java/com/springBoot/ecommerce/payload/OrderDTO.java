package com.springBoot.ecommerce.payload;

import com.springBoot.ecommerce.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private String orderId;
    private String email;
    private List<OrderItemDTO> orderItems;
    private Payment payment;
    private Double totalAmount;
    private String orderStatus;
    private Long addressId;
}
