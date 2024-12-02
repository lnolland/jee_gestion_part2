package com.example.jakartaeehelloworld.repository;

import com.example.jakartaeehelloworld.Model.Cours;
import com.example.jakartaeehelloworld.Model.Etudiant;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class EtudiantDAO {

	public Optional<Etudiant> findById(Session session, Long id) {
	    return Optional.ofNullable(session.get(Etudiant.class, id));
	}

	public void save(Session session, Etudiant etudiant) {
	    session.persist(etudiant);
	}
	
	public void delete(Session session, Etudiant etudiant) {
	    session.remove(etudiant);
	}
	
	public List<Etudiant> findAll(Session session) {
	    String hql = "FROM Etudiant";
	    Query<Etudiant> query = session.createQuery(hql, Etudiant.class);
	    return query.list();
	}
	
    // Trouver des étudiants par l'ID de la matière associée à leurs cours
    public List<Etudiant> findEtudiantsByCoursMatiereId(Session session, Long matiereId) {
        String hql = """
            SELECT DISTINCT i.etudiant
            FROM Inscription i
            WHERE i.cours.matiere.id = :matiereId
        """;
        Query<Etudiant> query = session.createQuery(hql, Etudiant.class);
        query.setParameter("matiereId", matiereId);
        return query.list();
    }

    // Trouver des étudiants par l'ID de leur matière associée via des notes
    public List<Etudiant> findByMatiere(Session session, Long matiereId) {
        String hql = """
            SELECT DISTINCT n.etudiant
            FROM Note n
            WHERE n.matiere.id = :matiereId
        """;
        Query<Etudiant> query = session.createQuery(hql, Etudiant.class);
        query.setParameter("matiereId", matiereId);
        return query.list();
    }

    // Rechercher des étudiants par leur nom ou prénom contenant un mot-clé
    public List<Etudiant> searchByNomOrPrenom(Session session, String keyword) {
        String hql = """
            SELECT DISTINCT e
            FROM Etudiant e
            WHERE e.nom LIKE :keyword OR e.prenom LIKE :keyword
        """;
        Query<Etudiant> query = session.createQuery(hql, Etudiant.class);
        query.setParameter("keyword", "%" + keyword + "%");
        return query.list();
    }

    // Trouver un étudiant par son nom, prénom et contact
    public Optional<Etudiant> findByNomPrenomContact(Session session, String nom, String prenom, String contact) {
        String hql = """
            SELECT e
            FROM Etudiant e
            WHERE e.nom = :nom AND e.prenom = :prenom AND e.contact = :contact
        """;
        Query<Etudiant> query = session.createQuery(hql, Etudiant.class);
        query.setParameter("nom", nom);
        query.setParameter("prenom", prenom);
        query.setParameter("contact", contact);
        return Optional.ofNullable(query.uniqueResult());
    }
}
