package com.Harsh.Smart.Courier.Manager.Dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class BulkAssignmentRequest {

    @NotEmpty(message = "Assignments list cannot be empty")
    private List<DeliveryAssignmentRequest> assignments;

    public BulkAssignmentRequest() {
    }

    public BulkAssignmentRequest(List<DeliveryAssignmentRequest> assignments) {
        this.assignments = assignments;
    }

    public List<DeliveryAssignmentRequest> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<DeliveryAssignmentRequest> assignments) {
        this.assignments = assignments;
    }
}
