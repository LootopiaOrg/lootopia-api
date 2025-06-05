package com.lootopiaApi.repository;

import com.lootopiaApi.model.entity.Hunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface HuntRepository extends JpaRepository<Hunt, Long> {
    Page<Hunt> findByOrganizer_Id(Long id, Pageable pageable);
    Page<Hunt> findByAccessModeAndEndDateAfter(String accessMode, LocalDateTime now, Pageable pageable);
    @Modifying
    @Query("UPDATE Hunt h SET h.organizer.id = NULL WHERE h.organizer.id IN :ids")
    void clearOrganizerReferences(@Param("ids") List<Long> ids);

}
