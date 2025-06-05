package com.lootopiaApi.repository;

import com.lootopiaApi.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    void deleteByUserIdIn(List<Long> userIds);

}