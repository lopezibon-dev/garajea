package com.unieus.garajea.model.dao.impl;

import com.unieus.garajea.model.dao.LangileaDAO;
import com.unieus.garajea.model.entities.Langilea;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * LangileaDAO interfazearen JDBC inplementazioa.
 */
public class LangileaDAOImpl implements LangileaDAO {

    private Connection conn;

    public LangileaDAOImpl(Connection conn) {
        this.conn = conn;
    }
    
    // -----------------------------------------------------------------
    // Metodo laguntzailea (Mapper / Mapeatzailea)
    // -----------------------------------------------------------------
    private Langilea langileaSortu(ResultSet rs) throws SQLException {
        Langilea langilea = new Langilea();
        // PK
        langilea.setLangileaId(rs.getInt("langilea_id")); 
        // Atributuak
        langilea.setIzena(rs.getString("izena"));
        langilea.setAbizena(rs.getString("abizena"));
        langilea.setLanpostua(rs.getString("lanpostua"));
        // Kredentzial berriak
        langilea.setErabiltzailea(rs.getString("erabiltzailea")); 
        langilea.setPasahitza(rs.getString("pasahitza")); 
        return langilea;
    }

    // -----------------------------------------------------------------
    // CRUD Metodoak (Eguneratuak / Actualizados)
    // -----------------------------------------------------------------
    
    @Override
    public void save(Langilea langilea) {
        // SQL eguneratua (erabiltzailea eta pasahitza gehitu dira)
        String sql = "INSERT INTO LANGILEA (izena, abizena, lanpostua, erabiltzailea, pasahitza) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, langilea.getIzena());
            ps.setString(2, langilea.getAbizena());
            ps.setString(3, langilea.getLanpostua());
            ps.setString(4, langilea.getErabiltzailea());
            ps.setString(5, langilea.getPasahitza());     
            
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        langilea.setLangileaId(rs.getInt(1)); // PK sortua esleitu
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Langilea gordetzean: " + e.getMessage());
            // e.printStackTrace();
        }
    }
    
    @Override
    public void update(Langilea langilea) {
        String sql = "UPDATE LANGILEA SET izena = ?, abizena = ?, lanpostua = ?, erabiltzailea = ?, pasahitza = ? WHERE langilea_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, langilea.getIzena());
            ps.setString(2, langilea.getAbizena());
            ps.setString(3, langilea.getLanpostua());
            ps.setString(4, langilea.getErabiltzailea()); 
            ps.setString(5, langilea.getPasahitza());     
            ps.setInt(6, langilea.getLangileaId()); // PK WHERE klausulan
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Langilea eguneratzean: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    @Override
    public void delete(int langileaId) {
        String sql = "DELETE FROM LANGILEA WHERE langilea_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, langileaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Langilea ezabatzean: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    @Override
    public Langilea findById(int langileaId) {
        String sql = "SELECT langilea_id, izena, abizena, lanpostua, erabiltzailea, pasahitza FROM LANGILEA WHERE langilea_id = ?";
        Langilea langilea = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, langileaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    langilea = langileaSortu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Langilea bilatzean: " + e.getMessage());
            // e.printStackTrace();
        }
        return langilea;
    }

    @Override
    public List<Langilea> findAll() {
        String sql = "SELECT langilea_id, izena, abizena, lanpostua, erabiltzailea, pasahitza FROM LANGILEA ORDER BY abizena";
        List<Langilea> langileak = new ArrayList<>();
        try (Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                langileak.add(langileaSortu(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errorea Langile guztiak bilatzean: " + e.getMessage());
            // e.printStackTrace();
        }
        return langileak;
    }

    // -----------------------------------------------------------------
    // Bilaketa Metodo Espezifikoak 
    // -----------------------------------------------------------------
    
    @Override
    public List<Langilea> findByLanpostua(String lanpostua) {
        String sql = "SELECT langilea_id, izena, abizena, lanpostua, erabiltzailea, pasahitza FROM LANGILEA WHERE lanpostua = ? ORDER BY abizena";
        List<Langilea> langileak = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lanpostua);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    langileak.add(langileaSortu(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Lanpostuaren arabera langileak bilatzean: " + e.getMessage());
        }
        return langileak;
    }
    
    @Override
    public Langilea findByErabiltzailea(String erabiltzailea) {
        String sql = "SELECT langilea_id, izena, abizena, lanpostua, erabiltzailea, pasahitza FROM LANGILEA WHERE erabiltzailea = ?";
        Langilea langilea = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, erabiltzailea);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    langilea = langileaSortu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea erabiltzaile izenaren arabera langilea bilatzean: " + e.getMessage());
        }
        return langilea;
    }
}