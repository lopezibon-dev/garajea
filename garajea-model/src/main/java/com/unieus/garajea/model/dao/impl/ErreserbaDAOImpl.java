package com.unieus.garajea.model.dao.impl;

import com.unieus.garajea.model.dao.ErreserbaDAO;
import com.unieus.garajea.model.dao.MaterialaDAO;
import com.unieus.garajea.model.entities.Erreserba;
import com.unieus.garajea.model.entities.Materiala;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ErreserbaDAO interfazearen JDBC inplementazioa.
 */
public class ErreserbaDAOImpl implements ErreserbaDAO {

    private Connection conn;
    // MaterialaDAO bat behar da Materiala objektuak lortzeko (Injekzioa falta da)
    private final MaterialaDAO materialaDAO; 

    public ErreserbaDAOImpl(Connection conn, MaterialaDAO materialaDAO) {
        this.conn = conn;
        this.materialaDAO = materialaDAO;
    }
    
    // -----------------------------------------------------------------
    // Metodo laguntzailea (Mapper / Mapeatzailea)
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
        erreserba.setHasieraDataOrdua(rs.getTimestamp("hasiera_data_ordua").toLocalDateTime());
        erreserba.setAmaieraDataOrdua(rs.getTimestamp("amaiera_data_ordua").toLocalDateTime());
        
        erreserba.setOharrak(rs.getString("oharrak"));
        erreserba.setEgoera(rs.getString("egoera"));
        
        // faktura_id FK-ren NULL balioak kudeatu
        int fakturaId = rs.getInt("faktura_id");
        erreserba.setFakturaId(rs.wasNull() ? null : fakturaId);
        
        return erreserba;
    }

    // -----------------------------------------------------------------
    // CRUD Metodoak
    // -----------------------------------------------------------------
    
    @Override
    public void save(Erreserba erreserba) {
        String sql = "INSERT INTO ERRESERBA (bezeroa_id, ibilgailua_id, kabina_id, langilea_id, hasiera_data_ordua, amaiera_data_ordua, oharrak, egoera, faktura_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            ps.setTimestamp(5, Timestamp.valueOf(erreserba.getHasieraDataOrdua()));
            ps.setTimestamp(6, Timestamp.valueOf(erreserba.getAmaieraDataOrdua()));
            
            ps.setString(7, erreserba.getOharrak());
            ps.setString(8, erreserba.getEgoera());
            
            // faktura_id (NULL kudeatu)
            if (erreserba.getFakturaId() != null) {
                ps.setInt(9, erreserba.getFakturaId());
            } else {
                ps.setNull(9, java.sql.Types.INTEGER);
            }
            
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        erreserba.setErreserbaId(rs.getInt(1)); 
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Erreserba gordetzean: " + e.getMessage());
        }
    }
    
    @Override
    public void update(Erreserba erreserba) {
        String sql = "UPDATE ERRESERBA SET bezeroa_id = ?, ibilgailua_id = ?, kabina_id = ?, langilea_id = ?, hasiera_data_ordua = ?, amaiera_data_ordua = ?, oharrak = ?, egoera = ?, faktura_id = ? WHERE erreserba_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, erreserba.getBezeroaId());
            ps.setInt(2, erreserba.getIbilgailuaId());
            ps.setInt(3, erreserba.getKabinaId());
            
            if (erreserba.getLangileaId() != null) {
                ps.setInt(4, erreserba.getLangileaId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            
            ps.setTimestamp(5, Timestamp.valueOf(erreserba.getHasieraDataOrdua()));
            ps.setTimestamp(6, Timestamp.valueOf(erreserba.getAmaieraDataOrdua()));
            
            ps.setString(7, erreserba.getOharrak());
            ps.setString(8, erreserba.getEgoera());
            
            if (erreserba.getFakturaId() != null) {
                ps.setInt(9, erreserba.getFakturaId());
            } else {
                ps.setNull(9, java.sql.Types.INTEGER);
            }
            
            ps.setInt(10, erreserba.getErreserbaId()); // PK WHERE klausulan
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Erreserba eguneratzean: " + e.getMessage());
        }
    }

    @Override
    public void delete(int erreserbaId) {
        String sql = "DELETE FROM ERRESERBA WHERE erreserba_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, erreserbaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Errorea Erreserba ezabatzean: " + e.getMessage());
        }
    }

    @Override
    public Erreserba findById(int erreserbaId) {
        String sql = "SELECT * FROM ERRESERBA WHERE erreserba_id = ?";
        Erreserba erreserba = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, erreserbaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    erreserba = erreserbaSortu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Erreserba bilatzean: " + e.getMessage());
        }
        return erreserba;
    }

    @Override
    public List<Erreserba> findAll() {
        String sql = "SELECT * FROM ERRESERBA ORDER BY hasiera_data_ordua DESC";
        List<Erreserba> erreserbak = new ArrayList<>();
        try (Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                erreserbak.add(erreserbaSortu(rs));
            }
        } catch (SQLException e) {
            System.err.println("Errorea Erreserba guztiak bilatzean: " + e.getMessage());
        }
        return erreserbak;
    }
    
    // -----------------------------------------------------------------
    // Metodo Espezifikoak
    // -----------------------------------------------------------------

    @Override
    public List<Erreserba> findByBezeroa(int bezeroaId) {
        String sql = "SELECT * FROM ERRESERBA WHERE bezeroa_id = ? ORDER BY hasiera_data_ordua DESC";
        List<Erreserba> erreserbak = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bezeroaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    erreserbak.add(erreserbaSortu(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea Bezeroaren arabera erreserbak bilatzean: " + e.getMessage());
        }
        return erreserbak;
    }

    @Override
    public boolean isKabinaErabilgarri(int kabinaId, LocalDateTime hasiera, LocalDateTime amaiera) {
        // Logika: Bilatu erreserbarik dagoen gure tartea gurutzatzen duena.
        // Gurutzatzen du: (Amaiera > Hasiera Berria) ETA (Hasiera < Amaiera Berria)
        String sql = "SELECT erreserba_id FROM ERRESERBA WHERE kabina_id = ? AND egoera != 'Ezeztatua' AND (? < amaiera_data_ordua) AND (? > hasiera_data_ordua)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kabinaId);
            ps.setTimestamp(2, Timestamp.valueOf(hasiera));
            ps.setTimestamp(3, Timestamp.valueOf(amaiera));
            
            try (ResultSet rs = ps.executeQuery()) {
                // Konklusioa: Erreserba bat aurkitzen bada, kabina EZ dago erabilgarri.
                return !rs.next(); 
            }
        } catch (SQLException e) {
            System.err.println("Errorea kabina erabilgarritasuna egiaztatzean: " + e.getMessage());
            return false;
        }
    }
    
    // -----------------------------------------------------------------
    // N:M Harremana (ERRESERBA_MATERIALA)
    // -----------------------------------------------------------------

    @Override
    public void addMateriala(int erreserbaId, int materialaId, int kopurua) {
        String sql = "INSERT INTO ERRESERBA_MATERIALA (erreserba_id, materiala_id, kopurua) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, erreserbaId);
            ps.setInt(2, materialaId);
            ps.setInt(3, kopurua);
            ps.executeUpdate();
            
            // Hemen, MaterialaDAO erabiliz stocka eguneratzeko logika sartu behar da.
            // MaterialaDAO-k stock_kopurua murriztuko luke.
        } catch (SQLException e) {
            System.err.println("Errorea materiala erreserban gehitzean: " + e.getMessage());
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
            System.err.println("Errorea erreserbako materialak lortzean: " + e.getMessage());
        }
        return materialsMap;
    }
}