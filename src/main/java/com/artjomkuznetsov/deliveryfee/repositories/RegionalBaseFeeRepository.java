package com.artjomkuznetsov.deliveryfee.repositories;

import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionalBaseFeeRepository extends JpaRepository<RegionalBaseFee, Long> {
}
