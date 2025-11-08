package com.unieus.garajea.model.dao.impl;

import com.unieus.garajea.model.dao.MakinaDAO;
import com.unieus.garajea.model.entities.Makina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MakinaDAO interfazearen JDBC inplementazioa.
 */
public class MakinaDAOImpl implements MakinaDAO {

    private Connection conn;

    public MakinaDAOImpl(Connection conn) {
        this.conn = conn;
    }
    
    // -----------------------------------------------------------------
    // Metodo laguntzailea (Mapper / Mapeatzailea)
    // -----------------------------------------------------------------
    /**
     * ResultSet bat Makina objektu bihurtzen du.
     */
    private Makina makinaSortu(ResultSet rs) throws SQLException {
        Makina makina = new Makina();
        
        makina.setMakinaId(rs.getInt("makina_id")); 
        makina.setIzena(rs.getString("izena"));
        makina.setMota(rs.getString("mota"));
        makina.setEgoera(rs.getString("egoera"));
        
        int kabinaId = rs.getInt("kabina_id");
        makina.setKabinaId(rs.wasNull() ? null : kabinaId); 
        
        return makina;
    }

    // -----------------------------------------------------------------
    // CRUD Metodoak
    // -----------------------------------------------------------------
    
    @Override
    public void save(Makina makina) {
        String sql = "INSERT INTO MAKINA (izena, mota, egoera, kabina_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, makina.getIzena());
            ps.setString(2, makina.getMota());
            ps.setString(3, makina.getEgoera());
            
            // kabina_id (NULL handling)
            if (makina.getKabinaId() != null) {
                ps.setInt(4, makina.getKabinaId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        makina.setMakinaId(rs.getInt(1)); // PK sortua esleitu
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Makina gordetzean: " + e.getMessage());
        }
    }
    
    @Override
    public void update(Makina makina) {
        String sql = "UPDATE MAKINA SET izena = ?, mota = ?, egoera = ?, kabina_id = ? WHERE makina_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, makina.getIzena());
            ps.setString(2, makina.getMota());
            ps.setString(3, makina.getEgoera());
            
            if (makina.getKabinaId() != null) {
                ps.setInt(4, makina.getKabinaId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            
            ps.setInt(5, makina.getMakinaId()); // PK WHERE klausulan
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Makina eguneratzean: " + e.getMessage());
        }
    }

    @Override
    public void delete(int makinaId) {
        String sql = "DELETE FROM MAKINA WHERE makina_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, makinaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Makina ezabatzean: " + e.getMessage());
        }
    }

    @Override
    public Makina findById(int makinaId) {
        String sql = "SELECT makina_id, izena, mota, egoera, kabina_id FROM MAKINA WHERE makina_id = ?";
        Makina makina = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, makinaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    makina = makinaSortu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Makina bilatzean: " + e.getMessage());
        }
        return makina;
    }

    @Override
    public List<Makina> findAll() {
        String sql = "SELECT makina_id, izena, mota, egoera, kabina_id FROM MAKINA ORDER BY mota, izena";
        List<Makina> makinak = new ArrayList<>();
        try (Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                makinak.add(makinaSortu(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errorea Makina guztiak bilatzean: " + e.getMessage());
        }
        return makinak;
    }

    // -----------------------------------------------------------------
    // Metodo Espezifikoak
    // -----------------------------------------------------------------
    
    @Override
    public List<Makina> findByMota(String mota) {
        String sql = "SELECT makina_id, izena, mota, egoera, kabina_id FROM MAKINA WHERE mota = ? ORDER BY izena";
        List<Makina> makinak = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mota);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    makinak.add(makinaSortu(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Motaren arabera Makinak bilatzean: " + e.getMessage());
        }
        return makinak;
    }

    @Override
    public List<Makina> findByEgoera(String egoera) {
        String sql = "SELECT makina_id, izena, mota, egoera, kabina_id FROM MAKINA WHERE egoera = ? ORDER BY izena";
        List<Makina> makinak = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, egoera);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    makinak.add(makinaSortu(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Egoeraren arabera Makinak bilatzean: " + e.getMessage());
        }
        return makinak;
    }

    @Override
    public List<Makina> findByKabinaId(int kabinaId) {
        String sql = "SELECT makina_id, izena, mota, egoera, kabina_id FROM MAKINA WHERE kabina_id = ? ORDER BY izena";
        List<Makina> makinak = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kabinaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    makinak.add(makinaSortu(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Kabinaren arabera Makinak bilatzean: " + e.getMessage());
        }
        return makinak;
    }
}