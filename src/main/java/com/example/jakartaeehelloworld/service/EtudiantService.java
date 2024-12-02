package com.example.jakartaeehelloworld.service;

import com.example.jakartaeehelloworld.Model.Cours;
import com.example.jakartaeehelloworld.Model.Etudiant;
import com.example.jakartaeehelloworld.Model.Inscription;
import com.example.jakartaeehelloworld.repository.CoursDAO;
import com.example.jakartaeehelloworld.repository.EtudiantDAO;
import com.example.jakartaeehelloworld.repository.InscriptionDAO;
import com.example.jakartaeehelloworld.repository.NoteDAO;
import com.example.jakartaeehelloworld.repository.UtilisateurDAO;
import com.example.jakartaeehelloworld.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class EtudiantService {

    private final EtudiantDAO etudiantDAO;
    private final CoursDAO coursDAO;
    private final InscriptionDAO inscriptionDAO;
    private final NoteDAO noteDAO;
    private final UtilisateurDAO utilisateurDAO;

    public EtudiantService(EtudiantDAO etudiantDAO, CoursDAO coursDAO, InscriptionDAO inscriptionDAO, NoteDAO noteDAO, UtilisateurDAO utilisateurDAO) {
        this.etudiantDAO = etudiantDAO;
        this.coursDAO = coursDAO;
        this.inscriptionDAO = inscriptionDAO;
        this.noteDAO = noteDAO;
        this.utilisateurDAO = utilisateurDAO;
    }

    public void inscrireCours(Long etudiantId, Long coursId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Etudiant etudiant = etudiantDAO.findById(session, etudiantId)
                    .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
            Cours cours = coursDAO.findById(session, coursId)
                    .orElseThrow(() -> new RuntimeException("Cours non trouvé"));

            Inscription inscription = new Inscription(etudiant, cours);
            inscriptionDAO.save(session, inscription);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public List<Cours> getCoursInscrits(Long etudiantId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Etudiant etudiant = etudiantDAO.findById(session, etudiantId)
                    .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

            return etudiant.getInscriptions().stream()
                    .map(Inscription::getCours)
                    .toList();
        }
    }

    public void desinscrireCours(Long etudiantId, Long coursId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Etudiant etudiant = etudiantDAO.findById(session, etudiantId)
                    .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé"));

            Inscription inscription = etudiant.getInscriptions().stream()
                    .filter(i -> i.getCours().getId().equals(coursId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("L'étudiant n'est pas inscrit à ce cours"));

            etudiant.getInscriptions().remove(inscription);
            etudiantDAO.save(session, etudiant);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public List<Etudiant> getAllEtudiants() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return etudiantDAO.findAll(session);
        }
    }

    public List<Etudiant> findEtudiantsByCoursOrNotesMatiere(Long matiereId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Etudiant> etudiantsParCours = etudiantDAO.findEtudiantsByCoursMatiereId(session, matiereId);
            List<Etudiant> etudiantsParNotes = etudiantDAO.findByMatiere(session, matiereId);

            return Stream.concat(etudiantsParCours.stream(), etudiantsParNotes.stream())
                    .distinct()
                    .toList();
        }
    }

    public List<Etudiant> searchEtudiants(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return etudiantDAO.searchByNomOrPrenom(session, keyword);
        }
    }

    public void saveEtudiant(Etudiant etudiant) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            etudiantDAO.save(session, etudiant);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public void deleteEtudiant(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Etudiant etudiant = etudiantDAO.findById(session, id)
                    .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

            utilisateurDAO.deleteAllByEtudiantId(session, id);
            inscriptionDAO.deleteAllByEtudiantId(session, id);
            noteDAO.deleteAllByEtudiantId(session, id);
            etudiantDAO.delete(session, etudiant);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Etudiant getEtudiantById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return etudiantDAO.findById(session, id).orElse(null);
        }
    }

    public void updateEtudiant(Etudiant etudiant) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Etudiant existingEtudiant = etudiantDAO.findById(session, etudiant.getId())
                    .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

            existingEtudiant.setNom(etudiant.getNom());
            existingEtudiant.setPrenom(etudiant.getPrenom());
            existingEtudiant.setDateNaissance(etudiant.getDateNaissance());
            existingEtudiant.setContact(etudiant.getContact());

            etudiantDAO.save(session, existingEtudiant);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
}
