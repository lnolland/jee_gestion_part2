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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EnseignantService {

    private final EnseignantDAO enseignantDAO;
    private final MatiereDAO matiereDAO;
    private final CoursDAO coursDAO;

    public EnseignantService(EnseignantDAO enseignantDAO, MatiereDAO matiereDAO, CoursDAO coursDAO) {
        this.enseignantDAO = enseignantDAO;
        this.matiereDAO = matiereDAO;
        this.coursDAO = coursDAO;
    }

    public List<Enseignant> getAllEnseignants() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return enseignantDAO.findAll(session);
        }
    }

    public void ajouterEnseignantAvecMatieres(Enseignant enseignant, Long[] matiereIds) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            if (matiereIds != null) {
                List<Matiere> matieres = matiereDAO.findAllByIds(session, Arrays.asList(matiereIds));
                enseignant.setMatieres(matieres);
            }

            enseignantDAO.save(session, enseignant);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Enseignant getEnseignantById(Long enseignantId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return enseignantDAO.findById(session, enseignantId)
                    .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));
        }
    }

    public void modifierContact(Long enseignantId, String nouveauContact) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Enseignant enseignant = enseignantDAO.findById(session, enseignantId)
                    .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));
            enseignant.setContact(nouveauContact);

            enseignantDAO.save(session, enseignant);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public List<Enseignant> findAll() {
        return getAllEnseignants();
    }
}
