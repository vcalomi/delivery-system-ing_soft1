package com.ing_software.tp.exceptions;

import com.ing_software.tp.dto.DividerOrdersResponse;

public class RulesNotSatisfiedException extends RuntimeException {
    private final DividerOrdersResponse response;

    public RulesNotSatisfiedException(DividerOrdersResponse response) {
        this.response = response;
    }

    public DividerOrdersResponse getResponse() {
        return response;
    }
}
