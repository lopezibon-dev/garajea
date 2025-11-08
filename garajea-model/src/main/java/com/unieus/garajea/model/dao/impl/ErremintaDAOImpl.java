package com.unieus.garajea.model.dao.impl;

import com.unieus.garajea.model.dao.ErremintaDAO;
import com.unieus.garajea.model.entities.Erreminta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ErremintaDAO interfazearen JDBC inplementazioa.
 */
public class ErremintaDAOImpl implements ErremintaDAO {

    private Connection conn;

    public ErremintaDAOImpl(Connection conn) {
        this.conn = conn;
    }
    
    // -----------------------------------------------------------------
    // Metodo laguntzailea (Mapper / Mapeatzailea)
    // -----------------------------------------------------------------
    /**
     * ResultSet bat Erreminta objektu bihurtzen du.
     */
    private Erreminta erremintaSortu(ResultSet rs) throws SQLException {
        Erreminta erreminta = new Erreminta();
        
        erreminta.setErremintaId(rs.getInt("erreminta_id")); 
        erreminta.setIzena(rs.getString("izena"));
        erreminta.setMota(rs.getString("mota"));
        
        return erreminta;
    }

    // -----------------------------------------------------------------
    // CRUD Metodoak
    // -----------------------------------------------------------------
    
    @Override
    public void save(Erreminta erreminta) {
        String sql = "INSERT INTO ERREMINTA (izena, mota) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, erreminta.getIzena());
            ps.setString(2, erreminta.getMota());
            
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        erreminta.setErremintaId(rs.getInt(1)); // PK sortua esleitu
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Erreminta gordetzean: " + e.getMessage());
        }
    }
    
    @Override
    public void update(Erreminta erreminta) {
        String sql = "UPDATE ERREMINTA SET izena = ?, mota = ? WHERE erreminta_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, erreminta.getIzena());
            ps.setString(2, erreminta.getMota());
            ps.setInt(3, erreminta.getErremintaId()); // PK WHERE klausulan
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Erreminta eguneratzean: " + e.getMessage());
        }
    }

    @Override
    public void delete(int erremintaId) {
        String sql = "DELETE FROM ERREMINTA WHERE erreminta_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, erremintaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Erreminta ezabatzean: " + e.getMessage());
        }
    }

    @Override
    public Erreminta findById(int erremintaId) {
        String sql = "SELECT erreminta_id, izena, mota, egoera FROM ERREMINTA WHERE erreminta_id = ?";
        Erreminta erreminta = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, erremintaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    erreminta = erremintaSortu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Erreminta bilatzean: " + e.getMessage());
        }
        return erreminta;
    }

    @Override
    public List<Erreminta> findAll() {
        String sql = "SELECT erreminta_id, izena, mota, egoera FROM ERREMINTA ORDER BY mota, izena";
        List<Erreminta> erremintak = new ArrayList<>();
        try (Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                erremintak.add(erremintaSortu(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errorea Erreminta guztiak bilatzean: " + e.getMessage());
        }
        return erremintak;
    }

    // -----------------------------------------------------------------
    // Metodo Espezifikoak
    // -----------------------------------------------------------------
    
    @Override
    public List<Erreminta> findByMota(String mota) {
        String sql = "SELECT erreminta_id, izena, mota, egoera FROM ERREMINTA WHERE mota = ? ORDER BY izena";
        List<Erreminta> erremintak = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mota);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    erremintak.add(erremintaSortu(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Motaren arabera Erremintak bilatzean: " + e.getMessage());
        }
        return erremintak;
    }

}
