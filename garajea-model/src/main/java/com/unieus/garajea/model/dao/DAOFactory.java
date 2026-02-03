package com.unieus.garajea.model.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DAOFactory abstraktua DAO interfazeak lortzeko.
 */
public abstract class DAOFactory {

    // Klase abstraktuko Logger-a (klase honen metodoak logatzeko)
    private static final Logger LOG = LoggerFactory.getLogger(DAOFactory.class);

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

    // -----------------------------------------------------------------
    // UnitOfWork funtzionalitatea
    // -----------------------------------------------------------------

    /**
     * UnitOfWork bat sortzeko metodo abstraktua (babestua, DAOFactory-ren barne erabilerarako)
     * 
     * @return UnitOfWork
     */
    protected abstract UnitOfWork createUnitOfWork();

    /**
     * Transakzio baten barruan exekutatu beharreko kodea definitzeko interfazea.
     */
    @FunctionalInterface
    public interface TransactionVoidCallback {
        void execute() throws Exception;
    }
    /**
     * Transakzio baten barruan kodea (callback) exekutatzen du eta emaitza itzultzen du.
     */
    @FunctionalInterface
    public interface TransactionCallback<T> {
        T execute() throws Exception;
    }

    /**
     * Transakzio baten barruan kodea (callback) exekutatzen du. Automatikoki kudeatzen du: hasi,
     * exekutatu, bukatu/rollback, itxi.
     * 
     * @param callback TransakzioCallback interfazearen instantzia
     */
    public void executeInTransaction(TransactionVoidCallback callback) {
        try (UnitOfWork uow = createUnitOfWork()) {
            uow.begin();
            callback.execute();
            uow.commit();
        } catch (Exception e) {
            // close() metodoa jada exekutatua (eta beharrezkoa balitz, rollback ere)
            // Logatu errore nagusia
            LOG.error("Errorea transakzioan (void). Mezua: {}", e.getMessage(), e);
            // Berbidali salbuespena RuntimeException gisa
            throw new RuntimeException("Errorea transakzioan (void): " + e.getMessage(), e);
        }
    }

    /**
     * Transakzio baten barruan kodea exekutatzen du eta emaitza itzultzen du. Transakzioaren ziklo
     * osoa automatikoki kudeatzen du: hasi, exekutatu, bukatu/rollback, itxi. Transakzioan errore
     * bat gertatzen bada, rollback egingo da eta RuntimeException bat jaurtiko du.
     * 
     * @param <T> Exekutatu beharreko kodeak itzultzen duen emaitzaren mota (generikoa).
     * @param callback Transakzioan exekutatu beharreko kodea. UnitOfWork bat jasotzen du eta T
     *        motako balio bat itzultzen du.
     * @return Callback-ak itzultzen duen emaitza.
     */
    public <T> T executeInTransaction(TransactionCallback<T> callback) {
        try (UnitOfWork uow = createUnitOfWork()) {
            uow.begin();
            T result = callback.execute();
            uow.commit();
            return result;
        } catch (Exception e) {
            // close() metodoa jada exekutatua (eta beharrezkoa balitz, rollback ere)
            // Logatu errore nagusia
            LOG.error("Errorea transakzioan (emaitzarekin). Mezua: {}", e.getMessage(), e);
            // Berbidali salbuespena RuntimeException gisa; ez da T motako baliorik itzultzen
            throw new RuntimeException("Errorea transakzioan (emaitzarekin): " + e.getMessage(), e);
        }
    }
}
