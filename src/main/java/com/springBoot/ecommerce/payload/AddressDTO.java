package com.springBoot.ecommerce.payload;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;

    @NotBlank
    @Size(min =5,message = "Street name must be atleast 5 characters")
    private String street;

    @NotBlank
    @Size(min =5,message = "Building name must be atleast 4 character")
    private String building;

    @NotBlank
    @Size(min =5,message = "City name must be atleast 4 character")
    private String city;

    @NotBlank
    @Size(min =5,message = "State name must be atleast 4 character")
    private String state;

    @NotBlank
    @Size(min =5,message = "Country name must be atleast 4 character")
    private String country;

    @NotBlank
    @Size(min =6,message = "Pincode must be atleast 4 character")
    private String pincode;
}
