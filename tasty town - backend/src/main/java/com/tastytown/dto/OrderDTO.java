package com.tastytown.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String orderId;
    private List<OrderItemDTO> items;
    private double totalAmount;
    private String status;
    private LocalDateTime orderDate;

    // later added
    private String contactInfo;
    private String addressInfo;

}