package com.springBoot.ecommerce.controller;

import com.springBoot.ecommerce.Repository.AddressRepository;
import com.springBoot.ecommerce.payload.AddressDTO;
import com.springBoot.ecommerce.model.User;
import com.springBoot.ecommerce.service.AddressService;
import com.springBoot.ecommerce.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    AddressRepository addressRepository;
    @Autowired
    AddressService addressService;
    @Autowired
    AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        User user = authUtil.loggedInUser();
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO,user);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddress(){
        List<AddressDTO> addressList = addressService.getAllAddresses();

        return new ResponseEntity<>(addressList,HttpStatus.FOUND);
    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){

        AddressDTO address = addressService.getAddressById(addressId);
        return new ResponseEntity<>(address,HttpStatus.FOUND);
    }

    @GetMapping("user/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressByUser(){
        User user = authUtil.loggedInUser();
        List<AddressDTO> addressDTOList = addressService.getAddressesByUser(user);

        return new ResponseEntity<>(addressDTOList,HttpStatus.FOUND);
    }

    @PutMapping(("/address/{addressId}"))
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long addressId
    ,@RequestBody AddressDTO newaddress){

        AddressDTO addressDTO = addressService.updateAddressById(addressId,newaddress);

        return new ResponseEntity<>(addressDTO,HttpStatus.OK);

    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<String> deteleAddressById(@PathVariable Long addressId){
        String response = addressService.deleteAddressById(addressId);
        return  new ResponseEntity<>(response,HttpStatus.OK);
    }

}
