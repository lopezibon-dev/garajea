package com.unieus.garajea.model.dao.impl;

import com.unieus.garajea.model.dao.KabinaDAO;
import com.unieus.garajea.model.entities.Kabina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * KabinaDAO interfazearen JDBC inplementazioa.
 */
public class KabinaDAOImpl implements KabinaDAO {

    private Connection conn;

    public KabinaDAOImpl(Connection conn) {
        this.conn = conn;
    }
    
    // -----------------------------------------------------------------
    // Metodo laguntzailea (Mapper / Mapeatzailea)
    // -----------------------------------------------------------------
    /**
     * ResultSet bat Kabina objektu bihurtzen du.
     */
    private Kabina kabinaSortu(ResultSet rs) throws SQLException {
        Kabina kabina = new Kabina();
        // PK
        kabina.setKabinaId(rs.getInt("kabina_id")); 
        // Atributuak
        kabina.setIzena(rs.getString("izena"));
        return kabina;
    }

    // -----------------------------------------------------------------
    // CRUD Metodoak
    // -----------------------------------------------------------------
    
    @Override
    public void save(Kabina kabina) {
        String sql = "INSERT INTO KABINA (izena) VALUES (?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, kabina.getIzena());
            
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        kabina.setKabinaId(rs.getInt(1)); // PK sortua esleitu
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Kabina gordetzean: " + e.getMessage());
            // e.printStackTrace();
        }
    }
    
    @Override
    public void update(Kabina kabina) {
        String sql = "UPDATE KABINA SET izena = ? WHERE kabina_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kabina.getIzena());
            ps.setInt(2, kabina.getKabinaId()); // PK WHERE klausulan
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Kabina eguneratzean: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    @Override
    public void delete(int kabinaId) {
        String sql = "DELETE FROM KABINA WHERE kabina_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kabinaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Kabina ezabatzean: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    @Override
    public Kabina findById(int kabinaId) {
        String sql = "SELECT kabina_id, izena FROM KABINA WHERE kabina_id = ?";
        Kabina kabina = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kabinaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    kabina = kabinaSortu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Kabina bilatzean: " + e.getMessage());
            // e.printStackTrace();
        }
        return kabina;
    }

    @Override
    public List<Kabina> findAll() {
        String sql = "SELECT kabina_id, izena FROM KABINA ORDER BY izena";
        List<Kabina> kabinak = new ArrayList<>();
        try (Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                kabinak.add(kabinaSortu(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errorea Kabina guztiak bilatzean: " + e.getMessage());
            // e.printStackTrace();
        }
        return kabinak;
    }

    // -----------------------------------------------------------------
    // Bilaketa Metodo Espezifikoak
    // -----------------------------------------------------------------
    
    @Override
    public Kabina findByIzena(String izena) {
        String sql = "SELECT kabina_id, izena FROM KABINA WHERE izena = ?";
        Kabina kabina = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, izena);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    kabina = kabinaSortu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Izenaren arabera Kabina bilatzean: " + e.getMessage());
        }
        return kabina;
    }
}
