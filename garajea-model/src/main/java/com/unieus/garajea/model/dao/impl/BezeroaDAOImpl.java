package com.unieus.garajea.model.dao.impl;

import com.unieus.garajea.model.dao.BezeroaDAO;
import com.unieus.garajea.model.entities.Bezeroa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BezeroaDAOImpl implements BezeroaDAO {

    private static final Logger LOG = LoggerFactory.getLogger(BezeroaDAOImpl.class);

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
        bezeroa.setAbizenak(rs.getString("abizenak"));
        bezeroa.setEmaila(rs.getString("emaila"));
        bezeroa.setTelefonoa(rs.getString("telefonoa"));
        bezeroa.setPasahitza(rs.getString("pasahitza"));
        return bezeroa;
    }

    @Override
    public void save(Bezeroa bezeroa) {
        String sql = "INSERT INTO BEZEROA (izena, abizenak, emaila, telefonoa, pasahitza) VALUES (?, ?, ?, ?, ?)";

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
            // logger-a catch blokean erabili (System.err.println -ren ordez)
            LOG.error("Errorea datu-basean Bezeroa gordetzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            
            // runtime salbuespen bat egin, Service geruzak kapturatu dezan
            throw new RuntimeException("Errorea Bezeroaren datuak gordetzean.", e);
        }
    }

    @Override
    public void update(Bezeroa bezeroa) {
        String sql = "UPDATE BEZEROA SET izena = ?, abizenak = ?, emaila = ?, telefonoa = ? WHERE bezeroa_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bezeroa.getIzena());
            ps.setString(2, bezeroa.getAbizenak());
            ps.setString(3, bezeroa.getEmaila());
            ps.setString(4, bezeroa.getTelefonoa());
            ps.setInt(5, bezeroa.getBezeroaId()); // PK WHERE klausulan
            ps.executeUpdate();
        } catch (SQLException e) {
            // logger-a catch blokean erabili (System.err.println -ren ordez)
            LOG.error("Errorea datu-basean Bezeroa eguneratzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            
            // runtime salbuespen bat egin, Service geruzak kapturatu dezan
            throw new RuntimeException("Errorea Bezeroaren datuak eguneratzean.", e);
        }
    }

    @Override
    public void delete(int bezeroaId) {
        String sql = "DELETE FROM BEZEROA WHERE bezeroa_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bezeroaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            // logger-a catch blokean erabili (System.err.println -ren ordez)
            LOG.error("Errorea datu-basean Bezeroa ezabatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            
            // runtime salbuespen bat egin, Service geruzak kapturatu dezan
            throw new RuntimeException("Errorea Bezeroaren datuak ezabatzean.", e);
        }
    }

    @Override
    public Bezeroa findById(int bezeroaId) {
        String sql = "SELECT bezeroa_id, izena, abizenak, emaila, telefonoa, pasahitza FROM BEZEROA WHERE bezeroa_id = ?";
        Bezeroa bezeroa = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bezeroaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    bezeroa = bezeroaSortu(rs);
                }
            }
        } catch (SQLException e) {
            // logger-a catch blokean erabili (System.err.println -ren ordez)
            LOG.error("Errorea datu-basean Bezeroa bilatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            
            // runtime salbuespen bat egin, Service geruzak kapturatu dezan
            throw new RuntimeException("Errorea Bezeroa bilatzean.", e);
        }
        return bezeroa;
    }

    @Override
    public List<Bezeroa> findAll() {
        String sql = "SELECT bezeroa_id, izena, abizenak, emaila, telefonoa, pasahitza FROM BEZEROA ORDER BY abizenak";
        List<Bezeroa> bezeroak = new ArrayList<>();
        try (Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                bezeroak.add(bezeroaSortu(rs));
            }
        } catch (SQLException e) {
            // logger-a catch blokean erabili (System.err.println -ren ordez)
            LOG.error("Errorea datu-basean Bezero guztiak bilatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            
            // runtime salbuespen bat egin, Service geruzak kapturatu dezan
            throw new RuntimeException("Errorea Bezero guztiak bilatzean.", e);
        }
        return bezeroak;
    }

    @Override
    public boolean existitzenDaEmaila(String emaila) {
        String sql = "SELECT COUNT(*) FROM BEZEROA WHERE emaila = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emaila);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            // logger-a catch blokean erabili (System.err.println -ren ordez)
            LOG.error("Errorea emaila existitzen den egiaztatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            
            // runtime salbuespen bat egin, Service geruzak kapturatu dezan
            throw new RuntimeException("Errorea emaila existitzen den egiaztatzean.", e);
        }
        return false;
    }

    @Override
    public Bezeroa getByEmailaPasahitza(String emaila, String pasahitza) {
        String sql = "SELECT * FROM BEZEROA WHERE emaila = ? AND pasahitza = ?";
        Bezeroa bezeroa = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emaila);
            ps.setString(2, pasahitza);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // mapeatzeko metodoa berrerabili
                    bezeroa = bezeroaSortu(rs); 
                }
            }
        } catch (SQLException e) {
            // logger-a catch blokean erabili (System.err.println -ren ordez)
            LOG.error("Errorea emaila eta pasahitzaren arabera bezeroa bilatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            
            // runtime salbuespen bat egin, Service geruzak kapturatu dezan
            throw new RuntimeException("Errorea emaila eta pasahitzaren arabera bezeroa bilatzean.", e);
        }
        return bezeroa;
    }

    @Override
    public void updatePasahitza(int bezeroaId, String pasahitza) {
        String sql = "UPDATE BEZEROA SET pasahitza = ? WHERE bezeroa_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pasahitza);
            ps.setInt(2, bezeroaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            // logger-a catch blokean erabili (System.err.println -ren ordez)
            LOG.error("Errorea Bezeroaren pasahitza eguneratzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            
            // runtime salbuespen bat egin, Service geruzak kapturatu dezan
            throw new RuntimeException("Errorea Bezeroaren pasahitza eguneratzean.", e);
        }
    }    
}
