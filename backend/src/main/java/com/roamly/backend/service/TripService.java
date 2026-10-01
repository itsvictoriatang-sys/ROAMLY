package com.roamly.backend.service;

import com.roamly.backend.entity.Trip;
import com.roamly.backend.entity.TripMember;
import com.roamly.backend.entity.TripRole;
import com.roamly.backend.entity.User;
import com.roamly.backend.repository.TripMemberRepository;
import com.roamly.backend.repository.TripRepository;
import com.roamly.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.roamly.backend.exception.InvalidTripDateException;
import java.util.List;
import java.time.LocalDate;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final TripMemberRepository tripMemberRepository;
    private final UserRepository userRepository;

    public TripService(
            TripRepository tripRepository,
            TripMemberRepository tripMemberRepository,
            UserRepository userRepository) {

        this.tripRepository = tripRepository;
        this.tripMemberRepository = tripMemberRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Trip createTrip(
            String name,
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            String creatorEmail) {

        if (endDate.isBefore(startDate)) {
    throw new InvalidTripDateException(
            "End date cannot be before start date"
    );
}

        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        Trip trip = Trip.builder()
                .name(name)
                .destination(destination)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        Trip savedTrip = tripRepository.save(trip);

        TripMember ownerMembership = TripMember.builder()
                .trip(savedTrip)
                .user(creator)
                .role(TripRole.OWNER)
                .build();

        tripMemberRepository.save(ownerMembership);

        return savedTrip;
    }
    @Transactional(readOnly = true)
public List<Trip> getTripsForUser(String email) {

    return tripMemberRepository.findByUserEmail(email)
            .stream()
            .map(TripMember::getTrip)
            .toList();
}
}