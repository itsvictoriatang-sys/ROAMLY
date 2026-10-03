package com.roamly.backend.dto;

import com.roamly.backend.entity.ItineraryItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateItineraryItemRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must not exceed 150 characters")
    private String title;

    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;

    @NotNull(message = "Type is required")
    private ItineraryItemType type;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
}