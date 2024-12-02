package com.example.jakartaeehelloworld.Controller;

import com.example.jakartaeehelloworld.Model.*;
import com.example.jakartaeehelloworld.repository.CoursDAO;
import com.example.jakartaeehelloworld.repository.EnseignantDAO;
import com.example.jakartaeehelloworld.repository.EtudiantDAO;
import com.example.jakartaeehelloworld.repository.InscriptionDAO;
import com.example.jakartaeehelloworld.repository.MatiereDAO;
import com.example.jakartaeehelloworld.repository.NoteDAO;
import com.example.jakartaeehelloworld.repository.UtilisateurDAO;
import com.example.jakartaeehelloworld.service.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "NoteController", urlPatterns = {"/notes/ajouterNote"})
public class NoteController extends HttpServlet {

    private final NoteService noteService;
    private final UtilisateurService utilisateurService;
    private final EtudiantService etudiantService;
    private final MatiereService matiereService;

    public NoteController() {
        this.noteService = new NoteService(new NoteDAO(), new EtudiantDAO(), new MatiereDAO());
        this.utilisateurService = new UtilisateurService(new UtilisateurDAO(), new EtudiantDAO(), new EnseignantDAO());
        this.etudiantService = new EtudiantService(new EtudiantDAO(), new CoursDAO(), new InscriptionDAO(), new NoteDAO(), new UtilisateurDAO());
        this.matiereService = new MatiereService(new MatiereDAO());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if (path.equals("/notes/ajouterNote")) {
            showAddNoteForm(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page introuvable");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if (path.equals("/notes/ajouterNote")) {
            saisirNote(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page introuvable");
        }
    }

    private void showAddNoteForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findById(userId);

        if (utilisateur == null) {
            throw new IllegalStateException("Utilisateur introuvable dans la session.");
        }

        if (utilisateur.getRole() == Role.ENSEIGNANT) {
            List<Matiere> matieres = utilisateur.getEnseignant().getMatieres();
            request.setAttribute("matieres", matieres);
        } else if (utilisateur.getRole() == Role.ADMINISTRATEUR) {
            List<Matiere> matieres = matiereService.getAllMatieres();
            request.setAttribute("matieres", matieres);
        } else {
            throw new IllegalStateException("Rôle utilisateur non pris en charge.");
        }

        request.setAttribute("etudiants", etudiantService.getAllEtudiants());
        RequestDispatcher dispatcher = request.getRequestDispatcher("ajouterNote.jsp");
        dispatcher.forward(request, response);
    }

    private void saisirNote(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findById(userId);

        if (utilisateur == null) {
            throw new IllegalStateException("Utilisateur non trouvé.");
        }

        Long etudiantId = Long.parseLong(request.getParameter("etudiantId"));
        Long matiereId = Long.parseLong(request.getParameter("matiereId"));
        Float note = Float.parseFloat(request.getParameter("note"));

        if (utilisateur.getRole() == Role.ENSEIGNANT) {
            Enseignant enseignant = utilisateur.getEnseignant();
            if (enseignant == null) {
                throw new IllegalStateException("L'utilisateur est un enseignant mais aucune entité Enseignant n'est associée.");
            }

            boolean enseigneLaMatiere = enseignant.getMatieres().stream()
                    .anyMatch(m -> m.getId().equals(matiereId));

            if (!enseigneLaMatiere) {
                throw new IllegalArgumentException("Vous ne pouvez pas noter pour cette matière.");
            }
        } else if (utilisateur.getRole() != Role.ADMINISTRATEUR) {
            throw new IllegalArgumentException("Vous n'avez pas l'autorisation d'ajouter une note.");
        }

        noteService.ajouterNotePourEtudiant(etudiantId, matiereId, note);

        response.sendRedirect(request.getContextPath() + "/etudiants/detailsEtudiantEnseignant?id=" + etudiantId);
    }
}
