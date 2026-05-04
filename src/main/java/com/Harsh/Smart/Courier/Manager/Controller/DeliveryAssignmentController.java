package com.Harsh.Smart.Courier.Manager.Controller;

import com.Harsh.Smart.Courier.Manager.Dto.*;
import com.Harsh.Smart.Courier.Manager.Security.JwtTokenProvider;
import com.Harsh.Smart.Courier.Manager.Service.DeliveryAssignmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "*")
public class DeliveryAssignmentController {

    @Autowired
    private DeliveryAssignmentService deliveryAssignmentService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @PostMapping("/assign")
    public ResponseEntity<ApiResponse<DeliveryAssignmentResponse>> assignDelivery(
            @Valid @RequestBody DeliveryAssignmentRequest request,
            @RequestHeader("Authorization") String token) {
        
        DeliveryAssignmentResponse response = deliveryAssignmentService.assignDelivery(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Delivery assigned successfully",
                        response,
                        201
                ));
    }


    @PostMapping("/bulk-assign")
    public ResponseEntity<ApiResponse<BulkAssignmentResponse>> bulkAssignDeliveries(
            @Valid @RequestBody BulkAssignmentRequest request,
            @RequestHeader("Authorization") String token) {
        
        long startTime = System.currentTimeMillis();
        BulkAssignmentResponse response = deliveryAssignmentService.bulkAssignDeliveries(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Bulk assignments processed: " + response.getSuccessfulAssignments() + 
                        " successful, " + response.getFailedAssignments() + " failed",
                        response,
                        201
                ));
    }


    @GetMapping("/{assignmentId}")
    public ResponseEntity<ApiResponse<DeliveryAssignmentResponse>> getAssignmentById(
            @PathVariable int assignmentId,
            @RequestHeader("Authorization") String token) {
        
        DeliveryAssignmentResponse response = 
                deliveryAssignmentService.getAssignmentById(assignmentId);
        
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Assignment retrieved successfully",
                        response,
                        200
                )
        );
    }


    @GetMapping("/my-assignments")
    public ResponseEntity<ApiResponse<List<DeliveryAssignmentResponse>>> getMyAssignments(
            @RequestHeader("Authorization") String token) {
        
        String jwt = token.replace("Bearer ", "");
        int agentId = jwtTokenProvider.getUserIdFromToken(jwt);
        
        List<DeliveryAssignmentResponse> responses = 
                deliveryAssignmentService.getMyAssignments(agentId);
        
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Your assignments retrieved: " + responses.size() + " found",
                        responses,
                        200
                )
        );
    }


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<DeliveryAssignmentResponse>>> getAllAssignments(
            @RequestHeader("Authorization") String token) {
        
        List<DeliveryAssignmentResponse> responses = 
                deliveryAssignmentService.getAllAssignments();
        
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "All assignments retrieved: " + responses.size() + " found",
                        responses,
                        200
                )
        );
    }

    @PutMapping("/{assignmentId}/status")
    public ResponseEntity<ApiResponse<DeliveryAssignmentResponse>> updateAssignmentStatus(
            @PathVariable int assignmentId,
            @RequestParam String status,
            @RequestHeader("Authorization") String token) {
        
        DeliveryAssignmentResponse response = 
                deliveryAssignmentService.updateAssignmentStatus(assignmentId, status);
        
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Status updated to: " + status,
                        response,
                        200
                )
        );
    }


    @PutMapping("/{assignmentId}/location/{locationId}")
    public ResponseEntity<ApiResponse<DeliveryAssignmentResponse>> updateCurrentLocation(
            @PathVariable int assignmentId,
            @PathVariable int locationId,
            @RequestHeader("Authorization") String token) {
        
        DeliveryAssignmentResponse response = 
                deliveryAssignmentService.updateCurrentLocation(assignmentId, locationId);
        
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Current location updated",
                        response,
                        200
                )
        );
    }

    /**
     * 8️⃣ GET IN-TRANSIT DELIVERIES
     * GET /api/assignments/in-transit
     * View all deliveries currently being transported
     */
    @GetMapping("/in-transit")
    public ResponseEntity<ApiResponse<List<DeliveryAssignmentResponse>>> getInTransitAssignments(
            @RequestHeader("Authorization") String token) {
        
        List<DeliveryAssignmentResponse> responses = 
                deliveryAssignmentService.getInTransitAssignments();
        
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "In-transit assignments: " + responses.size(),
                        responses,
                        200
                )
        );
    }
}
