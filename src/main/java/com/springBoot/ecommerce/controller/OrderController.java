package com.springBoot.ecommerce.controller;

import com.springBoot.ecommerce.payload.OrderDTO;
import com.springBoot.ecommerce.payload.OrderRequestDTO;
import com.springBoot.ecommerce.service.OrderService;
import com.springBoot.ecommerce.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtil authUtil;
    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProduct(@PathVariable String paymentMethod,
                                                 @RequestBody OrderRequestDTO orderRequestDTO){
        String emailId = authUtil.loggedInEmail();
        OrderDTO orderDTO =  orderService.placeOrder(emailId,paymentMethod,orderRequestDTO);
        return  new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }
}
