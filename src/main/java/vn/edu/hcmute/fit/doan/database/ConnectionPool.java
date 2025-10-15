package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ConnectionPool {
    private static EntityManagerFactory emf;

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("doan-unit");
        }
        return emf;
    }

    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}