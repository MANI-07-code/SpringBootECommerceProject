package com.springBoot.ecommerce.Repository;

import com.springBoot.ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

//    @Query("Select a from Address a where a.user.id=?1")
//    List<Address> findByUserId(Long userId);
}
