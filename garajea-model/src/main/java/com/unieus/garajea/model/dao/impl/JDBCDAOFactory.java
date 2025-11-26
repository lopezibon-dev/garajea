package com.unieus.garajea.model.dao.impl;

import com.unieus.garajea.model.dao.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DAO Fabrika inplementazioa JDBC erabiliz (MySQL).
 * Konexioa hasieratu eta DAO guztiak sortzeaz arduratzen da.
 */
public class JDBCDAOFactory extends DAOFactory {
    
    // Konexioaren parametroak (Produkzioan ez lirateke hemen egon behar)
    // private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/garajea?serverTimezone=Europe/Madrid";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/garajea?serverTimezone=Europe/Madrid";
    //private static final String DB_URL = "jdbc:mysql://localhost:3306/garajea?serverTimezone=UTC+1&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "garajea";
    private static final String DB_PASSWORD = "1234";
    
    // Konexioa mantentzeko atributua, Factory-aren bizitza zikloan.
    private Connection connection; 

    /**
     * Driver-a kargatu eta konexioa ezarri.
     * Exekuzio garaian errorea gertatzen bada, RuntimeException bat jaurtiko du.
     */
    public JDBCDAOFactory() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            // Konexioa ireki
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver ez da aurkitu.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Errorea datu basearekin konektatzean: " + e.getMessage(), e);
        }
    }

    /**
     * Konexioa ixten du baliabideak askatzeko.
     */
    @Override
    public void close() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Errorea datu base konexioa ixten: " + e.getMessage());
        }
    }
    
    // Implementazioak: DAO bakoitza sortu eta Konexioa (this.connection) pasatu
    
    @Override
    public BezeroaDAO getBezeroaDAO() {
        return new BezeroaDAOImpl(connection);
    }
    
    @Override
    public LangileaDAO getLangileaDAO() {
        return new LangileaDAOImpl(connection);
    }
    
    @Override
    public IbilgailuaDAO getIbilgailuaDAO() {
        return new IbilgailuaDAOImpl(connection);
    }
    
    @Override
    public KabinaDAO getKabinaDAO() {
        return new KabinaDAOImpl(connection);
    }
    
    @Override
    public MaterialaDAO getMaterialaDAO() {
        return new MaterialaDAOImpl(connection);
    }
    
    @Override
    public ErreserbaDAO getErreserbaDAO() {
        // ErreserbaDAOImpl-ek MaterialaDAO behar du, beraz, Factoriak ematen dio.
        return new ErreserbaDAOImpl(connection, getMaterialaDAO());
    }
    
    @Override
    public FakturaDAO getFakturaDAO() {
        return new FakturaDAOImpl(connection);
    }
    
    @Override
    public GoragailuaDAO getGoragailuaDAO() {
        return new GoragailuaDAOImpl(connection);
    }
    
    @Override
    public ErremintaDAO getErremintaDAO() {
        return new ErremintaDAOImpl(connection);
    }
    
    @Override
    public MakinaDAO getMakinaDAO() {
        return new MakinaDAOImpl(connection);
    }
}
