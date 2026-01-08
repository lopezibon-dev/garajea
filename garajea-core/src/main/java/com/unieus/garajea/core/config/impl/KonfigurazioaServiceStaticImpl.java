package com.unieus.garajea.core.config.impl;

import com.unieus.garajea.core.config.KonfigurazioaService;
import java.time.LocalTime;

public class KonfigurazioaServiceStaticImpl implements KonfigurazioaService {

    private static final LocalTime LANALDI_HASIERA = LocalTime.of(8, 0);
    private static final LocalTime LANALDI_AMAIERA = LocalTime.of(20, 0);

    @Override
    public LocalTime getLanaldiHasiera() {
        return LANALDI_HASIERA;
    }

    @Override
    public LocalTime getLanaldiAmaiera() {
        return LANALDI_AMAIERA;
    }
}