package com.artjomkuznetsov.deliveryfee.repositories;

import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionalBaseFeeRepository extends JpaRepository<RegionalBaseFee, Integer> {
    Optional<RegionalBaseFee> findByCity(String city);
}
