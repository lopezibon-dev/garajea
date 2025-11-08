package com.unieus.garajea.model.dao.impl;

import com.unieus.garajea.model.dao.BezeroaDAO;
import com.unieus.garajea.model.entities.Bezeroa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BezeroaDAOImpl implements BezeroaDAO {

    private Connection conn;

    public BezeroaDAOImpl(Connection conn) {
        this.conn = conn;
    }
    
    // -----------------------------------------------------------------
    // Metodo laguntzailea
    // -----------------------------------------------------------------
    private Bezeroa bezeroaSortu(ResultSet rs) throws SQLException {
        Bezeroa bezeroa = new Bezeroa();
 
        bezeroa.setBezeroaId(rs.getInt("bezeroa_id")); 
        bezeroa.setIzena(rs.getString("izena"));
        bezeroa.setAbizenak(rs.getString("abizena"));
        bezeroa.setEmaila(rs.getString("emaila"));
        bezeroa.setTelefonoa(rs.getString("telefonoa"));
        bezeroa.setPasahitza(rs.getString("pasahitza"));
        return bezeroa;
    }

    // -----------------------------------------------------------------
    // CRUD Metodoak
    // -----------------------------------------------------------------
    
    @Override
    public void save(Bezeroa bezeroa) {
        String sql = "INSERT INTO BEZEROA (izena, abizena, emaila, telefonoa, pasahitza) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, bezeroa.getIzena());
            ps.setString(2, bezeroa.getAbizenak()); 
            ps.setString(3, bezeroa.getEmaila());
            ps.setString(4, bezeroa.getTelefonoa());
            ps.setString(5, bezeroa.getPasahitza());
            
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        // Gako primario sortua esleitu
                        bezeroa.setBezeroaId(rs.getInt(1)); 
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Errorea Bezeroa gordetzean: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    @Override
    public void update(Bezeroa bezeroa) {
        String sql = "UPDATE BEZEROA SET izena = ?, abizena = ?, emaila = ?, telefonoa = ?, pasahitza = ? WHERE bezeroa_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bezeroa.getIzena());
            ps.setString(2, bezeroa.getAbizenak());
            ps.setString(3, bezeroa.getEmaila());
            ps.setString(4, bezeroa.getTelefonoa());
            ps.setString(5, bezeroa.getPasahitza());
            ps.setInt(6, bezeroa.getBezeroaId()); // PK WHERE klausulan
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Bezeroa eguneratzean: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    @Override
    public void delete(int bezeroaId) {
        String sql = "DELETE FROM BEZEROA WHERE bezeroa_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bezeroaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Bezeroa ezabatzean: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    // -----------------------------------------------------------------
    // Bilatzeko Metodo Espezifikoak
    // -----------------------------------------------------------------
    @Override
    public Bezeroa findById(int bezeroaId) {
        String sql = "SELECT bezeroa_id, izena, abizena, emaila, telefonoa, pasahitza FROM BEZEROA WHERE bezeroa_id = ?";
        Bezeroa bezeroa = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bezeroaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    bezeroa = bezeroaSortu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Bezeroa bilatzean: " + e.getMessage());
            // e.printStackTrace();
        }
        return bezeroa;
    }

    @Override
    public List<Bezeroa> findAll() {
        String sql = "SELECT bezeroa_id, izena, abizena, emaila, telefonoa, pasahitza FROM BEZEROA ORDER BY abizena";
        List<Bezeroa> bezeroak = new ArrayList<>();
        try (Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                bezeroak.add(bezeroaSortu(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errorea Bezero guztiak bilatzean: " + e.getMessage());
            // e.printStackTrace();
        }
        return bezeroak;
    }
}
