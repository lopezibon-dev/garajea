package com.unieus.garajea.core.services.context;

public interface ServiceContextFactory {

    /**
     * Irekitzen du ServiceContext bat (request edo use-case baten bizitza ziklorako)
     */
    ServiceContext open();
}

