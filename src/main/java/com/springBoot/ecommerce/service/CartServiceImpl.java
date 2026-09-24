package com.springBoot.ecommerce.service;

import com.springBoot.ecommerce.Exceptions.APIException;
import com.springBoot.ecommerce.Exceptions.ResourceNotFoundException;
import com.springBoot.ecommerce.Repository.CartItemRepository;
import com.springBoot.ecommerce.Repository.CartRepository;
import com.springBoot.ecommerce.Repository.ProductRepository;
import com.springBoot.ecommerce.model.Cart;
import com.springBoot.ecommerce.model.CartItem;
import com.springBoot.ecommerce.model.Product;
import com.springBoot.ecommerce.payload.CartDTO;
import com.springBoot.ecommerce.payload.ProductDTO;
import com.springBoot.ecommerce.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AuthUtil authUtil;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartItemRepository cartItemRepository;



    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {

        Cart cart = createCart();


        Product product = productRepository.findById(productId)
                .orElseThrow(() ->new ResourceNotFoundException("Product","ProductId",productId));


        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId,cart.getCartId());
        if(cartItem!=null){
            throw new APIException("Product "+product.getProductName()+" already exist in the cart");
        }
        if(product.getQuantity() == 0){
            throw new APIException(product.getProductName()+" is not available");
        }
        if(product.getQuantity() < quantity){
            throw new APIException("Pleas, make an order of the "+product.getProductName()
                    +" less than or equal to the quantity "+ product.getQuantity());
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());
        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity());
        cart.setTotalPrice(cart.getTotalPrice()+(product.getSpecialPrice()*quantity));
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
        List<CartItem>  cartItems = cart.getCartItems();
        Stream<ProductDTO> productStream = cartItems.stream().map(items ->{ //toget product quantity from cart not from product
            ProductDTO map = modelMapper.map(items.getProduct(),ProductDTO.class);
            map.setQuantity(items.getQuantity());
            return map;
        });
        cartDTO.setProducts(productStream.toList());
      return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.size() == 0){
            throw new APIException("No cart exist");
        }
        List<CartDTO> cartDTOS = carts.stream()
                .map(cart ->{
                    CartDTO cartDTO =modelMapper.map(cart,CartDTO.class);
                    List<ProductDTO> productDTOS =cart.getCartItems().stream().map(cartItem -> {
                        ProductDTO productDTO = modelMapper.map(cartItem.getProduct(),ProductDTO.class);
                        productDTO.setQuantity(cartItem.getQuantity());
                        return productDTO;

                            }).toList();

                    cartDTO.setProducts(productDTOS);

                    return cartDTO;
                }).collect(Collectors.toList());
        return cartDTOS;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
       Cart cart = cartRepository.findCartByEmailAndCartId(emailId,cartId);
       if(cart == null){
           throw new ResourceNotFoundException("Cart","cartId",cartId);
       }
       CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
       cart.getCartItems().forEach(c->c.getProduct().setQuantity(c.getQuantity()));
       List<ProductDTO> products = cart.getCartItems().stream()
               .map(p->modelMapper.map(p.getProduct(),ProductDTO.class))
               //.collect((Collectors.toList()));
               .toList();
       cartDTO.setProducts(products);
       return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        Cart cart = cartRepository.findById(userCart.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart","CartId", userCart.getCartId()));

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->new ResourceNotFoundException("Product","ProductId",productId));

        if(product.getQuantity() == 0){
            throw new APIException(product.getProductName()+" is not available");
        }
        if(product.getQuantity() < quantity){
            throw new APIException("Pleas, make an order of the "+product.getProductName()
                    +" less than or equal to the quantity "+ product.getQuantity());
        }
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId,cart.getCartId());
        if(cartItem==null){
            throw new APIException("Product "+product.getProductName()+" not available in cart!!");
        }
        Integer newQuantity = cartItem.getQuantity()+quantity;
        if(newQuantity<0){
            throw new APIException("The resulting quantity cannot be negative!");
        }
        if(newQuantity ==0){
            deleteProductFromCart(cart.getCartId(),productId);
        }else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * cartItem.getQuantity()));
            cartRepository.save(cart);
        }
        cartItemRepository.save(cartItem);
        if(cartItem .getQuantity()==0){
            cartItemRepository.deleteById(cartItem.getCartItemId());
        }




        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productStream = cartItems.stream().map(item ->{
            ProductDTO prd = modelMapper.map(item.getProduct(),ProductDTO.class);
            prd.setQuantity(item.getQuantity());
            return prd;
        });
        cartDTO.setProducts(productStream.toList());
        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(()-> new ResourceNotFoundException("Cart","cartId",cartId));
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId,cartId);
        if(cartItem == null){
            throw new ResourceNotFoundException("Product","productId",productId);
        }
        cart.setTotalPrice(cart.getTotalPrice()-(cartItem.getProductPrice()*cartItem.getQuantity()));
        cartItemRepository.deleteCartItemByProductIdAndCartId(productId,cartId);
        return "product "+ cartItem.getProduct().getProductName() +" removed from cart";
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart","CartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->new ResourceNotFoundException("Product","ProductId",productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId,cartId);
        if(cartItem==null){
            throw new APIException("Product "+ product.getProductName()+" not available");
        }
        Double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice()* cartItem.getQuantity());
        cartItem.setProductPrice(product.getSpecialPrice());
        cart.setTotalPrice(cartPrice+(cartItem.getProductPrice()*cartItem.getQuantity()));

        cartItemRepository.save(cartItem);

    }


    public Cart createCart(){
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart !=null){
            return userCart;
        }

            Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        cartRepository.save(cart);
        return cart;

    }
}
