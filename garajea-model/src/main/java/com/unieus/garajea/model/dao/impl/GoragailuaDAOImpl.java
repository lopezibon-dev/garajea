package com.unieus.garajea.model.dao.impl;

import com.unieus.garajea.model.dao.GoragailuaDAO;
import com.unieus.garajea.model.entities.Goragailua;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GoragailuaDAO interfazearen JDBC inplementazioa.
 */
public class GoragailuaDAOImpl implements GoragailuaDAO {

    private Connection conn;

    public GoragailuaDAOImpl(Connection conn) {
        this.conn = conn;
    }
    
    // Metodo laguntzailea (Mapper / Mapeatzailea) 
    private Goragailua goragailuaSortu(ResultSet rs) throws SQLException {
        Goragailua goragailua = new Goragailua();
        
        goragailua.setGoragailuaId(rs.getInt("goragailua_id")); 
        goragailua.setIzena(rs.getString("izena"));
        goragailua.setEgoera(rs.getString("egoera"));
        
        int kabinaId = rs.getInt("kabina_id");
        goragailua.setKabinaId(rs.wasNull() ? null : kabinaId); 
        
        return goragailua;
    }

    // -----------------------------------------------------------------
    // CRUD Metodoak
    // -----------------------------------------------------------------    

@Override
    public void save(Goragailua goragailua) {
        String sql = "INSERT INTO GORAGAILUA (izena, egoera, kabina_id) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, goragailua.getIzena());
            ps.setString(2, goragailua.getEgoera());
            
            // kabina_id (NULL handling)
            if (goragailua.getKabinaId() != null) {
                ps.setInt(3, goragailua.getKabinaId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        goragailua.setGoragailuaId(rs.getInt(1)); // PK sortua esleitu
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Goragailua gordetzean: " + e.getMessage());
        }
    }
    
    @Override
    public void update(Goragailua goragailua) {
        String sql = "UPDATE GORAGAILUA SET izena = ?, egoera = ?, kabina_id = ? WHERE goragailua_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, goragailua.getIzena());
            ps.setString(2, goragailua.getEgoera());
            
            if (goragailua.getKabinaId() != null) {
                ps.setInt(3, goragailua.getKabinaId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            
            ps.setInt(4, goragailua.getGoragailuaId()); // PK WHERE klausulan
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Goragailua eguneratzean: " + e.getMessage());
        }
    }

    @Override
    public void delete(int goragailuaId) {
        String sql = "DELETE FROM GORAGAILUA WHERE goragailua_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, goragailuaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Goragailua ezabatzean: " + e.getMessage());
        }
    }

    @Override
    public Goragailua findById(int goragailuaId) {
        String sql = "SELECT goragailua_id, izena, egoera, kabina_id FROM GORAGAILUA WHERE goragailua_id = ?";
        Goragailua goragailua = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, goragailuaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    goragailua = goragailuaSortu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Goragailua bilatzean: " + e.getMessage());
        }
        return goragailua;
    }

    @Override
    public List<Goragailua> findAll() {
        String sql = "SELECT goragailua_id, izena, egoera, kabina_id FROM GORAGAILUA ORDER BY izena";
        List<Goragailua> goragailuak = new ArrayList<>();
        try (Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                goragailuak.add(goragailuaSortu(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errorea Goragailu guztiak bilatzean: " + e.getMessage());
        }
        return goragailuak;
    }
    
    // -----------------------------------------------------------------
    // Metodo Espezifikoak
    // -----------------------------------------------------------------
    
    @Override
    public List<Goragailua> findByEgoera(String egoera) {
        String sql = "SELECT goragailua_id, izena, egoera, kabina_id FROM GORAGAILUA WHERE egoera = ? ORDER BY izena";
        List<Goragailua> goragailuak = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, egoera);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    goragailuak.add(goragailuaSortu(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea egoeraren arabera Goragailuak bilatzean: " + e.getMessage());
        }
        return goragailuak;
    }

    @Override
    public Goragailua findByKabinaId(int kabinaId) { 
        String sql = "SELECT goragailua_id, izena, egoera, kabina_id FROM GORAGAILUA WHERE kabina_id = ?";
        Goragailua goragailua = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kabinaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    goragailua = goragailuaSortu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea kabinaren arabera Goragailuak bilatzean: " + e.getMessage());
        }
        return goragailua;
    }
}