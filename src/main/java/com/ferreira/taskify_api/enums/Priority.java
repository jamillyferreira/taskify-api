package com.ferreira.taskify_api.enums;

import lombok.Getter;

public enum Priority {
    LOW("Baixa"),
    MEDIUM("Média"),
    HIGH("Alta");

    private final String description;

    Priority(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
