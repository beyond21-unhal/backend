package com.team3.notificationservice.dto.request;

import lombok.Builder;

public record MarkAsReadRequest(
        Long notificationId
) {
    @Builder
    public MarkAsReadRequest { }
}
