package com.example.jakartaeehelloworld.repository;

import com.example.jakartaeehelloworld.Model.Enseignant;
import com.example.jakartaeehelloworld.Model.Inscription;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class InscriptionDAO {

	public void save(Session session, Inscription inscription) {
	    session.persist(inscription);
	}
	
    // Trouver une inscription par l'ID de l'étudiant et du cours
    public Optional<Inscription> findByEtudiantIdAndCoursId(Session session, Long etudiantId, Long coursId) {
        String hql = """
            SELECT i
            FROM Inscription i
            WHERE i.etudiant.id = :etudiantId AND i.cours.id = :coursId
        """;
        Query<Inscription> query = session.createQuery(hql, Inscription.class);
        query.setParameter("etudiantId", etudiantId);
        query.setParameter("coursId", coursId);
        return Optional.ofNullable(query.uniqueResult());
    }

    // Supprimer toutes les Inscription associées à un étudiant
    public void deleteAllByEtudiantId(Session session, Long etudiantId) {
        String hql = "DELETE FROM Inscription i WHERE i.etudiant.id = :etudiantId";
        Query<?> query = session.createQuery(hql);
        query.setParameter("etudiantId", etudiantId);
        query.executeUpdate();
    }
}
