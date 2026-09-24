package com.springBoot.ecommerce.service;

import com.springBoot.ecommerce.Exceptions.APIException;
import com.springBoot.ecommerce.Exceptions.ResourceNotFoundException;
import com.springBoot.ecommerce.Repository.*;
import com.springBoot.ecommerce.model.*;
import com.springBoot.ecommerce.Repository.*;
import com.springBoot.ecommerce.model.*;
import com.springBoot.ecommerce.payload.OrderDTO;
import com.springBoot.ecommerce.payload.OrderItemDTO;
import com.springBoot.ecommerce.payload.OrderRequestDTO;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
   private OrderItemRepository orderItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
   private AddressRepository addressRepository;
    @Autowired
   private ProductRepository productRepository;
    @Autowired
   private ModelMapper modelMapper;

    @Autowired
   private CartService cartService;

    @Autowired
   private PaymentRepository paymentRepository;

    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, String paymentMethod, OrderRequestDTO orderRequestDTO) {
        //Getting User Cart
        Cart cart = cartRepository.findCartByEmail(emailId);
        if(cart == null){
            throw new ResourceNotFoundException("Cart","email",emailId);
        }
        Long addressId = orderRequestDTO.getAddressId();
        Address address = addressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address","addressId",addressId));

        // create a new order with payment info
        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted !");
        order.setAddress(address);

        String pgPaymentId = orderRequestDTO.getPgPaymentId();
        String pgStatus = orderRequestDTO.getPgStatus();
        String pgResponseMessage = orderRequestDTO.getPgResponseMessage();
        String pgName = orderRequestDTO.getPgName();



          Payment payment = new Payment(paymentMethod,pgPaymentId,pgStatus,pgResponseMessage,pgName) ;
          payment.setOrder(order);
          paymentRepository.save(payment);
          order.setPayment(payment);

          orderRepository.save(order);

        // Get items from the cart into the order item
        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems.isEmpty()){
            throw new APIException("Cart is Empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for( CartItem cartItem : cartItems){
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);

        //update product stack;
        //clear the cart
        cart.getCartItems().forEach(item ->{
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);

            cartService.deleteProductFromCart(cart.getCartId(),item.getProduct().getProductId());
        });

        //send back the order summary
        OrderDTO orderDTO = modelMapper.map(order,OrderDTO.class);
        orderItems.forEach(item ->
                orderDTO.getOrderItems().add(
                        modelMapper.map(item, OrderItemDTO.class)));
        orderDTO.setAddressId(addressId);

 return orderDTO;
    }
}
