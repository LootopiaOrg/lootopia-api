package com.lootopiaApi.repository;

import com.lootopiaApi.model.entity.Hunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface HuntRepository extends JpaRepository<Hunt, Long> {
    List<Hunt> findByOrganizer_Id(Long id);
    Page<Hunt> findByAccessModeAndEndDateAfter(String accessMode, LocalDateTime now, Pageable pageable);
}
