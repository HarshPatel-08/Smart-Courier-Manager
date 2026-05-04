package com.Harsh.Smart.Courier.Manager.Repository;

import com.Harsh.Smart.Courier.Manager.Model.DeliveryAssignment;
import com.Harsh.Smart.Courier.Manager.Model.DeliveryStatus;
import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment,Integer> {
    List<DeliveryAssignment> findByAgent_Id(int agentId);


    List<DeliveryAssignment> findByStatus(DeliveryStatus status);
}
