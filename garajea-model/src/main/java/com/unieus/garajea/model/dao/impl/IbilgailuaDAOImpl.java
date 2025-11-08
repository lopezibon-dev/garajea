package com.unieus.garajea.model.dao.impl;

import com.unieus.garajea.model.dao.IbilgailuaDAO;
import com.unieus.garajea.model.entities.Ibilgailua;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * IbilgailuaDAO interfazearen JDBC inplementazioa.
 */
public class IbilgailuaDAOImpl implements IbilgailuaDAO {

    private Connection conn;

    public IbilgailuaDAOImpl(Connection conn) {
        this.conn = conn;
    }
    
    // -----------------------------------------------------------------
    // Metodo laguntzailea (Mapper / Mapeatzailea)
    // -----------------------------------------------------------------
    private Ibilgailua ibilgailuaSortu(ResultSet rs) throws SQLException {
        Ibilgailua ibilgailua = new Ibilgailua();
        // PK
        ibilgailua.setIbilgailuaId(rs.getInt("ibilgailua_id")); 
        // Atributuak
        ibilgailua.setMatrikula(rs.getString("matrikula"));
        ibilgailua.setMarka(rs.getString("marka"));
        ibilgailua.setModeloa(rs.getString("modeloa"));
        ibilgailua.setUrtea(rs.getInt("urtea"));
        // FK
        ibilgailua.setBezeroaId(rs.getInt("bezeroa_id")); 
        return ibilgailua;
    }

    // -----------------------------------------------------------------
    // CRUD Metodoak
    // -----------------------------------------------------------------
    
    @Override
    public void save(Ibilgailua ibilgailua) {
        String sql = "INSERT INTO IBILGAILUA (matrikula, marka, modeloa, urtea, bezeroa_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ibilgailua.getMatrikula());
            ps.setString(2, ibilgailua.getMarka());
            ps.setString(3, ibilgailua.getModeloa());
            ps.setInt(4, ibilgailua.getUrtea());
            ps.setInt(5, ibilgailua.getBezeroaId());
            
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        ibilgailua.setIbilgailuaId(rs.getInt(1)); // PK sortua esleitu
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Ibilgailua gordetzean: " + e.getMessage());
            // e.printStackTrace();
        }
    }
    
    @Override
    public void update(Ibilgailua ibilgailua) {
        String sql = "UPDATE IBILGAILUA SET matrikula = ?, marka = ?, modeloa = ?, urtea = ?, bezeroa_id = ? WHERE ibilgailua_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ibilgailua.getMatrikula());
            ps.setString(2, ibilgailua.getMarka());
            ps.setString(3, ibilgailua.getModeloa());
            ps.setInt(4, ibilgailua.getUrtea());
            ps.setInt(5, ibilgailua.getBezeroaId());
            ps.setInt(6, ibilgailua.getIbilgailuaId()); // PK WHERE klausulan
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Ibilgailua eguneratzean: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    @Override
    public void delete(int ibilgailuaId) {
        String sql = "DELETE FROM IBILGAILUA WHERE ibilgailua_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ibilgailuaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Ibilgailua ezabatzean: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    @Override
    public Ibilgailua findById(int ibilgailuaId) {
        String sql = "SELECT ibilgailua_id, matrikula, marka, modeloa, urtea, bezeroa_id FROM IBILGAILUA WHERE ibilgailua_id = ?";
        Ibilgailua ibilgailua = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ibilgailuaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ibilgailua = ibilgailuaSortu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Ibilgailua bilatzean: " + e.getMessage());
            // e.printStackTrace();
        }
        return ibilgailua;
    }

    @Override
    public List<Ibilgailua> findAll() {
        String sql = "SELECT ibilgailua_id, matrikula, marka, modeloa, urtea, bezeroa_id FROM IBILGAILUA ORDER BY matrikula";
        List<Ibilgailua> ibilgailuak = new ArrayList<>();
        try (Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                ibilgailuak.add(ibilgailuaSortu(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errorea Ibilgailu guztiak bilatzean: " + e.getMessage());
            // e.printStackTrace();
        }
        return ibilgailuak;
    }

    // -----------------------------------------------------------------
    // Aurkitzeko Metodo Espezifikoak
    // -----------------------------------------------------------------
    
    @Override
    public Ibilgailua findByMatrikula(String matrikula) {
        String sql = "SELECT ibilgailua_id, matrikula, marka, modeloa, urtea, bezeroa_id FROM IBILGAILUA WHERE matrikula = ?";
        Ibilgailua ibilgailua = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matrikula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ibilgailua = ibilgailuaSortu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Matrikularen arabera ibilgailua bilatzean: " + e.getMessage());
        }
        return ibilgailua;
    }

    @Override
    public List<Ibilgailua> findByBezeroa(int bezeroaId) {
        String sql = "SELECT ibilgailua_id, matrikula, marka, modeloa, urtea, bezeroa_id FROM IBILGAILUA WHERE bezeroa_id = ? ORDER BY urtea DESC";
        List<Ibilgailua> ibilgailuak = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bezeroaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ibilgailuak.add(ibilgailuaSortu(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Bezeroaren arabera ibilgailuak bilatzean: " + e.getMessage());
        }
        return ibilgailuak;
    }
}
