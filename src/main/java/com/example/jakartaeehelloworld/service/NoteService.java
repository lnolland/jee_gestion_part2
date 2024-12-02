package com.example.jakartaeehelloworld.service;

import com.example.jakartaeehelloworld.Model.Note;
import com.example.jakartaeehelloworld.Model.Etudiant;
import com.example.jakartaeehelloworld.Model.Matiere;
import com.example.jakartaeehelloworld.repository.NoteDAO;
import com.example.jakartaeehelloworld.repository.EtudiantDAO;
import com.example.jakartaeehelloworld.repository.MatiereDAO;
import com.example.jakartaeehelloworld.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteService {

    private final NoteDAO noteDAO;
    private final EtudiantDAO etudiantDAO;
    private final MatiereDAO matiereDAO;

    public NoteService(NoteDAO noteDAO, EtudiantDAO etudiantDAO, MatiereDAO matiereDAO) {
        this.noteDAO = noteDAO;
        this.etudiantDAO = etudiantDAO;
        this.matiereDAO = matiereDAO;
    }

    public void saveNoteForEtudiant(Note note, Long etudiantId, Long matiereId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Etudiant etudiant = etudiantDAO.findById(session, etudiantId)
                    .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé"));
            Matiere matiere = matiereDAO.findById(session, matiereId)
                    .orElseThrow(() -> new IllegalArgumentException("Matière non trouvée"));

            note.setEtudiant(etudiant);
            note.setMatiere(matiere);

            noteDAO.save(session, note);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Integer getMaxNoteCountForEtudiant(Long etudiantId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return noteDAO.findMaxNoteCountForEtudiant(session, etudiantId);
        }
    }

    public void deleteLastNoteForEtudiantAndMatiere(Long etudiantId, Long matiereId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            List<Note> notes = noteDAO.findLastNoteForEtudiantAndMatiere(session, etudiantId, matiereId);
            if (!notes.isEmpty()) {
                noteDAO.delete(session, notes.get(0));
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public Map<String, Object> getNotesAndAverages(Long etudiantId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Map<String, Object> data = new HashMap<>();

            List<Long> matiereIds = noteDAO.findDistinctMatiereIdsByEtudiant(session, etudiantId);

            for (Long matiereId : matiereIds) {
                Matiere matiere = matiereDAO.findById(session, matiereId)
                        .orElseThrow(() -> new IllegalArgumentException("Matière non trouvée"));

                List<Note> notes = noteDAO.findByEtudiantAndMatiere(session, etudiantId, matiereId);
                Double moyenneEtudiant = noteDAO.findAverageByEtudiantAndMatiere(session, etudiantId, matiereId);
                Double moyenneGenerale = noteDAO.findGeneralAverageByMatiere(session, matiereId);

                Map<String, Object> matiereData = new HashMap<>();
                matiereData.put("matiereNom", matiere.getNom());
                matiereData.put("notes", notes);
                matiereData.put("moyenneEtudiant", moyenneEtudiant);
                matiereData.put("moyenneGenerale", moyenneGenerale);

                data.put(matiereId.toString(), matiereData);
            }

            return data;
        }
    }

    public void ajouterNotePourEtudiant(Long etudiantId, Long matiereId, Float noteValeur) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Etudiant etudiant = etudiantDAO.findById(session, etudiantId)
                    .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé avec l'ID : " + etudiantId));
            Matiere matiere = matiereDAO.findById(session, matiereId)
                    .orElseThrow(() -> new IllegalArgumentException("Matière non trouvée avec l'ID : " + matiereId));

            Note note = new Note();
            note.setEtudiant(etudiant);
            note.setMatiere(matiere);
            note.setNote(noteValeur);

            noteDAO.save(session, note);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
}
