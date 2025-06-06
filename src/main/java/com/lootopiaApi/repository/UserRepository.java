package com.lootopiaApi.repository;

import com.lootopiaApi.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    User findByResetToken(String token);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE %:query% OR LOWER(u.firstName) LIKE %:query% OR LOWER(u.lastName) LIKE %:query%")
    Page<User> findByQuery(@Param("query") String query, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.role = :role")
    Page<User> findByRole(@Param("role") com.lootopiaApi.model.enums.ERole role, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE " +
            "(LOWER(u.username) LIKE %:query% OR LOWER(u.firstName) LIKE %:query% OR LOWER(u.lastName) LIKE %:query%) " +
            "AND r.role = :role")
    Page<User> findByQueryAndRole(@Param("query") String query, @Param("role") com.lootopiaApi.model.enums.ERole role, Pageable pageable);

    Page<User> findByActive(Boolean isActive, Pageable pageable);

    List<User> findByActiveFalseAndUpdatedAtBefore(LocalDateTime dateTime);

}
