package com.unieus.garajea.model.dao;

import java.sql.SQLException;

/**
 * UnitOfWork patroia inplementatzeko interfazea.
 * Transakzio baten barruko operazio guztiak kudeatzen ditu.
 */
public interface UnitOfWork extends AutoCloseable {

    /**
     * Transakzioa hasten du.
     * @throws SQLException errorea gertatzen bada
     */
    void begin() throws SQLException;

    /**
     * Transakzioa burutu eta aldaketak egonkortzen ditu.
     * @throws SQLException errorea gertatzen bada
     */
    void commit() throws SQLException;
    /**
     * Transakzioa bertan behera uzten du, aldaketak ezabatuz.
     * @throws SQLException errorea gertatzen bada
     */
    void rollback() throws SQLException;

    /**
     * UnitOfWork itxi eta baliabideak askatzen ditu.
     */
    @Override
    void close();
    
    // UnitOfWork-era lotutako DAO-ak lortzeko metodoak

    /**
     * ErreserbaDAO eskuratzen du, UnitOfWork-aren konexioa erabiliz.
     * @return ErreserbaDAO
     */
    ErreserbaDAO getErreserbaDAO();

    /**
     * MaterialaDAO eskuratzen du, UnitOfWork-aren konexioa erabiliz.
     * @return MaterialaDAO
     */ 
    MaterialaDAO getMaterialaDAO();
}