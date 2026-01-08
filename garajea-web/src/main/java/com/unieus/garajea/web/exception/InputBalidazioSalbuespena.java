package com.unieus.garajea.web.exception;

import java.util.List;

public class InputBalidazioSalbuespena extends RuntimeException {

    private final List<String> erroreak;

    public InputBalidazioSalbuespena(String errorea) {
        this.erroreak = List.of(errorea);
    }

    public InputBalidazioSalbuespena(List<String> erroreak) {
        this.erroreak = erroreak;
    }

    public List<String> getErroreak() {
        return erroreak;
    }


}
