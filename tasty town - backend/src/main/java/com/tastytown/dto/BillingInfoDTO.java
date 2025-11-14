package com.tastytown.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingInfoDTO {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String state;
    private String city;
    private String zip;
}