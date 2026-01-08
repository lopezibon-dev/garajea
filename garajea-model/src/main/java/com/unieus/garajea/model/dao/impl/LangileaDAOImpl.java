package com.unieus.garajea.model.dao.impl;

import com.unieus.garajea.model.dao.LangileaDAO;
import com.unieus.garajea.model.entities.Langilea;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LangileaDAO interfazearen JDBC inplementazioa.
 */
public class LangileaDAOImpl implements LangileaDAO {

    private static final Logger LOG = LoggerFactory.getLogger(LangileaDAOImpl.class);

    private Connection conn;

    public LangileaDAOImpl(Connection conn) {
        this.conn = conn;
    }
    
    // -----------------------------------------------------------------
    // Metodo laguntzailea (Mapper / Mapeatzailea)
    // -----------------------------------------------------------------
    private Langilea langileaSortu(ResultSet rs) throws SQLException {
        Langilea langilea = new Langilea();
        langilea.setLangileaId(rs.getInt("langilea_id")); 
        langilea.setIzena(rs.getString("izena"));
        langilea.setAbizenak(rs.getString("abizenak"));
        langilea.setKategoria(rs.getString("kategoria"));
        langilea.setTelefonoa(rs.getString("telefonoa"));
        langilea.setEmaila(rs.getString("emaila")); 
        langilea.setPasahitza(rs.getString("pasahitza")); 
        return langilea;
    }

    // -----------------------------------------------------------------
    // CRUD Metodoak
    // -----------------------------------------------------------------
    
    @Override
    public void save(Langilea langilea) {
        String sql = "INSERT INTO LANGILEA (izena, abizenak, kategoria, telefonoa, emaila, pasahitza) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, langilea.getIzena());
            ps.setString(2, langilea.getAbizenak());
            ps.setString(3, langilea.getKategoria());
            ps.setString(4, langilea.getTelefonoa());
            ps.setString(5, langilea.getEmaila());
            ps.setString(6, langilea.getPasahitza());     
            
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        langilea.setLangileaId(rs.getInt(1)); // PK sortua esleitu
                    }
                }
            }
        } catch (SQLException e) {
            //System.err.println("Errorea Langilea gordetzean: " + e.getMessage());
            LOG.error("Errorea datu-basean Langilea gordetzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea Langilearen datuak gordetzean.", e);
        }
    }
    
    @Override
    public void update(Langilea langilea) {
        String sql = "UPDATE LANGILEA SET izena = ?, abizenak = ?, kategoria = ?, telefonoa = ?, emaila = ?, pasahitza = ? WHERE langilea_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, langilea.getIzena());
            ps.setString(2, langilea.getAbizenak());
            ps.setString(3, langilea.getKategoria());
            ps.setString(4, langilea.getTelefonoa());
            ps.setString(5, langilea.getEmaila()); 
            ps.setString(6, langilea.getPasahitza());     
            ps.setInt(7, langilea.getLangileaId()); // PK WHERE klausulan
            ps.executeUpdate();
        } catch (SQLException e) {
            //System.err.println("Errorea Langilea eguneratzean: " + e.getMessage());
            LOG.error("Errorea datu-basean Langilea eguneratzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea Langilearen datuak eguneratzean.", e);            
        }
    }

    @Override
    public void delete(int langileaId) {
        String sql = "DELETE FROM LANGILEA WHERE langilea_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, langileaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            //System.err.println("Errorea Langilea ezabatzean: " + e.getMessage());
            LOG.error("Errorea datu-basean Langilea ezabatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea Langilearen datuak ezabatzean.", e);  
        }
    }

    @Override
    public Langilea findById(int langileaId) {
        String sql = "SELECT * FROM LANGILEA WHERE langilea_id = ?";
        Langilea langilea = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, langileaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    langilea = langileaSortu(rs);
                }
            }
        } catch (SQLException e) {
            //System.err.println("Errorea Langilea bilatzean: " + e.getMessage());
            LOG.error("Errorea datu-basean Langilea bilatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea Langilea bilatzean.", e);
        }
        return langilea;
    }

    @Override
    public List<Langilea> findAll() {
        String sql = "SELECT * FROM LANGILEA ORDER BY abizenak";
        List<Langilea> langileak = new ArrayList<>();
        try (Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                langileak.add(langileaSortu(rs));
            }
        } catch (SQLException e) {
            //System.err.println("Errorea Langile guztiak bilatzean: " + e.getMessage());
            LOG.error("Errorea datu-basean Langile guztiak bilatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea Langile guztiak bilatzean.", e);
        }
        return langileak;
    }

    // -----------------------------------------------------------------
    // Bilaketa Metodo Espezifikoak 
    // -----------------------------------------------------------------
    
    @Override
    public List<Langilea> findByKategoria(String kategoria) {
        String sql = "SELECT * FROM LANGILEA WHERE kategoria = ? ORDER BY abizenak";
        List<Langilea> langileak = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    langileak.add(langileaSortu(rs));
                }
            }
        } catch (SQLException e) {
            //System.err.println("Errorea kategoriaren arabera langileak bilatzean: " + e.getMessage());
            LOG.error("Errorea datu-basean kategoriaren arabera langileak bilatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea kategoriaren arabera langileak bilatzean.", e);

        }
        return langileak;
    }
    
    @Override
    public Langilea getByEmailaPasahitza(String emaila, String pasahitza) {
        String sql = "SELECT * FROM LANGILEA WHERE emaila = ? AND pasahitza = ?";
        Langilea langilea = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emaila);
            ps.setString(2, pasahitza);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    langilea = langileaSortu(rs);
                }
            }
        } catch (SQLException e) {
            //System.err.println("Errorea erabiltzaile izenaren arabera langilea bilatzean: " + e.getMessage());
            LOG.error("Errorea emaila eta pasahitzaren arabera langilea bilatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea emaila eta pasahitzaren arabera langilea bilatzean.", e);

        }
        return langilea;
    }
}