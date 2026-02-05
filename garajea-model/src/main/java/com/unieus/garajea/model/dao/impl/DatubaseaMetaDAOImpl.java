package com.unieus.garajea.model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unieus.garajea.model.dao.DatubaseaMetaDAO;

public class DatubaseaMetaDAOImpl implements DatubaseaMetaDAO {

    private static final Logger LOG =
        LoggerFactory.getLogger(DatubaseaMetaDAOImpl.class);

    private static final String SQL_FIND_TAULA_IZENAK =
        "SELECT table_name " +
        "FROM information_schema.tables " +
        "WHERE table_schema = DATABASE() " +
        "ORDER BY table_name";

    private Connection conn;

    /*
    * DatubaseaMetaDAOImpl sortzailea, konexioa jasotzen du eta klasearen barruan gordetzen du.
    */
    public DatubaseaMetaDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<String> findTaulaIzenak() {

        List<String> taulak = new ArrayList<>();

        try (
            PreparedStatement ps =
                conn.prepareStatement(SQL_FIND_TAULA_IZENAK);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                taulak.add(rs.getString("table_name"));
            }

            return taulak;

        } catch (SQLException e) {
            LOG.error(
                "Errorea datu-baseko taulen izenak eskuratzean. " +
                "Kodea: {}. Mezua: {}",
                e.getErrorCode(),
                e.getMessage(),
                e
            );

            throw new RuntimeException(
                "Errorea datu-baseko taulen izenak eskuratzean.",
                e
            );
        }
    }
}
