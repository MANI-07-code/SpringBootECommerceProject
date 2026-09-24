package com.springBoot.ecommerce.service;

import com.springBoot.ecommerce.payload.AddressDTO;
import com.springBoot.ecommerce.model.User;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(@Valid AddressDTO addressDTO, User user);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getAddressesByUser(User user);

    AddressDTO updateAddressById(Long addressId, AddressDTO newaddress);

    String deleteAddressById(Long addressId);
}
