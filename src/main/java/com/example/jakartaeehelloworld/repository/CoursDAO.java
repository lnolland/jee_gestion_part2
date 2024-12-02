package com.example.jakartaeehelloworld.repository;

import com.example.jakartaeehelloworld.Model.Cours;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class CoursDAO {
	
	public Optional<Cours> findById(Session session, Long id) {
	    return Optional.ofNullable(session.get(Cours.class, id));
	}

	public void save(Session session, Cours Cours) {
	    session.persist(Cours);
	}
	
	public List<Cours> findAll(Session session) {
	    String hql = "FROM Cours";
	    Query<Cours> query = session.createQuery(hql, Cours.class);
	    return query.list();
	}

	public boolean existsById(Session session, Long id) {
	    String hql = "SELECT COUNT(c) FROM Cours c WHERE c.id = :id";
	    Query<Long> query = session.createQuery(hql, Long.class);
	    query.setParameter("id", id);
	    return query.uniqueResult() > 0;
	}

	public void deleteById(Session session, Long id) {
	    String hql = "DELETE FROM Cours c WHERE c.id = :id";
	    Query<?> query = session.createQuery(hql);
	    query.setParameter("id", id);
	    query.executeUpdate();
	}

	
    // Trouver les Cours sans enseignant
    public List<Cours> findByEnseignantIsNull(Session session) {
        String hql = "FROM Cours c WHERE c.enseignant IS NULL";
        Query<Cours> query = session.createQuery(hql, Cours.class);
        return query.list();
    }

    // Trouver les Cours attribués à un enseignant spécifique
    public List<Cours> findByEnseignantId(Session session, Long enseignantId) {
        String hql = "FROM Cours c WHERE c.enseignant.id = :enseignantId";
        Query<Cours> query = session.createQuery(hql, Cours.class);
        query.setParameter("enseignantId", enseignantId);
        return query.list();
    }

    // Trouver les Cours sans enseignant mais associés aux matières d'un enseignant spécifique
    public List<Cours> findCoursSansProfesseurByEnseignantMatiere(Session session, Long enseignantId) {
        String hql = """
            SELECT c
            FROM Cours c
            WHERE c.enseignant IS NULL AND c.matiere IN (
                SELECT m
                FROM Matiere m
                JOIN m.enseignants e
                WHERE e.id = :enseignantId
            )
        """;
        Query<Cours> query = session.createQuery(hql, Cours.class);
        query.setParameter("enseignantId", enseignantId);
        return query.list();
    }

    // Dissocier les Cours d'un enseignant spécifique
    public void dissocierCours(Session session, Long enseignantId) {
        String hql = "UPDATE Cours c SET c.enseignant = NULL WHERE c.enseignant.id = :enseignantId";
        Query<?> query = session.createQuery(hql);
        query.setParameter("enseignantId", enseignantId);
        query.executeUpdate();
    }
}
