package com.roamly.backend.controller;

import com.roamly.backend.dto.CreateItineraryItemRequest;
import com.roamly.backend.dto.ItineraryItemResponse;
import com.roamly.backend.entity.ItineraryItem;
import com.roamly.backend.service.ItineraryService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips/{tripId}/itinerary")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(
            ItineraryService itineraryService) {

        this.itineraryService = itineraryService;
    }

    @PostMapping
    public ResponseEntity<ItineraryItemResponse> createItem(
            @PathVariable Long tripId,
            @Valid @RequestBody CreateItineraryItemRequest request,
            Authentication authentication) {

        String requesterEmail = authentication.getName();

        ItineraryItem item = itineraryService.createItem(
                tripId,
                request,
                requesterEmail
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ItineraryItemResponse.from(item));
    }

    @GetMapping
    public ResponseEntity<List<ItineraryItemResponse>> getItems(
            @PathVariable Long tripId,
            Authentication authentication) {

        String requesterEmail = authentication.getName();

        List<ItineraryItemResponse> items =
                itineraryService.getItems(
                        tripId,
                        requesterEmail
                )
                .stream()
                .map(ItineraryItemResponse::from)
                .toList();

        return ResponseEntity.ok(items);
    }
}