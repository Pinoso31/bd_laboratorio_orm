package com.laboratorio.bd_laboratorio_orm.database;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAutil {

    private static EntityManagerFactory entityManagerFactory;

    public static synchronized void iniciar() {

        if (entityManagerFactory == null) {

            entityManagerFactory =
                    Persistence.createEntityManagerFactory("bd_laboratorio_orm");

        }
    }

    public static EntityManagerFactory getEntityManager() {
        iniciar();
        return entityManagerFactory;
    }

    public static synchronized void cerrar() {

        if (entityManagerFactory != null
                && entityManagerFactory.isOpen()) {

            entityManagerFactory.close();
            entityManagerFactory = null;
        }
    }
}
