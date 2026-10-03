package com.roamly.backend.repository;

import com.roamly.backend.entity.TripMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripMemberRepository
        extends JpaRepository<TripMember, Long> {

    List<TripMember> findByUserEmail(String email);

    Optional<TripMember> findByTripIdAndUserEmail(
            Long tripId,
            String email
    );

    boolean existsByTripIdAndUserEmail(
            Long tripId,
            String email
    );

    List<TripMember> findByTripId(Long tripId);
}