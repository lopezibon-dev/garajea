package com.unieus.garajea.model.dao.impl;

import com.unieus.garajea.model.dao.ErreserbaDAO;
import com.unieus.garajea.model.dao.MaterialaDAO;
import com.unieus.garajea.model.dto.ErreserbaInfoDTO;
import com.unieus.garajea.model.entities.Erreserba;
import com.unieus.garajea.model.entities.Materiala;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * ErreserbaDAO interfazearen JDBC inplementazioa.
 */
public class ErreserbaDAOImpl implements ErreserbaDAO {
    private static final Logger LOG = LoggerFactory.getLogger(ErreserbaDAOImpl.class);
    private Connection conn;
    // MaterialaDAO bat behar da Materiala objektuak lortzeko
    private final MaterialaDAO materialaDAO; 

    public ErreserbaDAOImpl(Connection conn, MaterialaDAO materialaDAO) {
        this.conn = conn;
        this.materialaDAO = materialaDAO;
    }
    
    // -----------------------------------------------------------------
    // Metodo laguntzaileak (Mapper / Mapeatzaileak)
    // -----------------------------------------------------------------
    private Erreserba erreserbaSortu(ResultSet rs) throws SQLException {
        Erreserba erreserba = new Erreserba();
        
        erreserba.setErreserbaId(rs.getInt("erreserba_id")); 
        erreserba.setBezeroaId(rs.getInt("bezeroa_id"));
        erreserba.setIbilgailuaId(rs.getInt("ibilgailua_id"));
        erreserba.setKabinaId(rs.getInt("kabina_id"));
        
        // langilea_id FK-ren NULL balioak kudeatu
        int langileaId = rs.getInt("langilea_id");
        erreserba.setLangileaId(rs.wasNull() ? null : langileaId); 
        
        // LocalDateTime kudeaketa (MySQL 5.6+ eta JDBC 4.2+ behar dira)
        erreserba.setHasiera(rs.getTimestamp("hasiera").toLocalDateTime());
        erreserba.setAmaiera(rs.getTimestamp("amaiera").toLocalDateTime());
        
        erreserba.setOharrak(rs.getString("oharrak"));
        erreserba.setEgoera(rs.getString("egoera"));
        
        return erreserba;
    }

    private ErreserbaInfoDTO erreserbaInfoSortu(ResultSet rs) throws SQLException {
        ErreserbaInfoDTO info = new ErreserbaInfoDTO();
        info.setErreserbaId(rs.getInt("erreserba_id"));
        info.setBezeroaId(rs.getInt("bezeroa_id"));
        info.setIbilgailuaId(rs.getInt("ibilgailua_id"));
        info.setKabinaId(rs.getInt("kabina_id"));
        
        // Langile ID-a NULL izan daiteke
        int lId = rs.getInt("langilea_id");
        info.setLangileaId(rs.wasNull() ? null : lId);

        info.setHasiera(rs.getTimestamp("hasiera").toLocalDateTime());
        info.setAmaiera(rs.getTimestamp("amaiera").toLocalDateTime());
        info.setEgoera(rs.getString("egoera"));
        
        // Alias bidez elkartutako datuak
        info.setBezeroIzenAbizenak(rs.getString("bezero_izenabizenak"));
        info.setLangileIzena(rs.getString("langile_izena"));
        info.setKabinaIzena(rs.getString("kabina_izena"));
        info.setIbilgailuInfo(rs.getString("ibilgailu_info"));
        
        return info;
    }

    // -----------------------------------------------------------------
    // CRUD Metodoak
    // -----------------------------------------------------------------
    
