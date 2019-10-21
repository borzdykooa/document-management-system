package com.borzdykooa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DocumentType {

    SCIENCE_FICTION("Science fiction"),
    POETRY("Poetry"),
    DETECTIVE("Detective");

    private String value;

    public static DocumentType getByValue(String value) {
        return Arrays.stream(values())
                .filter(documentType -> documentType.value.equals(value))
                .findFirst()
                .orElse(null);
    }
}
