package com.unieus.garajea.model.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.unieus.garajea.model.dao.BezeroaDAO;
import com.unieus.garajea.model.dao.DAOFactory;
import com.unieus.garajea.model.dao.ErremintaDAO;
import com.unieus.garajea.model.dao.ErreserbaDAO;
import com.unieus.garajea.model.dao.FakturaDAO;
import com.unieus.garajea.model.dao.GoragailuaDAO;
import com.unieus.garajea.model.dao.IbilgailuaDAO;
import com.unieus.garajea.model.dao.KabinaDAO;
import com.unieus.garajea.model.dao.LangileaDAO;
import com.unieus.garajea.model.dao.MakinaDAO;
import com.unieus.garajea.model.dao.MaterialaDAO;
import com.unieus.garajea.model.dao.UnitOfWork;
import com.unieus.garajea.model.infraestructure.InfraConfig;


/**
 * DAOFactory inplementazioa JDBC erabiliz (MySQL). Konexioa hasieratu eta DAO guztiak sortzeaz
 * arduratzen da. UnitOfWork funtzionalitatea ere gehitzen du.
 */
public class JDBCDAOFactory extends DAOFactory {

    // Konexioaren parametroak
    private final InfraConfig infra;

    // Konexioa mantentzeko atributua, Factory-aren bizitza zikloan.
    private Connection connection;

    // Inplementazio klaseko Logger-a (klase honen metodoak logatzeko)
    private static final Logger LOG = LoggerFactory.getLogger(JDBCDAOFactory.class);

    /**
     * Driver-a kargatu eta konexioa ezarri. Exekuzio garaian errorea gertatzen bada,
     * RuntimeException bat jaurtiko du.
     */
    public JDBCDAOFactory(InfraConfig infra) {
        this.infra = infra;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Konexioa ireki
            this.connection = DriverManager.getConnection(infra.getDbUrl(), infra.getDbUser(),
                    infra.getDbPassword());
        } catch (ClassNotFoundException e) {
            LOG.error("MySQL JDBC Driver ez da aurkitu.", e);
            throw new RuntimeException("MySQL JDBC Driver ez da aurkitu.", e);
        } catch (SQLException e) {
            LOG.error("Errorea datu basearekin konektatzean. Kodea: {}. Mezua: {}",
                    e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea datu basearekin konektatzean: " + e.getMessage(),
                    e);
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
            LOG.error("Errorea datu-base konexioa ixtean. Kodea: {}. Mezua: {}", e.getErrorCode(),
                    e.getMessage(), e);
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
    // -----------------------------------------------------------------
    // UnitOfWork funtzionalitatea
    // -----------------------------------------------------------------

    /**
     * JDBCUnitOfWork klase estatikoa, UnitOfWork interfazearen JDBC inplementazioa dena.
     * Transakzioetarako konexio independentea du.
     */
    public class JDBCUnitOfWork implements UnitOfWork {

        private static final Logger LOG = LoggerFactory.getLogger(JDBCUnitOfWork.class);
        private Connection transactionalConn;
        private boolean transactionActive = false;

        /**
         * Eraikitzailea. Konexio berria sortzen du.
         * 
         * @throws SQLException konexioan errorea gertatzen bada
         */
        public JDBCUnitOfWork() throws SQLException {
            this.transactionalConn = DriverManager.getConnection(infra.getDbUrl(), infra.getDbUser(),
                    infra.getDbPassword());
        }

        @Override
        public void begin() throws SQLException {
            transactionalConn.setAutoCommit(false);
            transactionActive = true;
        }

        @Override
        public void commit() throws SQLException {
            transactionalConn.commit();
            transactionalConn.setAutoCommit(true);
            transactionActive = false;
        }

        @Override
        public void rollback() throws SQLException {
            transactionalConn.rollback();
            transactionalConn.setAutoCommit(true);
            transactionActive = false;
        }

        @Override
        public void close() {
            try {
                // 1.- Rollback automatikoa, transakzioa aktiboa bada
                if (transactionActive) {
                    try {
                        transactionalConn.rollback();
                        transactionalConn.setAutoCommit(true);
                    } catch (SQLException e) {
                        LOG.error("Errorea rollback automatikoa egitean. Kodea: {}. Mezua: {}",
                                e.getErrorCode(), e.getMessage(), e);
                    }
                }
                // 2.- Konexioa itxi
                if (transactionalConn != null && !transactionalConn.isClosed()) {
                    transactionalConn.close();
                }
            } catch (SQLException e) {
                LOG.error("Errorea UnitOfWork konexioa ixterakoan. Kodea: {}. Mezua: {}",
                        e.getErrorCode(), e.getMessage(), e);
            }
        }

        @Override
        public ErreserbaDAO getErreserbaDAO() {
            // Kontuz: MaterialaDAO ere konexio honetatik sortu behar da
            MaterialaDAO materialaDAO = new MaterialaDAOImpl(transactionalConn);
            return new ErreserbaDAOImpl(transactionalConn, materialaDAO);
        }

        @Override
        public MaterialaDAO getMaterialaDAO() {
            return new MaterialaDAOImpl(transactionalConn);
        }
    }

    /**
     * UnitOfWork berri bat sortzen du.
     * 
     * @return UnitOfWork
     */
    @Override
    protected UnitOfWork createUnitOfWork() {
        try {
            return new JDBCUnitOfWork();
        } catch (SQLException e) {
            LOG.error("Ezin da UnitOfWork sortu. Kodea: {}. Mezua: {}", e.getErrorCode(),
                    e.getMessage(), e);
            throw new RuntimeException("UnitOfWork ezin da sortu.", e);
        }
    }
}
