package com.springBoot.ecommerce.Repository;

import com.springBoot.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @Query("Select ci from CartItem ci where ci.product.id = ?1 And ci.cart.id = ?2 ")
    CartItem findCartItemByProductIdAndCartId(Long productId, Long id);

    @Query("Delete from CartItem ci where ci.product.id =?1 and ci.cart.id = ?2")
    void deleteCartItemByProductIdAndCartId(Long productId, Long cartId);
}
