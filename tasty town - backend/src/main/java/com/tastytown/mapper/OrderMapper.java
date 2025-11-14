package com.tastytown.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.tastytown.dto.OrderDTO;
import com.tastytown.dto.OrderItemDTO;
import com.tastytown.entity.OrderEntity;

public class OrderMapper {
    private OrderMapper() {}

    public static OrderDTO convertToOrderDTO(OrderEntity order) {
        List<OrderItemDTO> items = order.getOrderItems().stream()
            .map(i -> new OrderItemDTO(i.getFoodName(), i.getFoodPrice(), i.getQuantity()))
            .collect(Collectors.toList());

        return new OrderDTO(
            order.getOrderId(),
            items,
            order.getTotalAmount(),
            order.getStatus().toString(),
            order.getOrderDate(),
            order.getContactInfo(),
            order.getAddressInfo()
        );
    }
}
