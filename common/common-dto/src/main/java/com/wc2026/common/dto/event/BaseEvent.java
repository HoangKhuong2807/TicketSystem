package com.wc2026.common.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BaseEvent<T>(
        @NotBlank String eventId,
        @NotBlank String eventType,
        @NotBlank String source,
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC") Instant timestamp,
        @NotNull T payload
) {}
