package com.unieus.garajea.model.dao;

import com.unieus.garajea.model.entities.Bezeroa;
import java.util.List;

public interface BezeroaDAO {

    void save(Bezeroa bezeroa);
    void update(Bezeroa bezeroa); // izena, abizenak, telefonoa, emaila
    void delete(int bezeroaId);
    Bezeroa findById(int bezeroaId);
    List<Bezeroa> findAll();
    boolean existitzenDaEmaila(String emaila);
    Bezeroa getByEmailaPasahitza(String emaila, String pasahitza);
    void updatePasahitza(int bezeroaId, String pasahitza);

}
