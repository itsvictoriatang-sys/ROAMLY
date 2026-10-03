package com.roamly.backend.dto;

import com.roamly.backend.entity.TripMember;
import com.roamly.backend.entity.TripRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TripMemberResponse {

    private Long id;
    private Long userId;
    private String email;
    private String displayName;
    private TripRole role;
    private LocalDateTime joinedAt;

    public static TripMemberResponse from(TripMember tripMember) {
        return TripMemberResponse.builder()
                .id(tripMember.getId())
                .userId(tripMember.getUser().getId())
                .email(tripMember.getUser().getEmail())
                .displayName(tripMember.getUser().getDisplayName())
                .role(tripMember.getRole())
                .joinedAt(tripMember.getJoinedAt())
                .build();
    }
}