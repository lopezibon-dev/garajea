package com.unieus.garajea.model.dao.impl;

import com.unieus.garajea.model.dao.MaterialaDAO;
import com.unieus.garajea.model.entities.Materiala;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MaterialaDAO interfazearen JDBC inplementazioa.
 */
public class MaterialaDAOImpl implements MaterialaDAO {

    private Connection conn;

    public MaterialaDAOImpl(Connection conn) {
        this.conn = conn;
    }
    
    // -----------------------------------------------------------------
    // Metodo laguntzailea (Mapper / Mapeatzailea)
    // -----------------------------------------------------------------
    /**
     * ResultSet bat Materiala objektu bihurtzen du.
     */
    private Materiala materialaSortu(ResultSet rs) throws SQLException {
        Materiala materiala = new Materiala();
        // PK
        materiala.setMaterialaId(rs.getInt("materiala_id")); 
        // Atributuak
        materiala.setIzena(rs.getString("izena"));
        materiala.setMota(rs.getString("mota"));
        materiala.setPrezioa(rs.getDouble("prezioa")); // DECIMAL -> double
        materiala.setStockKopurua(rs.getInt("stock_kopurua")); 
        return materiala;
    }

    // -----------------------------------------------------------------
    // CRUD Metodoak
    // -----------------------------------------------------------------
    
    @Override
    public void save(Materiala materiala) {
        String sql = "INSERT INTO MATERIALA (izena, mota, prezioa, stock_kopurua) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, materiala.getIzena());
            ps.setString(2, materiala.getMota());
            ps.setDouble(3, materiala.getPrezioa());
            ps.setInt(4, materiala.getStockKopurua());
            
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        materiala.setMaterialaId(rs.getInt(1)); // PK sortua esleitu
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Materiala gordetzean: " + e.getMessage());
            // e.printStackTrace();
        }
    }
    
    @Override
    public void update(Materiala materiala) {
        String sql = "UPDATE MATERIALA SET izena = ?, mota = ?, prezioa = ?, stock_kopurua = ? WHERE materiala_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, materiala.getIzena());
            ps.setString(2, materiala.getMota());
            ps.setDouble(3, materiala.getPrezioa());
            ps.setInt(4, materiala.getStockKopurua());
            ps.setInt(5, materiala.getMaterialaId()); // PK WHERE klausulan
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Materiala eguneratzean: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    @Override
    public void delete(int materialaId) {
        String sql = "DELETE FROM MATERIALA WHERE materiala_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, materialaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Materiala ezabatzean: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    @Override
    public Materiala findById(int materialaId) {
        String sql = "SELECT materiala_id, izena, mota, prezioa, stock_kopurua FROM MATERIALA WHERE materiala_id = ?";
        Materiala materiala = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, materialaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    materiala = materialaSortu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Materiala bilatzean: " + e.getMessage());
            // e.printStackTrace();
        }
        return materiala;
    }

    @Override
    public List<Materiala> findAll() {
        // SELECT eguneratua
        String sql = "SELECT materiala_id, izena, mota, prezioa, stock_kopurua FROM MATERIALA ORDER BY izena";
        List<Materiala> materialak = new ArrayList<>();
        try (Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                materialak.add(materialaSortu(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errorea Material guztiak bilatzean: " + e.getMessage());
            // e.printStackTrace();
        }
        return materialak;
    }

    // -----------------------------------------------------------------
    // Bilatzeko Metodo Espezifikoak
    // -----------------------------------------------------------------
    
    @Override
    public Materiala findByIzena(String izena) {
        // SELECT eguneratua
        String sql = "SELECT materiala_id, izena, mota, prezioa, stock_kopurua FROM MATERIALA WHERE izena = ?";
        Materiala materiala = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, izena);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    materiala = materialaSortu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Izenaren arabera Materiala bilatzean: " + e.getMessage());
        }
        return materiala;
    }
    
    // Material baten stocka eguneratzeko
    @Override
    public void updateStock(int materialaId, int stockKopurua) {
        String sql = "UPDATE MATERIALA SET stock_kopurua = ? WHERE materiala_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, stockKopurua);
            ps.setInt(2, materialaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea stocka eguneratzean: " + e.getMessage());
            // e.printStackTrace();
        }
    }
}
