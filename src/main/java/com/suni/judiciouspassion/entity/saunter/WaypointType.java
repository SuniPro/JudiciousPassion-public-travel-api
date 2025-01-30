package com.suni.judiciouspassion.entity.saunter;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum WaypointType {
    @JsonProperty("start") START,
    @JsonProperty("stop") STOP,
    @JsonProperty("end") END;
}