package com.lootopiaApi.repository;

import com.lootopiaApi.model.entity.Hunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HuntRepository extends JpaRepository<Hunt, Long> {
    List<Hunt> findByPartnerId_Id(Long id);
}
