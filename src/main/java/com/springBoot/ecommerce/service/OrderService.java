package com.springBoot.ecommerce.service;

import com.springBoot.ecommerce.payload.OrderDTO;
import com.springBoot.ecommerce.payload.OrderRequestDTO;
import jakarta.transaction.Transactional;

public interface OrderService {

    @Transactional
    OrderDTO placeOrder(String emailId, String paymentMethod, OrderRequestDTO orderRequestDTO);
}
