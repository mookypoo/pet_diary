package com.mooky.pet_diary.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mooky.pet_diary.domain.user.User;
import com.mooky.pet_diary.domain.user.dto.UserWithPetSummaryProjection;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM User u where u.email = :email) THEN true ELSE false END")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM User u WHERE u.username = :username) THEN true ELSE false END")
    boolean existsByUsername(@Param("username") String username);

    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.recentLoginAt = CURRENT_TIMESTAMP WHERE u.id = :userId")
    int updateRecentLoginById(@Param("userId") Long userId);

    @Query("""
            SELECT u.id as userId, u.username as username, u.email as email,
                    p.id as petId, p.name as petName, p.profilePhoto as petProfilePhoto
            FROM User u
            JOIN FETCH Pet p ON u.id = p.ownerId
            WHERE u.id=:userId
            """)
    List<UserWithPetSummaryProjection> findUserProfileWithPetsSummaryById(@Param("userId") Long userId);
}
