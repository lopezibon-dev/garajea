package com.unieus.garajea.core.exception;

import java.util.List;

public class ZerbitzuSalbuespena extends RuntimeException {

    private final List<String> erroreak;

    public ZerbitzuSalbuespena(String errorea) {
        this.erroreak = List.of(errorea);
    }

    public ZerbitzuSalbuespena(List<String> erroreak) {
        this.erroreak = erroreak;
    }
    
    public List<String> getErroreak() {
        return erroreak;
    }

}