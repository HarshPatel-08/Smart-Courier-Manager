package com.Harsh.Smart.Courier.Manager.Service;

import com.Harsh.Smart.Courier.Manager.Dto.OrderResponse;
import com.Harsh.Smart.Courier.Manager.Dto.OrdersRequest;
import com.Harsh.Smart.Courier.Manager.Exception.LocationNotFound;
import com.Harsh.Smart.Courier.Manager.Exception.OrderNotFoundException;
import com.Harsh.Smart.Courier.Manager.Exception.UnauthorizedException;
import com.Harsh.Smart.Courier.Manager.Model.*;
import com.Harsh.Smart.Courier.Manager.Repository.LocationRepository;
import com.Harsh.Smart.Courier.Manager.Repository.OrderRepository;
import com.Harsh.Smart.Courier.Manager.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public OrderResponse createOrder(OrdersRequest request,int customerId){

        Users customer=userRepository.findById(customerId).orElseThrow(()->new RuntimeException("Customer not found!!!"));
        if(customer.getRole()!= UserRole.CUSTOMER){
            throw new UnauthorizedException("Only customers can create orders");
        }
        Location senderLocation = locationRepository.findById(request.getSenderLocationId()).orElseThrow(()-> new LocationNotFound("Sender location not found"));
        Location receiverLocation = locationRepository.findById(request.getReceiverLocationId()).orElseThrow(()-> new LocationNotFound("Receiver location not found "));
        Order order = new Order();
        order.setCustomer(customer);
        order.setSenderLocation(senderLocation);
        order.setReceiverLocation(receiverLocation);
        order.setExpectedDeliveryDate(request.getExpectedDeliveryDate());
        order.setStatus(OrderStatus.PENDING);

        Order saveOrder= orderRepository.save(order);
        return  convertToResponse(saveOrder);
    }
    public OrderResponse getOrderById(int orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found - ID: " + orderId));

        return convertToResponse(order);
    }

    //customer
    @Transactional
    public List<OrderResponse> getOrdersByCustomer(int customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);

        if (orders == null) {
            return new ArrayList<>();  // Return empty list instead of null
        }
              return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    //admin and manager
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public OrderResponse updateOrderStatus(int orderId, String status) {


        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        try {
            order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
            Order updatedOrder = orderRepository.save(order);
            // ADD — send email notification async (won't slow down the API response)
            String customerEmail = order.getCustomer().getEmail();
            String customerName  = order.getCustomer().getName();
            emailService.sendOrderStatusEmail(customerEmail, customerName, orderId, status);

            return convertToResponse(updatedOrder);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    private OrderResponse convertToResponse(Order order){
        return  new OrderResponse(      order.getId(),
                order.getCustomer().getName(),
                order.getSenderLocation().getCity(),
                order.getReceiverLocation().getCity(),
                order.getOrderDate(),
                order.getExpectedDeliveryDate(),
                order.getStatus().name());
    }
}
