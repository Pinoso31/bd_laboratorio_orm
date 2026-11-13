package com.laboratorio.bd_laboratorio_orm.dao;

import com.laboratorio.bd_laboratorio_orm.database.JPAutil;
import com.laboratorio.bd_laboratorio_orm.model.TipoEquipo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TipoEquipoDAO {

    public List<TipoEquipo> listarTodos() {
        EntityManager em = JPAutil.getEntityManager().createEntityManager();
        try {
            TypedQuery<TipoEquipo> query = em.createQuery("SELECT t FROM TipoEquipo t ORDER BY t.nombre", TipoEquipo.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public TipoEquipo buscarPorId(Integer id) {
        EntityManager em = JPAutil.getEntityManager().createEntityManager();
        try {
            return em.find(TipoEquipo.class, id);
        } finally {
            em.close();
        }
    }

    public List<TipoEquipo> obtenerParaComboBox() {
        return listarTodos();
    }
}
