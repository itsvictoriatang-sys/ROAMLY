package com.roamly.backend.controller;

import com.roamly.backend.dto.CreateTripRequest;
import com.roamly.backend.dto.TripResponse;
import com.roamly.backend.entity.Trip;
import com.roamly.backend.service.TripService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.roamly.backend.dto.AddTripMemberRequest;
import com.roamly.backend.dto.TripMemberResponse;
import com.roamly.backend.entity.TripMember;


@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<TripResponse> createTrip(
            @Valid @RequestBody CreateTripRequest request,
            Authentication authentication) {

        String creatorEmail = authentication.getName();

        Trip trip = tripService.createTrip(
                request.getName(),
                request.getDestination(),
                request.getStartDate(),
                request.getEndDate(),
                creatorEmail
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TripResponse.from(trip));
    }
    @GetMapping
public ResponseEntity<List<TripResponse>> getMyTrips(
        Authentication authentication) {

    String email = authentication.getName();

    List<TripResponse> trips = tripService.getTripsForUser(email)
            .stream()
            .map(TripResponse::from)
            .toList();

    return ResponseEntity.ok(trips);
}
@PostMapping("/{tripId}/members")
public ResponseEntity<TripMemberResponse> addMember(
        @PathVariable Long tripId,
        @Valid @RequestBody AddTripMemberRequest request,
        Authentication authentication) {

    String requesterEmail = authentication.getName();

    TripMember tripMember = tripService.addMember(
            tripId,
            request.getEmail(),
            request.getRole(),
            requesterEmail
    );

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(TripMemberResponse.from(tripMember));
}
@GetMapping("/{tripId}/members")
public ResponseEntity<List<TripMemberResponse>> getTripMembers(
        @PathVariable Long tripId,
        Authentication authentication) {

    String requesterEmail = authentication.getName();

    List<TripMemberResponse> members =
            tripService.getTripMembers(
                    tripId,
                    requesterEmail
            )
            .stream()
            .map(TripMemberResponse::from)
            .toList();

    return ResponseEntity.ok(members);
}
}