    @Override
    public void save(Erreserba erreserba) {
        String sql = "INSERT INTO erreserba (bezeroa_id, ibilgailua_id, kabina_id, langilea_id, hasiera, amaiera, oharrak, egoera) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, erreserba.getBezeroaId());
            ps.setInt(2, erreserba.getIbilgailuaId());
            ps.setInt(3, erreserba.getKabinaId());
            
            // langilea_id (NULL kudeatu)
            if (erreserba.getLangileaId() != null) {
                ps.setInt(4, erreserba.getLangileaId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            
            // LocalDateTime -> Timestamp
            ps.setTimestamp(5, Timestamp.valueOf(erreserba.getHasiera()));
            ps.setTimestamp(6, Timestamp.valueOf(erreserba.getAmaiera()));
            
            ps.setString(7, erreserba.getOharrak());
            ps.setString(8, erreserba.getEgoera());
                        
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        erreserba.setErreserbaId(rs.getInt(1)); 
                    }
                }
            }
        } catch (SQLException e) {
            LOG.error("Errorea datu-basean Erreserba gordetzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea Erreserba gordetzean.", e);
        }
    }
    
    @Override
    public void update(Erreserba erreserba) {
        String sql = "UPDATE erreserba SET bezeroa_id = ?, ibilgailua_id = ?, kabina_id = ?, langilea_id = ?, hasiera = ?, amaiera = ?, oharrak = ?, egoera = ? WHERE erreserba_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, erreserba.getBezeroaId());
            ps.setInt(2, erreserba.getIbilgailuaId());
            ps.setInt(3, erreserba.getKabinaId());
            
            if (erreserba.getLangileaId() != null) {
                ps.setInt(4, erreserba.getLangileaId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            
            ps.setTimestamp(5, Timestamp.valueOf(erreserba.getHasiera()));
            ps.setTimestamp(6, Timestamp.valueOf(erreserba.getAmaiera()));
            ps.setString(7, erreserba.getOharrak());
            ps.setString(8, erreserba.getEgoera());
            ps.setInt(9, erreserba.getErreserbaId()); // PK WHERE klausulan
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Errorea datu-basean Erreserba eguneratzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea Erreserba eguneratzean.", e);
        }
    }

    @Override
    public void delete(int erreserbaId) {
        String sql = "DELETE FROM erreserba WHERE erreserba_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, erreserbaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Errorea datu-basean Erreserba ezabatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea Erreserba ezabatzean.", e);
        }
    }

    @Override
    public Erreserba findByErreserbaId(int erreserbaId) {
        String sql = "SELECT * FROM erreserba WHERE erreserba_id = ?";
        Erreserba erreserba = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, erreserbaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    erreserba = erreserbaSortu(rs);
                }
            }
        } catch (SQLException e) {
            LOG.error("Errorea datu-basean Erreserba bilatzean (ID). Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea Erreserba bilatzean.", e);
        }
        return erreserba;
    }

    @Override
    public List<Erreserba> findAll() {
        String sql = "SELECT * FROM erreserba ORDER BY hasiera ASC";
        List<Erreserba> erreserbak = new ArrayList<>();
        try (Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                erreserbak.add(erreserbaSortu(rs));
            }
        } catch (SQLException e) {
            LOG.error("Errorea datu-basean Erreserba guztiak bilatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea erreserba zerrenda lortzean.", e);
        }
        return erreserbak;
    }
    
    // -----------------------------------------------------------------
    // Metodo Espezifikoak
    // -----------------------------------------------------------------

    @Override
    public List<Erreserba> bilatuErreserbaLista(Integer langileId, Integer kabinaId, String egoera, LocalDate hasiera, LocalDate amaiera) {
        // SQL kontsulta dinamikoa eraiki
        StringBuilder sql = new StringBuilder("SELECT * FROM erreserba WHERE 1=1");
        List<Object> parametroak = new ArrayList<>();

        if (langileId != null) {
            sql.append(" AND langilea_id = ?");
            parametroak.add(langileId);
        }
        if (kabinaId != null) {
            sql.append(" AND kabina_id = ?");
            parametroak.add(kabinaId);
        }
        if (egoera != null) {
            sql.append(" AND egoera = ?");
            parametroak.add(egoera);
        }
        if (hasiera != null) {
            sql.append(" AND amaiera >= ?"); 
            parametroak.add(java.sql.Timestamp.valueOf(hasiera.atStartOfDay()));
        }
        if (amaiera != null) {
            sql.append(" AND hasiera <= ?");
            parametroak.add(java.sql.Timestamp.valueOf(amaiera.atTime(23, 59, 59)));
        }

        sql.append(" ORDER BY hasiera ASC");

        List<Erreserba> erreserbaZerrenda = new ArrayList<>();

        // Try-with-resources erabili baliabideak automatikoki ixteko
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            // Parametroak PreparedStatement-ean txertatu dinamikoki
            for (int i = 0; i < parametroak.size(); i++) {
                ps.setObject(i + 1, parametroak.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // erreserbaSortu metodoak ResultSet-a Erreserba objektu bihurtzen du
                    erreserbaZerrenda.add(erreserbaSortu(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Errorea datu-basean erreserbak bilatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea erreserbak bilatzean (bilaketa dinamikoa).", e);
        }

        return erreserbaZerrenda;
    }

    @Override
    public List<ErreserbaInfoDTO> bilatuErreserbaInfoLista(Integer langileId, Integer kabinaId, String egoera, LocalDate hasiera, LocalDate amaiera) {
        // SQL kontsulta dinamikoa eraiki
        StringBuilder sql = new StringBuilder(
            "SELECT e.*, " +
            "CONCAT(b.izena, ' ', b.abizenak) AS bezero_izenabizenak, " +
            "l.izena AS langile_izena, " +
            "k.izena AS kabina_izena, " +
            "CONCAT(i.marka, ' ', i.modeloa, ' (', i.matrikula, ')') AS ibilgailu_info " +
            "FROM erreserba e " +
            "INNER JOIN bezeroa b ON e.bezeroa_id = b.bezeroa_id " +
            "INNER JOIN kabina k ON e.kabina_id = k.kabina_id " +
            "INNER JOIN ibilgailua i ON e.ibilgailua_id = i.ibilgailua_id " +
            "LEFT JOIN langilea l ON e.langilea_id = l.langilea_id " +
            "WHERE 1=1"
        );

        List<Object> parametroak = new ArrayList<>();

        if (langileId != null) {
            sql.append(" AND e.langilea_id = ?");
            parametroak.add(langileId);
        }
        if (kabinaId != null) {
            sql.append(" AND e.kabina_id = ?");
            parametroak.add(kabinaId);
        }
        if (egoera != null) {
            sql.append(" AND e.egoera = ?");
            parametroak.add(egoera);
        }
        if (hasiera != null) {
            sql.append(" AND e.amaiera >= ?"); 
            parametroak.add(java.sql.Timestamp.valueOf(hasiera.atStartOfDay()));
        }
        if (amaiera != null) {
            sql.append(" AND e.hasiera <= ?");
            parametroak.add(java.sql.Timestamp.valueOf(amaiera.atTime(23, 59, 59)));
        }

        sql.append(" ORDER BY e.hasiera ASC"); 

        List<ErreserbaInfoDTO> erreserbaInfoZerrenda = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametroak.size(); i++) {
                ps.setObject(i + 1, parametroak.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    erreserbaInfoZerrenda.add(erreserbaInfoSortu(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Errorea datu-basean ErreserbaInfo bilatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea ErreserbaInfo bilatzean (bilaketa dinamikoa).", e);
        }
        return erreserbaInfoZerrenda;
    }

    @Override
    public List<Erreserba> findByBezeroa(int bezeroaId) {
        String sql = "SELECT * FROM erreserba WHERE bezeroa_id = ? ORDER BY hasiera DESC";
        List<Erreserba> erreserbak = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bezeroaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    erreserbak.add(erreserbaSortu(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Errorea datu-basean bezeroaren erreserbak bilatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea bezeroaren erreserbak bilatzean.", e);
        }
        return erreserbak;
    }

    @Override
    public List<Integer> bilatuKabinaOkupatuak(
        LocalDateTime tarteHasiera,
        LocalDateTime tarteAmaiera) {
            
        String sql = """
            SELECT DISTINCT kabina_id
            FROM ERRESERBA
            WHERE 
            egoera != 'Ezeztatua'
            AND hasiera < ?
            AND amaiera > ?
            """;

        List<Integer> emaitza = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(tarteHasiera));
            ps.setTimestamp(2, Timestamp.valueOf(tarteAmaiera));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    emaitza.add(rs.getInt("kabina_id"));
                }
            } 
        } catch (SQLException e) {
            LOG.error("Errorea datu-basean kabina okupatuak bilatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea datu-basean lan egitean.", e);
        }
        return emaitza;
    }

    @Override
    public List<Integer> bilatuMekanikariOkupatuak(
            LocalDateTime tarteHasiera,
            LocalDateTime tarteAmaiera) {

        String sql = """
            SELECT DISTINCT langilea_id
            FROM ERRESERBA
            WHERE langilea_id IS NOT NULL
            AND egoera != 'Ezeztatua'
            AND hasiera < ?
            AND amaiera > ?
            """;

        List<Integer> emaitza = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(tarteAmaiera));
            ps.setTimestamp(2, Timestamp.valueOf(tarteHasiera));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    emaitza.add(rs.getInt("langilea_id"));
                }
            }

        } catch (SQLException e) {
            LOG.error("Errorea datu-basean mekanikari okupatuak bilatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea datu-basean lan egitean.", e);
        }

        return emaitza;
    }

    @Override
    public boolean isKabinaErabilgarri(int kabinaId, LocalDateTime hasiera, LocalDateTime amaiera) {
        // Logika: Bilatu erreserbarik dagoen gure tartea gurutzatzen duena.
        // Gurutzatzen du: (Amaiera > Hasiera Berria) ETA (Hasiera < Amaiera Berria)
        String sql = "SELECT erreserba_id FROM ERRESERBA WHERE kabina_id = ? AND egoera != 'Ezeztatua' AND (? < amaiera) AND (? > hasiera)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kabinaId);
            ps.setTimestamp(2, Timestamp.valueOf(hasiera));
            ps.setTimestamp(3, Timestamp.valueOf(amaiera));
            
            try (ResultSet rs = ps.executeQuery()) {
                // Erreserba bat aurkitzen bada, kabina EZ dago erabilgarri.
                return !rs.next(); 
            }
        } catch (SQLException e) {
            LOG.error("Errorea datu-basean kabina-erabilgarritasuna egiaztatzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea datu-basean lan egitean.", e);
        }
    }
    
    // -----------------------------------------------------------------
    // N:M Harremana (ERRESERBA_MATERIALA)
    // -----------------------------------------------------------------

    /**
     * Erreserba batean erosi diren materialak gehitzen ditu taula laguntzailera.
     * @param erreserbaId erreserbaren IDa.
     * @param materialaId erositako materialaren IDa.
     * @param kopurua erositako kopurua.
     */
    @Override
    public void gehituMateriala(int erreserbaId, int materialaId, int kopurua) {
        String sql = "INSERT INTO ERRESERBA_MATERIALA (erreserba_id, materiala_id, kopurua) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, erreserbaId);
            ps.setInt(2, materialaId);
            ps.setInt(3, kopurua);
            ps.executeUpdate();
            
            // Hautazkoa: hemen, MaterialaDAO erabiliz stock-a eguneratuko litzateke.
        } catch (SQLException e) {
            LOG.error("Errorea materiala erreserbarekin lotzean (taula laguntzailean). Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea materiala erreserbarekin lotzean (taula laguntzailean).", e);
        }
    }

    @Override
    public Map<Materiala, Integer> getMaterialakByErreserba(int erreserbaId) {
        // MaterialaDAO erabiltzen du Materiala objektua lortzeko
        String sql = "SELECT materiala_id, kopurua FROM ERRESERBA_MATERIALA WHERE erreserba_id = ?";
        Map<Materiala, Integer> materialsMap = new HashMap<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, erreserbaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int materialaId = rs.getInt("materiala_id");
                    int kopurua = rs.getInt("kopurua");
                    
                    // Materiala kargatu bere DAO-tik
                    Materiala materiala = materialaDAO.findById(materialaId);
                    
                    if (materiala != null) {
                        materialsMap.put(materiala, kopurua);
                    }
                }
            }
        } catch (SQLException e) {
            LOG.error("Errorea erreserbako materialak lortzean. Kodea: {}. Mezua: {}", e.getErrorCode(), e.getMessage(), e);
            throw new RuntimeException("Errorea erreserbako materialen zerrenda lortzean.", e);
        }
        return materialsMap;
    }
}