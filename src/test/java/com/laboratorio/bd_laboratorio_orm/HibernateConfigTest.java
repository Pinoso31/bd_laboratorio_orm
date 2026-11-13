package com.laboratorio.bd_laboratorio_orm;

import com.laboratorio.bd_laboratorio_orm.database.JPAutil;
import com.laboratorio.bd_laboratorio_orm.model.Equipo;
import com.laboratorio.bd_laboratorio_orm.model.TipoEquipo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HibernateConfigTest {

    private static EntityManager entityManager;

    @BeforeAll
    static void setUp() {
        // Configurar variables de entorno para la prueba
        System.setProperty("DB_URL", "jdbc:postgresql://localhost:5432/bd_laboratorio_orm");
        System.setProperty("DB_USER", "postgres");
        System.setProperty("DB_PASSWORD", "1234");

        JPAutil.iniciar();
        entityManager = JPAutil.getEntityManager().createEntityManager();
    }

    @AfterAll
    static void tearDown() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        JPAutil.cerrar();
    }

    @Test
    void testEntityManagerFactoryInitialization() {
        assertNotNull(JPAutil.getEntityManager());
        assertTrue(JPAutil.getEntityManager().isOpen());
    }

    @Test
    void testConnectionWithPostgreSQL() {
        assertNotNull(entityManager);
        assertTrue(entityManager.isOpen());
    }

    @Test
    void testSchemaRecognition() {
        // Verificar que el esquema laboratorio es reconocido
        // Esto se valida implícitamente al poder consultar las entidades
        assertDoesNotThrow(() -> {
            entityManager.createNativeQuery("SELECT 1 FROM laboratorio.tipo_equipo LIMIT 1").getSingleResult();
        });
    }

    @Test
    void testTipoEquipoEntityRecognition() {
        TypedQuery<TipoEquipo> query = entityManager.createQuery("SELECT t FROM TipoEquipo t", TipoEquipo.class);
        List<TipoEquipo> tipos = query.getResultList();
        
        // La tabla puede estar vacía, pero la entidad debe ser reconocida
        assertNotNull(tipos);
        System.out.println("Tipos de equipo encontrados: " + tipos.size());
        for (TipoEquipo tipo : tipos) {
            System.out.println("  - ID: " + tipo.getIdTipo() + ", Nombre: " + tipo.getNombre() + ", Descripción: " + tipo.getDescription());
        }
    }

    @Test
    void testEquipoEntityRecognition() {
        TypedQuery<Equipo> query = entityManager.createQuery("SELECT e FROM Equipo e", Equipo.class);
        List<Equipo> equipos = query.getResultList();
        
        // La tabla puede estar vacía, pero la entidad debe ser reconocida
        assertNotNull(equipos);
        System.out.println("Equipos encontrados: " + equipos.size());
        for (Equipo equipo : equipos) {
            System.out.println("  - ID: " + equipo.getIdEquipo() + ", Código: " + equipo.getCodigo() + 
                             ", Nombre: " + equipo.getNombre() + ", Tipo: " + 
                             (equipo.getTipoEquipo() != null ? equipo.getTipoEquipo().getNombre() : "null"));
        }
    }

    @Test
    void testEquipoWithTipoEquipoRelation() {
        // Consulta con JOIN FETCH para cargar la relación
        TypedQuery<Equipo> query = entityManager.createQuery(
            "SELECT e FROM Equipo e JOIN FETCH e.tipoEquipo", Equipo.class);
        List<Equipo> equipos = query.getResultList();
        
        assertNotNull(equipos);
        System.out.println("Equipos con tipo relacionado: " + equipos.size());
        for (Equipo equipo : equipos) {
            assertNotNull(equipo.getTipoEquipo(), "El tipo de equipo no debe ser null");
            System.out.println("  - Equipo: " + equipo.getNombre() + 
                             ", Tipo: " + equipo.getTipoEquipo().getNombre());
        }
    }
}