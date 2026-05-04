package com.Harsh.Smart.Courier.Manager.Service;

import com.Harsh.Smart.Courier.Manager.Dto.BulkAssignmentRequest;
import com.Harsh.Smart.Courier.Manager.Dto.BulkAssignmentResponse;
import com.Harsh.Smart.Courier.Manager.Dto.DeliveryAssignmentRequest;
import com.Harsh.Smart.Courier.Manager.Dto.DeliveryAssignmentResponse;
import com.Harsh.Smart.Courier.Manager.Exception.LocationNotFound;
import com.Harsh.Smart.Courier.Manager.Exception.OrderNotFoundException;
import com.Harsh.Smart.Courier.Manager.Exception.UnauthorizedException;
import com.Harsh.Smart.Courier.Manager.Model.*;
import com.Harsh.Smart.Courier.Manager.Repository.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class DeliveryAssignmentService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryAssignmentService.class);

    @Autowired
    private DeliveryAssignmentRepository deliveryAssignmentRepository;

    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Transactional
    public DeliveryAssignmentResponse assignDelivery(DeliveryAssignmentRequest request) {
        logger.info("Creating delivery assignment for parcel {} to agent {}", 
                   request.getParcelId(), request.getAgentId());

        // Validate parcel exists
        Parcel parcel = parcelRepository.findById(request.getParcelId())
                .orElseThrow(() -> new OrderNotFoundException("Parcel not found"));

        // Validate agent exists and is AGENT role
        Users agent = userRepository.findById(request.getAgentId())
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        if (agent.getRole() != UserRole.AGENT) {
            throw new UnauthorizedException("Only agents can receive assignments");
        }

        // Validate location if provided
        Location currentLocation = null;
        if (request.getCurrentLocationId() != null) {
            currentLocation = locationRepository.findById(request.getCurrentLocationId())
                    .orElseThrow(() -> new LocationNotFound("Location not found"));
        }

        // Create assignment
        DeliveryAssignment assignment = new DeliveryAssignment();
        assignment.setParcel(parcel);
        assignment.setAgent(agent);
        assignment.setEstimatedDeliveryDate(request.getEstimatedDeliveryDate());
        assignment.setCurrentLocation(currentLocation);
        assignment.setStatus(DeliveryStatus.ASSIGNED);

        DeliveryAssignment saved = deliveryAssignmentRepository.save(assignment);
        logger.info("Assignment created: {}", saved.getId());

        return convertToResponse(saved);
    }


     //BULK ASSIGNMENT - Multithreading with ExecutorService
     //Assigns multiple parcels to agents in parallel

    @Transactional
    public BulkAssignmentResponse bulkAssignDeliveries(BulkAssignmentRequest request) {
        logger.info("Starting bulk assignment for {} parcels", request.getAssignments().size());
        
        long startTime = System.currentTimeMillis();
        List<String> successMessages = Collections.synchronizedList(new ArrayList<>());
        List<String> errorMessages = Collections.synchronizedList(new ArrayList<>());
        List<Future<?>> futures = new ArrayList<>();

        // Submit all assignment tasks to thread pool
        for (DeliveryAssignmentRequest assignmentRequest : request.getAssignments()) {
            Future<?> future = executorService.submit(() -> {
                try {
                    DeliveryAssignmentResponse response = assignDelivery(assignmentRequest);
                    successMessages.add("Parcel " + response.getParcelId() + 
                                      " assigned to " + response.getAgentName());
                    logger.info("✅ Assignment success: Parcel {} -> Agent {}", 
                               response.getParcelId(), response.getAgentName());
                } catch (Exception e) {
                    String error = "Failed to assign parcel " + assignmentRequest.getParcelId() + 
                                 ": " + e.getMessage();
                    errorMessages.add(error);
                    logger.error("❌ Assignment failed: {}", error, e);
                }
            });
            futures.add(future);
        }

        // Wait for all tasks to complete
        for (Future<?> future : futures) {
            try {
                future.get(30, TimeUnit.SECONDS); // 30 second timeout per task
            } catch (TimeoutException e) {
                errorMessages.add("Task timeout");
                logger.error("Task execution timeout", e);
            } catch (Exception e) {
                errorMessages.add("Task failed: " + e.getMessage());
                logger.error("Task execution failed", e);
            }
        }

        long processingTime = System.currentTimeMillis() - startTime;
        int totalRequested = request.getAssignments().size();
        int successful = successMessages.size();
        int failed = errorMessages.size();

        logger.info("Bulk assignment completed: {} successful, {} failed in {}ms", 
                   successful, failed, processingTime);

        return new BulkAssignmentResponse(
                totalRequested,
                successful,
                failed,
                processingTime,
                successMessages,
                errorMessages
        );
    }


     //  GET ASSIGNMENT BY ID

    public DeliveryAssignmentResponse getAssignmentById(int assignmentId) {
        logger.debug("Fetching assignment {}", assignmentId);
        DeliveryAssignment assignment = deliveryAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new OrderNotFoundException("Assignment not found"));
        return convertToResponse(assignment);
    }


     //GET MY ASSIGNMENTS - Agent's assignments

    public List<DeliveryAssignmentResponse> getMyAssignments(int agentId) {
        logger.info("Fetching assignments for agent {}", agentId);
        return deliveryAssignmentRepository.findByAgent_Id(agentId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


     // GET ALL ASSIGNMENTS - Admin/Manager

    public List<DeliveryAssignmentResponse> getAllAssignments() {
        logger.info("Fetching all assignments");
        return deliveryAssignmentRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 6️⃣ UPDATE ASSIGNMENT STATUS
     */
    @Transactional
    public DeliveryAssignmentResponse updateAssignmentStatus(int assignmentId, String status) {
        logger.info("Updating assignment {} status to {}", assignmentId, status);
        
        DeliveryAssignment assignment = deliveryAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new OrderNotFoundException("Assignment not found"));

        try {
            DeliveryStatus newStatus = DeliveryStatus.valueOf(status.toUpperCase());
            assignment.setStatus(newStatus);
            
            // If delivered, set actual delivery date
            if (newStatus == DeliveryStatus.DELIVERED) {
                assignment.setActualDeliveryDate(java.time.LocalDate.now());
            }

            DeliveryAssignment updated = deliveryAssignmentRepository.save(assignment);
            logger.info("✅ Assignment {} status updated to {}", assignmentId, status);
            
            return convertToResponse(updated);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }


     //UPDATE CURRENT LOCATION

    @Transactional
    public DeliveryAssignmentResponse updateCurrentLocation(int assignmentId, int locationId) {
        logger.info("Updating assignment {} location to {}", assignmentId, locationId);
        
        DeliveryAssignment assignment = deliveryAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new OrderNotFoundException("Assignment not found"));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFound("Location not found"));

        assignment.setCurrentLocation(location);
        DeliveryAssignment updated = deliveryAssignmentRepository.save(assignment);
        
        logger.info("✅ Assignment {} location updated", assignmentId);
        return convertToResponse(updated);
    }

    /**
     * 8️⃣ GET IN-TRANSIT ASSIGNMENTS - For scheduled updates
     */
    public List<DeliveryAssignmentResponse> getInTransitAssignments() {
        logger.debug("Fetching in-transit assignments");
        return deliveryAssignmentRepository.findByStatus(DeliveryStatus.IN_TRANSIT)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


     //CLEANUP - Shutdown executor when app stops
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }


     //Helper method to convert entity to DTO

    private DeliveryAssignmentResponse convertToResponse(DeliveryAssignment assignment) {
        return new DeliveryAssignmentResponse(
                assignment.getId(),
                assignment.getParcel().getId(),
                assignment.getAgent().getName(),
                assignment.getAgent().getEmail(),
                assignment.getAssignedDate(),
                assignment.getEstimatedDeliveryDate(),
                assignment.getActualDeliveryDate(),
                assignment.getStatus().name(),
                assignment.getCurrentLocation() != null ? 
                        assignment.getCurrentLocation().getCity() : "Not assigned"
        );
    }
}
