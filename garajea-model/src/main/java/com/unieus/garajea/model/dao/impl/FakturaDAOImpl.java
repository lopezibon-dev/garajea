package com.unieus.garajea.model.dao.impl;

import com.unieus.garajea.model.dao.FakturaDAO;
import com.unieus.garajea.model.entities.Faktura;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FakturaDAO interfazearen JDBC inplementazioa.
 */
public class FakturaDAOImpl implements FakturaDAO {

    private Connection conn;

    public FakturaDAOImpl(Connection conn) {
        this.conn = conn;
    }
    
    // -----------------------------------------------------------------
    // Metodo laguntzailea (Mapper / Mapeatzailea)
    // -----------------------------------------------------------------
    private Faktura fakturaSortu(ResultSet rs) throws SQLException {
        Faktura faktura = new Faktura();
        
        faktura.setFakturaId(rs.getInt("faktura_id")); 
        faktura.setErreserbaId(rs.getInt("erreserba_id"));
        faktura.setZenbatekoa(rs.getDouble("zenbatekoa"));
        
        // LocalDate kudeatu
        faktura.setData(rs.getDate("data").toLocalDate());
        
        return faktura;
    }

    // -----------------------------------------------------------------
    // CRUD Metodoak
    // -----------------------------------------------------------------
    
    @Override
    public void save(Faktura faktura) {
        String sql = "INSERT INTO FAKTURA (erreserba_id, zenbatekoa, data) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, faktura.getErreserbaId());
            ps.setDouble(2, faktura.getZenbatekoa());
            
            // LocalDate -> Date
            ps.setDate(3, Date.valueOf(faktura.getData()));
            
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        faktura.setFakturaId(rs.getInt(1)); // PK sortua esleitu
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Faktura gordetzean: " + e.getMessage());
        }
    }
    
    @Override
    public Faktura findById(int fakturaId) {
        String sql = "SELECT faktura_id, erreserba_id, zenbatekoa, data FROM FAKTURA WHERE faktura_id = ?";
        Faktura faktura = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fakturaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    faktura = fakturaSortu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Faktura bilatzean: " + e.getMessage());
        }
        return faktura;
    }

    @Override
    public List<Faktura> findAll() {
        String sql = "SELECT faktura_id, erreserba_id, zenbatekoa, data FROM FAKTURA ORDER BY data DESC";
        List<Faktura> fakturak = new ArrayList<>();
        try (Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                fakturak.add(fakturaSortu(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errorea Faktura guztiak bilatzean: " + e.getMessage());
        }
        return fakturak;
    }
    
    // -----------------------------------------------------------------
    // Metodo Espezifikoa
    // -----------------------------------------------------------------

    @Override
    public Faktura findByErreserbaId(int erreserbaId) {
        String sql = "SELECT faktura_id, erreserba_id, zenbatekoa, data FROM FAKTURA WHERE erreserba_id = ?";
        Faktura faktura = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, erreserbaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    faktura = fakturaSortu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Erreserba IDaren arabera Faktura bilatzean: " + e.getMessage());
        }
        return faktura;
    }
}
