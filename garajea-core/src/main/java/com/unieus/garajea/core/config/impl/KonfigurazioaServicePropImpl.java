package com.unieus.garajea.core.config.impl;

import com.unieus.garajea.core.config.KonfigurazioaService;
import java.time.LocalTime;
import java.util.Properties;

public class KonfigurazioaServicePropImpl
        implements KonfigurazioaService {

    private final LocalTime LANALDI_HASIERA;
    private final LocalTime LANALDI_AMAIERA;

    public KonfigurazioaServicePropImpl(Properties p) {
        this.LANALDI_HASIERA = LocalTime.parse(p.getProperty("lanaldi.hasiera"));
        this.LANALDI_AMAIERA = LocalTime.parse(p.getProperty("lanaldi.amaiera"));
    }
    @Override
    public LocalTime getLanaldiHasiera() { 
        return LANALDI_HASIERA; 
    }
    
    @Override
    public LocalTime getLanaldiAmaiera() { 
        return LANALDI_AMAIERA; 
    }
}