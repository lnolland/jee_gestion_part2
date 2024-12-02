package com.example.jakartaeehelloworld.repository;

import com.example.jakartaeehelloworld.Model.Note;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class NoteDAO {
	
	public void save(Session session, Note note) {
	    session.persist(note);
	}

	public void delete(Session session, Note note) {
	    session.remove(note);
	}

    // Trouver les notes par étudiant et matière
    public List<Note> findByEtudiantAndMatiere(Session session, Long etudiantId, Long matiereId) {
        String hql = """
            SELECT n
            FROM Note n
            WHERE n.etudiant.id = :etudiantId AND n.matiere.id = :matiereId
        """;
        Query<Note> query = session.createQuery(hql, Note.class);
        query.setParameter("etudiantId", etudiantId);
        query.setParameter("matiereId", matiereId);
        return query.list();
    }

    // Trouver la moyenne des notes par étudiant et matière
    public Double findAverageByEtudiantAndMatiere(Session session, Long etudiantId, Long matiereId) {
        String hql = """
            SELECT AVG(n.note)
            FROM Note n
            WHERE n.etudiant.id = :etudiantId AND n.matiere.id = :matiereId
        """;
        Query<Double> query = session.createQuery(hql, Double.class);
        query.setParameter("etudiantId", etudiantId);
        query.setParameter("matiereId", matiereId);
        return query.uniqueResult();
    }

    // Trouver la moyenne générale des notes pour une matière
    public Double findGeneralAverageByMatiere(Session session, Long matiereId) {
        String hql = """
            SELECT AVG(n.note)
            FROM Note n
            WHERE n.matiere.id = :matiereId
        """;
        Query<Double> query = session.createQuery(hql, Double.class);
        query.setParameter("matiereId", matiereId);
        return query.uniqueResult();
    }

    // Trouver les IDs distincts des matières associées à un étudiant
    public List<Long> findDistinctMatiereIdsByEtudiant(Session session, Long etudiantId) {
        String hql = """
            SELECT DISTINCT n.matiere.id
            FROM Note n
            WHERE n.etudiant.id = :etudiantId
        """;
        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("etudiantId", etudiantId);
        return query.list();
    }

    // Trouver le nombre maximum de notes pour un étudiant par matière
    public Integer findMaxNoteCountForEtudiant(Session session, Long etudiantId) {
        String sql = """
    SELECT MAX(noteCount) 
    FROM (
        SELECT COUNT(n.id) AS noteCount
        FROM resultats n
        WHERE n.etudiant_id = :etudiantId
        GROUP BY n.matiere_id
    ) AS subquery
""";
        Query<Long> query = session.createNativeQuery(sql, Long.class);
        query.setParameter("etudiantId", etudiantId);
        Long maxCount = query.getSingleResult();
        Integer maxCountAsInteger = maxCount != null ? maxCount.intValue() : null;
        return maxCountAsInteger;
    }


    // Trouver les dernières notes pour un étudiant et une matière
    public List<Note> findLastNoteForEtudiantAndMatiere(Session session, Long etudiantId, Long matiereId) {
        String hql = """
            SELECT n
            FROM Note n
            WHERE n.etudiant.id = :etudiantId AND n.matiere.id = :matiereId
            ORDER BY n.id DESC
        """;
        Query<Note> query = session.createQuery(hql, Note.class);
        query.setParameter("etudiantId", etudiantId);
        query.setParameter("matiereId", matiereId);
        return query.list();
    }

    // Supprimer toutes les notes associées à un étudiant
    public void deleteAllByEtudiantId(Session session, Long etudiantId) {
        String hql = "DELETE FROM Note n WHERE n.etudiant.id = :etudiantId";
        Query<?> query = session.createQuery(hql);
        query.setParameter("etudiantId", etudiantId);
        query.executeUpdate();
    }
}
