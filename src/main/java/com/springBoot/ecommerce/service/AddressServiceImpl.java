package com.springBoot.ecommerce.service;

import com.springBoot.ecommerce.Exceptions.APIException;
import com.springBoot.ecommerce.Exceptions.ResourceNotFoundException;
import com.springBoot.ecommerce.Repository.AddressRepository;
import com.springBoot.ecommerce.Repository.UserRepository;
import com.springBoot.ecommerce.model.Address;
import com.springBoot.ecommerce.payload.AddressDTO;
import com.springBoot.ecommerce.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;



    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO,Address.class);
        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress,AddressDTO.class);

    }

    @Override
    public List<AddressDTO> getAllAddresses() {

        List<Address> addressList = addressRepository.findAll();
        if(addressList.size() == 0){
            throw new APIException("Address are not found");
        }
        List<AddressDTO> addressDTOList = addressList.stream()
                .map(address -> {
                    AddressDTO addressDTO = modelMapper.map(address,AddressDTO.class);
                    return addressDTO;
                }).toList();
        return addressDTOList;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {

        Address savedAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address","addressId",addressId));

        AddressDTO addressDTO = modelMapper.map(savedAddress,AddressDTO.class);
        return addressDTO;
    }

    @Override
    public List<AddressDTO> getAddressesByUser(User  user) {

        List<Address> addressList = user.getAddresses();
        if(addressList.isEmpty()){
            throw new APIException("No addresses are available for current user ");
        }
        List<AddressDTO> addressDTOS = addressList.stream()
                .map(address -> modelMapper.map(address,AddressDTO.class)).toList();
        return addressDTOS;
    }

    @Override
    public AddressDTO updateAddressById(Long addressId, AddressDTO newAddress) {


        Address savedAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address","addressId",addressId));


        modelMapper.map(newAddress, savedAddress);

        addressRepository.save(savedAddress);

        User user = savedAddress.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAddresses().add(savedAddress);
        userRepository.save(user);

        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddressById(Long addressId) {
        Address savedAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address","addressId",addressId));
        User user = savedAddress.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(user);



        addressRepository.deleteById(addressId);

        return "Address with id :"+ addressId+" was deleted successfully";
    }


}
