package com.Harsh.Smart.Courier.Manager.Repository;

import com.Harsh.Smart.Courier.Manager.Model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location,Integer> {
}
