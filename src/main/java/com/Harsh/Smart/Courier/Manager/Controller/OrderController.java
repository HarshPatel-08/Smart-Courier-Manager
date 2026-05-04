package com.Harsh.Smart.Courier.Manager.Controller;

import com.Harsh.Smart.Courier.Manager.Dto.ApiResponse;
import com.Harsh.Smart.Courier.Manager.Dto.OrderResponse;
import com.Harsh.Smart.Courier.Manager.Dto.OrdersRequest;
import com.Harsh.Smart.Courier.Manager.Security.JwtTokenProvider;
import com.Harsh.Smart.Courier.Manager.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @PostMapping("create")
    public ResponseEntity<?> createOrder(@RequestBody OrdersRequest request, @RequestHeader("Authorization")String token){
        String jwt=token.replace("Bearer ","");
        int customerId= jwtTokenProvider.getUserIdFromToken(jwt);
        OrderResponse response= orderService.createOrder(request,customerId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"Order created successfully",response,201));
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable int orderId, @RequestHeader("Authorization") String token) {
        OrderResponse response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Order retrieved successfully",
                        response,
                        200
                )
        );
    }
    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(@RequestHeader("Authorization") String token) {

        String jwt = token.replace("Bearer ", "");
        int customerId = jwtTokenProvider.getUserIdFromToken(jwt);

        List<OrderResponse> orders = orderService.getOrdersByCustomer(customerId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Orders retrieved successfully",
                        orders,
                        200
                )
        );
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders(
            @RequestHeader("Authorization") String token) {
        List<OrderResponse> orders = orderService.getAllOrders();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "All orders retrieved successfully",
                        orders,
                        200
                )
        );
    }
    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable int orderId,
            @RequestParam String status,
            @RequestHeader("Authorization") String token) {

        OrderResponse response = orderService.updateOrderStatus(orderId, status);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Order status updated successfully",
                        response,
                        200
                )
        );
    }
}
