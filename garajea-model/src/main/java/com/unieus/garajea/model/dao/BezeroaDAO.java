package com.unieus.garajea.model.dao;

import com.unieus.garajea.model.entities.Bezeroa;
import java.util.List;

public interface BezeroaDAO {

    void save(Bezeroa bezeroa);
    void update(Bezeroa bezeroa);
    void delete(int bezeroaId);
    Bezeroa findById(int bezeroaId);
    List<Bezeroa> findAll();

}
