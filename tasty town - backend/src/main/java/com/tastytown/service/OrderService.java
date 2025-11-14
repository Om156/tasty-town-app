package com.tastytown.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.tastytown.constants.OrderStatus;
import com.tastytown.dto.BillingInfoDTO;
import com.tastytown.dto.OrderDTO;
import com.tastytown.dto.OrderItemDTO;
import com.tastytown.entity.Cart;
import com.tastytown.entity.CartItem;
import com.tastytown.entity.OrderEntity;
import com.tastytown.entity.OrderItem;
import com.tastytown.entity.UserEntity;
import com.tastytown.mapper.OrderMapper;
import com.tastytown.repository.CartRepository;
import com.tastytown.repository.OrderRepository;
import com.tastytown.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderDTO placeOrder(BillingInfoDTO billingInfo, String userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.FOOD_PREPARING);

        // Combine contact info
        StringJoiner contactJoiner = new StringJoiner(", ");
        contactJoiner.add(billingInfo.getFullName());
        contactJoiner.add(billingInfo.getEmail());
        contactJoiner.add(billingInfo.getPhoneNumber());
        order.setContactInfo(contactJoiner.toString());

        // Combine address info
        StringJoiner addressJoiner = new StringJoiner(", ");
        addressJoiner.add(billingInfo.getAddress());
        addressJoiner.add(billingInfo.getCity());
        addressJoiner.add(billingInfo.getState());
        addressJoiner.add(billingInfo.getZip());
        order.setAddressInfo(addressJoiner.toString());

        double totalAmount = 0;

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setFoodName(cartItem.getFood().getFoodName());
            orderItem.setFoodPrice(cartItem.getFood().getFoodPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            totalAmount += cartItem.getFood().getFoodPrice() * cartItem.getQuantity();
            order.getOrderItems().add(orderItem);
        }

        order.setTotalAmount(totalAmount + 10 + (totalAmount * 0.1));
        orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return OrderMapper.convertToOrderDTO(order);
    }


    public List<OrderDTO> getOrdersByUser(String userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    
        List<OrderEntity> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(OrderMapper :: convertToOrderDTO)
                .collect(Collectors.toList());
    }
    
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll(Sort.by(Direction.DESC, "orderDate")).stream()
                .map(OrderMapper :: convertToOrderDTO)
                .collect(Collectors.toList());
    }
    
    public OrderDTO updateOrderStatus(String orderId, OrderStatus newStatus) {
        OrderEntity order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    
        order.setStatus(newStatus);
        orderRepository.save(order);
    
        return OrderMapper.convertToOrderDTO(order);
    }
}
