package com.example.jakartaeehelloworld.repository;

import com.example.jakartaeehelloworld.Model.Cours;
import com.example.jakartaeehelloworld.Model.Enseignant;
import com.example.jakartaeehelloworld.Model.Matiere;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class MatiereDAO {
	
	public Optional<Matiere> findById(Session session, Long id) {
	    return Optional.ofNullable(session.get(Matiere.class, id));
	}

	public List<Matiere> findAll(Session session) {
	    String hql = "FROM Matiere";
	    Query<Matiere> query = session.createQuery(hql, Matiere.class);
	    return query.list();
	}
	
	public List<Matiere> findAllByIds(Session session, List<Long> ids) {
	    String hql = "FROM Matiere m WHERE m.id IN (:ids)";
	    Query<Matiere> query = session.createQuery(hql, Matiere.class);
	    query.setParameter("ids", ids);
	    return query.list();
	}

    // Trouver les matières enseignées par un enseignant spécifique (par ID)
    public List<Matiere> findMatiereByEnseignantId(Session session, Long enseignantId) {
        String hql = """
            SELECT m
            FROM Matiere m
            JOIN m.enseignants e
            WHERE e.id = :enseignantId
        """;
        Query<Matiere> query = session.createQuery(hql, Matiere.class);
        query.setParameter("enseignantId", enseignantId);
        return query.list();
    }

    // Méthode similaire pour éviter les doublons (si nécessaire)
    public List<Matiere> findByEnseignantId(Session session, Long enseignantId) {
        return findMatiereByEnseignantId(session, enseignantId);
    }
}
