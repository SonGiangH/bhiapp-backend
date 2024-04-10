package com.project.bakerhughesapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ToolDTO {
    @JsonProperty("sensor_offset")
    private Float sensorOffset;

    @JsonProperty("default_bur")
    @NotNull(message = "default BUR cannot be empty")
    private Float defaultBur;
}
