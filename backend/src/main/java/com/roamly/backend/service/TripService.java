package com.roamly.backend.service;

import com.roamly.backend.entity.Trip;
import com.roamly.backend.entity.TripMember;
import com.roamly.backend.entity.TripRole;
import com.roamly.backend.entity.User;
import com.roamly.backend.exception.ForbiddenTripActionException;
import com.roamly.backend.exception.InvalidTripDateException;
import com.roamly.backend.exception.InvalidTripRoleException;
import com.roamly.backend.exception.TripMemberAlreadyExistsException;
import com.roamly.backend.exception.TripNotFoundException;
import com.roamly.backend.exception.UserNotFoundException;
import com.roamly.backend.repository.TripMemberRepository;
import com.roamly.backend.repository.TripRepository;
import com.roamly.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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

    // =========================================================
    // Create a new trip
    // =========================================================

    @Transactional
    public Trip createTrip(
            String name,
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            String creatorEmail) {

        // End date cannot be before start date
        if (endDate.isBefore(startDate)) {
            throw new InvalidTripDateException(
                    "End date cannot be before start date"
            );
        }

        // Find the user creating the trip
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        // Create the trip
        Trip trip = Trip.builder()
                .name(name)
                .destination(destination)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        Trip savedTrip = tripRepository.save(trip);

        // The creator automatically becomes the OWNER
        TripMember ownerMembership = TripMember.builder()
                .trip(savedTrip)
                .user(creator)
                .role(TripRole.OWNER)
                .build();

        tripMemberRepository.save(ownerMembership);

        return savedTrip;
    }

    // =========================================================
    // Get all trips that the current user belongs to
    // =========================================================

    @Transactional(readOnly = true)
    public List<Trip> getTripsForUser(String email) {

        return tripMemberRepository.findByUserEmail(email)
                .stream()
                .map(TripMember::getTrip)
                .toList();
    }

    // =========================================================
    // Add a member to a trip
    // Only the OWNER can perform this action
    // =========================================================

    @Transactional
    public TripMember addMember(
            Long tripId,
            String memberEmail,
            TripRole role,
            String requesterEmail) {

        // Make sure the trip exists
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new TripNotFoundException("Trip not found")
                );

        // Find the requester's membership in this trip
        TripMember requesterMembership =
                tripMemberRepository
                        .findByTripIdAndUserEmail(
                                tripId,
                                requesterEmail
                        )
                        .orElseThrow(() ->
                                new ForbiddenTripActionException(
                                        "You do not have access to this trip"
                                )
                        );

        // Only OWNER can add members
        if (requesterMembership.getRole() != TripRole.OWNER) {
            throw new ForbiddenTripActionException(
                    "Only the trip owner can add members"
            );
        }

        // Do not allow another OWNER to be created
        if (role == TripRole.OWNER) {
            throw new InvalidTripRoleException(
                    "Cannot add another owner"
            );
        }

        // Prevent duplicate membership
        if (tripMemberRepository.existsByTripIdAndUserEmail(
                tripId,
                memberEmail)) {

            throw new TripMemberAlreadyExistsException(
                    "User is already a member of this trip"
            );
        }

        // The user being added must already have a Roamly account
        User member = userRepository.findByEmail(memberEmail)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        // Create the membership
        TripMember membership = TripMember.builder()
                .trip(trip)
                .user(member)
                .role(role)
                .build();

        return tripMemberRepository.save(membership);
    }
    @Transactional(readOnly = true)
    public List<TripMember> getTripMembers(
            Long tripId,
            String requesterEmail) {

        tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new TripNotFoundException("Trip not found")
                );

        if (!tripMemberRepository.existsByTripIdAndUserEmail(
                tripId,
                requesterEmail)) {

            throw new ForbiddenTripActionException(
                    "You do not have access to this trip"
            );
        }

        return tripMemberRepository.findByTripId(tripId);
    }
}