package com.springBoot.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")

public class Address {
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

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    public Address(String pincode, String country, String state, String city, String building, String street) {
        this.pincode = pincode;
        this.country = country;
        this.state = state;
        this.city = city;
        this.building = building;
        this.street = street;
    }
}
