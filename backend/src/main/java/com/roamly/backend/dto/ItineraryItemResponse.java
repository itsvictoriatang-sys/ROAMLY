package com.roamly.backend.dto;

import com.roamly.backend.entity.ItineraryItem;
import com.roamly.backend.entity.ItineraryItemType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ItineraryItemResponse {

    private Long id;
    private Long tripId;
    private String title;
    private String location;
    private ItineraryItemType type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ItineraryItemResponse from(
            ItineraryItem item) {

        return ItineraryItemResponse.builder()
                .id(item.getId())
                .tripId(item.getTrip().getId())
                .title(item.getTitle())
                .location(item.getLocation())
                .type(item.getType())
                .startTime(item.getStartTime())
                .endTime(item.getEndTime())
                .notes(item.getNotes())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}