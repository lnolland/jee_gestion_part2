package com.example.jakartaeehelloworld.service;

import com.example.jakartaeehelloworld.Model.Matiere;
import com.example.jakartaeehelloworld.Model.Role;
import com.example.jakartaeehelloworld.Model.Utilisateur;
import com.example.jakartaeehelloworld.repository.MatiereDAO;
import com.example.jakartaeehelloworld.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class MatiereService {

    private final MatiereDAO matiereDAO;

    public MatiereService(MatiereDAO matiereDAO) {
        this.matiereDAO = matiereDAO;
    }

    public List<Matiere> getMatieresByEnseignant(Long enseignantId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return matiereDAO.findByEnseignantId(session, enseignantId);
        }
    }

    public List<Matiere> getAllMatieres() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return matiereDAO.findAll(session);
        }
    }

    public List<Matiere> getMatieresByEnseignantId(Long enseignantId) {
        return getMatieresByEnseignant(enseignantId);
    }

    public List<Matiere> getMatieresByUtilisateur(Utilisateur utilisateur) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (utilisateur.getRole() == Role.ENSEIGNANT && utilisateur.getEnseignant() != null) {
                return utilisateur.getEnseignant().getMatieres();
            } else if (utilisateur.getRole() == Role.ADMINISTRATEUR) {
                return matiereDAO.findAll(session);
            } else {
                throw new IllegalStateException("Rôle utilisateur non pris en charge.");
            }
        }
    }
}
