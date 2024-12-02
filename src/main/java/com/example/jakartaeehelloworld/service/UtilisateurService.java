package com.example.jakartaeehelloworld.service;

import com.example.jakartaeehelloworld.Model.Enseignant;
import com.example.jakartaeehelloworld.Model.Etudiant;
import com.example.jakartaeehelloworld.Model.Role;
import com.example.jakartaeehelloworld.Model.Utilisateur;
import com.example.jakartaeehelloworld.repository.EnseignantDAO;
import com.example.jakartaeehelloworld.repository.EtudiantDAO;
import com.example.jakartaeehelloworld.repository.UtilisateurDAO;
import com.example.jakartaeehelloworld.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Optional;

public class UtilisateurService {

    private final UtilisateurDAO utilisateurDAO;
    private final EtudiantDAO etudiantDAO;
    private final EnseignantDAO enseignantDAO;

    public UtilisateurService(UtilisateurDAO utilisateurDAO, EtudiantDAO etudiantDAO, EnseignantDAO enseignantDAO) {
        this.utilisateurDAO = utilisateurDAO;
        this.etudiantDAO = etudiantDAO;
        this.enseignantDAO = enseignantDAO;
    }

    public Utilisateur findByIdWithDetails(Long userId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Utilisateur utilisateur = utilisateurDAO.findById(session, userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            // Charger les dépendances en fonction du rôle
            if (utilisateur.getRole() == Role.ENSEIGNANT) {
                utilisateur.getEnseignant().getMatieres().size(); // Force le chargement
            } else if (utilisateur.getRole() == Role.ETUDIANT) {
                utilisateur.getEtudiant().getInscriptions().size(); // Force le chargement
            }

            transaction.commit();
            return utilisateur;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Utilisateur findById(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return utilisateurDAO.findById(session, userId)
                    .orElseThrow(() -> new IllegalStateException("Utilisateur non trouvé"));
        }
    }

    public Utilisateur findByIdWithSpecialites(Long userId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Utilisateur utilisateur = utilisateurDAO.findById(session, userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            if (utilisateur.getRole() == Role.ENSEIGNANT) {
                utilisateur.getEnseignant().getMatieres().size(); // Force le chargement
            }
            transaction.commit();
            return utilisateur;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Utilisateur creerCompte(String username, String password, Role role, String nom, String prenom, String contact) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Vérifier si un compte existe déjà pour cet utilisateur et ce rôle
            Optional<Utilisateur> existingUser = utilisateurDAO.findByRoleAndDetails(session, role, nom, prenom, contact);
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("Un compte existe déjà pour cet utilisateur avec le rôle spécifié.");
            }

            // Créer un utilisateur en fonction du rôle
            Utilisateur utilisateur;
            if (role == Role.ENSEIGNANT) {
                Enseignant enseignant = enseignantDAO.findByNomPrenomContact(session, nom, prenom, contact)
                        .orElseThrow(() -> new IllegalArgumentException("L'enseignant spécifié n'existe pas."));
                utilisateur = new Utilisateur(username, password, role, enseignant, null);
            } else if (role == Role.ETUDIANT) {
                Etudiant etudiant = etudiantDAO.findByNomPrenomContact(session, nom, prenom, contact)
                        .orElseThrow(() -> new IllegalArgumentException("L'étudiant spécifié n'existe pas."));
                utilisateur = new Utilisateur(username, password, role, null, etudiant);
            } else {
                throw new IllegalArgumentException("Rôle non pris en charge.");
            }

            utilisateurDAO.save(session, utilisateur);
            transaction.commit();
            return utilisateur;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
}
