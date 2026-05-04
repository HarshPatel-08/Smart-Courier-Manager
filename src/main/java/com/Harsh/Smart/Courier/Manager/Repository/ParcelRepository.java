package com.Harsh.Smart.Courier.Manager.Repository;

import com.Harsh.Smart.Courier.Manager.Model.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel,Integer> {

}
