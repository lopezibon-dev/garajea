package com.unieus.garajea.model.dao;

// Interfaze DAO guztiak inportatu
import com.unieus.garajea.model.dao.impl.*;

/**
 * Fabrika abstraktua DAO interfazeak lortzeko.
 */
public abstract class DAOFactory {
    
    // Metodo abstraktua konexioa itxi ahal izateko (beharrezkoa da)
    public abstract void close(); 
    
    // Metodo abstraktuak DAO guztiak lortzeko
    public abstract BezeroaDAO getBezeroaDAO();
    public abstract LangileaDAO getLangileaDAO();
    public abstract IbilgailuaDAO getIbilgailuaDAO();
    public abstract KabinaDAO getKabinaDAO();
    public abstract MaterialaDAO getMaterialaDAO();
    public abstract ErreserbaDAO getErreserbaDAO();
    public abstract FakturaDAO getFakturaDAO();
    public abstract GoragailuaDAO getGoragailuaDAO();
    public abstract ErremintaDAO getErremintaDAO();
    public abstract MakinaDAO getMakinaDAO();

    /**
     * Fabrika instantzia (JDBC, Hibernate...) lortzeko metodo estatikoa.
     * Faktoriaren inplementazio zehatza hemen erabakitzen da.
     * @return DAOFactory-ren instantzia bat.
     */
    public static DAOFactory getInstance() {
        return new JDBCDAOFactory(); 
    }
}
