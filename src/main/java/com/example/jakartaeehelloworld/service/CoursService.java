package com.example.jakartaeehelloworld.service;

import com.example.jakartaeehelloworld.Model.Cours;
import com.example.jakartaeehelloworld.Model.Enseignant;
import com.example.jakartaeehelloworld.Model.Matiere;
import com.example.jakartaeehelloworld.repository.CoursDAO;
import com.example.jakartaeehelloworld.repository.EnseignantDAO;
import com.example.jakartaeehelloworld.repository.MatiereDAO;
import com.example.jakartaeehelloworld.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;

public class CoursService {

    private final CoursDAO coursDAO;
    private final MatiereDAO matiereDAO;
    private final EnseignantDAO enseignantDAO;

    public CoursService(CoursDAO coursDAO, MatiereDAO matiereDAO, EnseignantDAO enseignantDAO) {
        this.coursDAO = coursDAO;
        this.matiereDAO = matiereDAO;
        this.enseignantDAO = enseignantDAO;
    }

    public List<Cours> getCoursSansProfesseurByEnseignantMatiere(Long enseignantId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return coursDAO.findCoursSansProfesseurByEnseignantMatiere(session, enseignantId);
        }
    }

    public void affecterProfesseurAuCours(Long coursId, Long enseignantId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Récupérer le cours
            Cours cours = coursDAO.findById(session, coursId)
                    .orElseThrow(() -> new RuntimeException("Cours non trouvé"));

            // Récupérer l'enseignant
            Enseignant enseignant = enseignantDAO.findById(session, enseignantId)
                    .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));

            // Affecter l'enseignant au cours
            cours.setEnseignant(enseignant);
            coursDAO.save(session, cours);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
    
    public void ajouterCoursSansProfesseur(LocalDateTime dateCours, Long matiereId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Conversion de LocalDateTime en java.util.Date
            Date date = java.sql.Timestamp.valueOf(dateCours);

            // Récupération de la matière
            Matiere matiere = matiereDAO.findById(session, matiereId)
                    .orElseThrow(() -> new RuntimeException("Matière non trouvée"));

            // Création d'un nouveau cours
            Cours cours = new Cours();
            cours.setDateCours(date);
            cours.setMatiere(matiere);

            // Enregistrement dans la base de données
            coursDAO.save(session, cours);

            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'ajout du cours sans professeur", e);
        }
    }

    public void ajouterCours(LocalDateTime dateCours, Long matiereId, Long enseignantId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Cours cours = new Cours();
            cours.setDateCours(java.sql.Timestamp.valueOf(dateCours));

            // Récupérer les relations
            Matiere matiere = matiereDAO.findById(session, matiereId)
                    .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
            Enseignant enseignant = enseignantDAO.findById(session, enseignantId)
                    .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));

            cours.setMatiere(matiere);
            cours.setEnseignant(enseignant);

            // Sauvegarder
            coursDAO.save(session, cours);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public List<Cours> getTousLesCours() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return coursDAO.findAll(session);
        }
    }

    public List<Cours> getCoursSansProfesseur() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return coursDAO.findByEnseignantIsNull(session);
        }
    }

    public List<Cours> getCoursParProfesseur(Long enseignantId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return coursDAO.findByEnseignantId(session, enseignantId);
        }
    }

    public void supprimerAffectationProfesseurAuCours(Long coursId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Récupérer le cours
            Cours cours = coursDAO.findById(session, coursId)
                    .orElseThrow(() -> new RuntimeException("Cours non trouvé"));

            // Supprimer l'affectation
            cours.setEnseignant(null);
            coursDAO.save(session, cours);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public void supprimerCoursComplet(Long coursId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            if (!coursDAO.existsById(session, coursId)) {
                throw new RuntimeException("Cours non trouvé");
            }

            coursDAO.deleteById(session, coursId);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Cours getCoursById(Long coursId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return coursDAO.findById(session, coursId)
                    .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        }
    }

    public void modifierDateCours(Long coursId, LocalDateTime nouvelleDate) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Récupérer le cours
            Cours cours = getCoursById(coursId);

            // Modifier la date
            cours.setDateCours(java.sql.Timestamp.valueOf(nouvelleDate));
            coursDAO.save(session, cours);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
}
