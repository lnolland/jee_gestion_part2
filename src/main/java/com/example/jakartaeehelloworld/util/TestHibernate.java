package com.example.jakartaeehelloworld.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestHibernate {
	private static SessionFactory buildSessionFactory() {
	    try {
	        // Test si le fichier hibernate.cfg.xml est accessible
	        if (HibernateUtil.class.getClassLoader().getResource("hibernate.cfg.xml") == null) {
	            throw new RuntimeException("Fichier hibernate.cfg.xml introuvable dans le classpath.");
	        }

	        return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
	    } catch (Throwable ex) {
	        throw new ExceptionInInitializerError("Échec de la création de la SessionFactory : " + ex);
	    }
	}

	
    public static void main(String[] args) {
        SessionFactory sessionFactory = buildSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            System.out.println("Connexion à Hibernate réussie !");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionFactory.close();
        }
    }
}
