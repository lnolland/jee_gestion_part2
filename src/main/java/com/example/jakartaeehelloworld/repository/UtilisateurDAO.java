package com.example.jakartaeehelloworld.repository;

import com.example.jakartaeehelloworld.Model.Role;
import com.example.jakartaeehelloworld.Model.Utilisateur;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Optional;

public class UtilisateurDAO {

    // Trouver un utilisateur par username et mot de passe
    public Utilisateur findByUsernameAndPassword(Session session, String username, String password) {
        String hql = """
            SELECT u
            FROM Utilisateur u 
            LEFT JOIN FETCH u.enseignant e
            LEFT JOIN FETCH e.matieres
            WHERE u.username = :username AND u.password = :password
        """;
        Query<Utilisateur> query = session.createQuery(hql, Utilisateur.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        return query.uniqueResult();
    }

    // Supprimer tous les Utilisateur liés à un étudiant par ID
    public void deleteAllByEtudiantId(Session session, Long etudiantId) {
        String hql = "DELETE FROM Utilisateur u WHERE u.etudiant.id = :etudiantId";
        Query<?> query = session.createQuery(hql);
        query.setParameter("etudiantId", etudiantId);
        query.executeUpdate();
    }

    // Trouver un utilisateur par username
    public Utilisateur findByUsername(Session session, String username) {
        String hql = """
            SELECT u
            FROM Utilisateur u
            WHERE u.username = :username
        """;
        Query<Utilisateur> query = session.createQuery(hql, Utilisateur.class);
        query.setParameter("username", username);
        return query.uniqueResult();
    }

    // Trouver un utilisateur lié par un ID d'enseignant ou d'étudiant
    public Optional<Utilisateur> findByLinkedId(Session session, Long id) {
        String hql = """
            SELECT u
            FROM Utilisateur u
            WHERE (u.enseignant.id = :id OR u.etudiant.id = :id)
        """;
        Query<Utilisateur> query = session.createQuery(hql, Utilisateur.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.uniqueResult());
    }

    // Trouver un utilisateur par ID d'enseignant
    public Optional<Utilisateur> findByEnseignantId(Session session, Long enseignantId) {
        String hql = """
            SELECT u
            FROM Utilisateur u
            WHERE u.enseignant.id = :enseignantId
        """;
        Query<Utilisateur> query = session.createQuery(hql, Utilisateur.class);
        query.setParameter("enseignantId", enseignantId);
        return Optional.ofNullable(query.uniqueResult());
    }

    public Optional<Utilisateur> findById(Session session, Long id) {
        return Optional.ofNullable(session.get(Utilisateur.class, id));
    }

    public void save(Session session, Utilisateur utilisateur) {
        session.persist(utilisateur);
    }
    
    // Trouver un utilisateur par rôle et détails (nom, prénom, contact)
    public Optional<Utilisateur> findByRoleAndDetails(Session session, Role role, String nom, String prenom, String contact) {
        String hql = """
            SELECT u
            FROM Utilisateur u
            WHERE u.role = :role
            AND (
                (u.enseignant.nom = :nom AND u.enseignant.prenom = :prenom AND u.enseignant.contact = :contact)
                OR 
                (u.etudiant.nom = :nom AND u.etudiant.prenom = :prenom AND u.etudiant.contact = :contact)
            )
        """;
        Query<Utilisateur> query = session.createQuery(hql, Utilisateur.class);
        query.setParameter("role", role);
        query.setParameter("nom", nom);
        query.setParameter("prenom", prenom);
        query.setParameter("contact", contact);
        return Optional.ofNullable(query.uniqueResult());
    }

    // Supprimer un utilisateur par ID d'enseignant
    public void deleteByEnseignantId(Session session, Long enseignantId) {
        String hql = "DELETE FROM Utilisateur u WHERE u.enseignant.id = :enseignantId";
        Query<?> query = session.createQuery(hql);
        query.setParameter("enseignantId", enseignantId);
        query.executeUpdate();
    }
}
