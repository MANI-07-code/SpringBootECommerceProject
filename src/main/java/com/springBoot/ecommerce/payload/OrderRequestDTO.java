package com.springBoot.ecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderRequestDTO {

    private Long addressId;
    private String paymentMethod;

    private String pgPaymentId; // payment gateway paymentId
    private String pgStatus;
    private String pgResponseMessage;
    private String pgName;
}
