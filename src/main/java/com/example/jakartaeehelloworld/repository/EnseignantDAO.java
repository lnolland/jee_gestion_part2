package com.example.jakartaeehelloworld.repository;

import com.example.jakartaeehelloworld.Model.Cours;
import com.example.jakartaeehelloworld.Model.Enseignant;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class EnseignantDAO {
	
	public Optional<Enseignant> findById(Session session, Long id) {
	    return Optional.ofNullable(session.get(Enseignant.class, id));
	}
	
	public void save(Session session, Enseignant enseignant) {
	    session.persist(enseignant);
	}
	
	public List<Enseignant> findAll(Session session) {
	    String hql = "FROM Enseignant";
	    Query<Enseignant> query = session.createQuery(hql, Enseignant.class);
	    return query.list();
	}

    // Trouver un enseignant par nom, prénom et contact
    public Optional<Enseignant> findByNomPrenomContact(Session session, String nom, String prenom, String contact) {
        String hql = """
            SELECT e
            FROM Enseignant e
            WHERE e.nom = :nom AND e.prenom = :prenom AND e.contact = :contact
        """;
        Query<Enseignant> query = session.createQuery(hql, Enseignant.class);
        query.setParameter("nom", nom);
        query.setParameter("prenom", prenom);
        query.setParameter("contact", contact);
        return Optional.ofNullable(query.uniqueResult());
    }

    // Supprimer les relations entre un enseignant et ses matières

    public void supprimerRelationsAvecMatieres(Session session, Long enseignantId) {
        // Récupérer l'enseignant avec ses matières
        Enseignant enseignant = session.get(Enseignant.class, enseignantId);
        if (enseignant != null) {
            // Vider la collection des matières
            enseignant.getMatieres().clear();
            // Mettre à jour l'enseignant
            session.merge(enseignant);
        }
    }


    // Supprimer les relations dans la table de jointure Enseignant_matieres (requête native)
    public void supprimerRelationsDansEnseignantsMatieres(Session session, Long enseignantId) {
        String sql = "DELETE FROM Enseignant_matieres WHERE enseignant_id = :enseignantId";
        Query<?> query = session.createNativeQuery(sql);
        query.setParameter("enseignantId", enseignantId);
        query.executeUpdate();
    }
}
