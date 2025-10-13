package com.example.resilient_api.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    CAPACITY_TECH_COUNT_INVALID("404", "The number of tech is invalid.", "techIds"),
    TECH_INVALID("404", "One or more techs are invalid.", "techIds"),
    CAPACITY_ALREADY_EXISTS("409", "The capacity already exists.", "name"),
    INVALID_PARAMETERS("400", "Invalid parameters provided.", "parameters");

    private final String code;
    private final String message;
    private final String param;
}