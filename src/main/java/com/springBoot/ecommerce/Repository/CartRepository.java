package com.springBoot.ecommerce.Repository;

import com.springBoot.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query("Select c from Cart c where c.user.email = ?1")
    Cart findCartByEmail(String email);

    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1 AND c.id = ?2")
    Cart findCartByEmailAndCartId(String emailId, Long cartId);

    @Query("select c from Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p where p.id=?1")
    List<Cart> findCartByProductId(Long productId);
}
