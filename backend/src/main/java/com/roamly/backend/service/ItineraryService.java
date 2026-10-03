package com.roamly.backend.service;

import com.roamly.backend.dto.CreateItineraryItemRequest;
import com.roamly.backend.entity.ItineraryItem;
import com.roamly.backend.entity.Trip;
import com.roamly.backend.entity.TripMember;
import com.roamly.backend.entity.TripRole;
import com.roamly.backend.exception.ForbiddenTripActionException;
import com.roamly.backend.exception.InvalidItineraryTimeException;
import com.roamly.backend.exception.TripNotFoundException;
import com.roamly.backend.repository.ItineraryItemRepository;
import com.roamly.backend.repository.TripMemberRepository;
import com.roamly.backend.repository.TripRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItineraryService {

    private final ItineraryItemRepository itineraryItemRepository;
    private final TripRepository tripRepository;
    private final TripMemberRepository tripMemberRepository;

    public ItineraryService(
            ItineraryItemRepository itineraryItemRepository,
            TripRepository tripRepository,
            TripMemberRepository tripMemberRepository) {

        this.itineraryItemRepository = itineraryItemRepository;
        this.tripRepository = tripRepository;
        this.tripMemberRepository = tripMemberRepository;
    }

    @Transactional
    public ItineraryItem createItem(
            Long tripId,
            CreateItineraryItemRequest request,
            String requesterEmail) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new TripNotFoundException("Trip not found")
                );

        TripMember membership =
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

        if (membership.getRole() == TripRole.VIEWER) {
            throw new ForbiddenTripActionException(
                    "Viewers cannot modify the itinerary"
            );
        }

        if (!request.getEndTime().isAfter(
                request.getStartTime())) {

            throw new InvalidItineraryTimeException(
                    "End time must be after start time"
            );
        }

        ItineraryItem item = ItineraryItem.builder()
                .trip(trip)
                .title(request.getTitle())
                .location(request.getLocation())
                .type(request.getType())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .notes(request.getNotes())
                .build();

        return itineraryItemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<ItineraryItem> getItems(
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

        return itineraryItemRepository
                .findByTripIdOrderByStartTimeAsc(tripId);
    }
}