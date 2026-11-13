package com.laboratorio.bd_laboratorio_orm.dao;

import com.laboratorio.bd_laboratorio_orm.database.JPAutil;
import com.laboratorio.bd_laboratorio_orm.model.Equipo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class EquipoDAO {

    public void guardarEquipo(Equipo equipo) {
        EntityManager em = JPAutil.getEntityManager().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(equipo);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void actualizarEquipo(Equipo equipo) {
        EntityManager em = JPAutil.getEntityManager().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(equipo);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void eliminarEquipo(Equipo equipo) {
        EntityManager em = JPAutil.getEntityManager().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Equipo managedEquipo = em.find(Equipo.class, equipo.getIdEquipo());
            if (managedEquipo != null) {
                em.remove(managedEquipo);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Equipo> listarEquipos() {
        EntityManager em = JPAutil.getEntityManager().createEntityManager();
        try {
            TypedQuery<Equipo> query = em.createQuery(
                "SELECT e FROM Equipo e JOIN FETCH e.tipoEquipo ORDER BY e.nombre", Equipo.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Equipo> buscarPorNombre(String nombre) {
        EntityManager em = JPAutil.getEntityManager().createEntityManager();
        try {
            TypedQuery<Equipo> query = em.createQuery(
                "SELECT e FROM Equipo e JOIN FETCH e.tipoEquipo WHERE LOWER(e.nombre) LIKE LOWER(:nombre) ORDER BY e.nombre", Equipo.class);
            query.setParameter("nombre", "%" + nombre + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Equipo> filtrarPorTipo(Integer idTipo) {
        EntityManager em = JPAutil.getEntityManager().createEntityManager();
        try {
            TypedQuery<Equipo> query = em.createQuery(
                "SELECT e FROM Equipo e JOIN FETCH e.tipoEquipo WHERE e.tipoEquipo.idTipo = :idTipo ORDER BY e.nombre", Equipo.class);
            query.setParameter("idTipo", idTipo);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Equipo> buscarYFiltrar(String nombre, Integer idTipo) {
        EntityManager em = JPAutil.getEntityManager().createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder(
                "SELECT e FROM Equipo e JOIN FETCH e.tipoEquipo WHERE 1=1");
            
            if (nombre != null && !nombre.trim().isEmpty()) {
                jpql.append(" AND LOWER(e.nombre) LIKE LOWER(:nombre)");
            }
            if (idTipo != null) {
                jpql.append(" AND e.tipoEquipo.idTipo = :idTipo");
            }
            jpql.append(" ORDER BY e.nombre");
            
            TypedQuery<Equipo> query = em.createQuery(jpql.toString(), Equipo.class);
            
            if (nombre != null && !nombre.trim().isEmpty()) {
                query.setParameter("nombre", "%" + nombre + "%");
            }
            if (idTipo != null) {
                query.setParameter("idTipo", idTipo);
            }
            
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public boolean existeCodigo(String codigo, Integer idExcluir) {
        EntityManager em = JPAutil.getEntityManager().createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder(
                "SELECT COUNT(e) FROM Equipo e WHERE e.codigo = :codigo");
            if (idExcluir != null) {
                jpql.append(" AND e.idEquipo != :idExcluir");
            }
            TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);
            query.setParameter("codigo", codigo);
            if (idExcluir != null) {
                query.setParameter("idExcluir", idExcluir);
            }
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
}
