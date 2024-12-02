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
import java.util.Map;

@WebServlet(name = "EtudiantController", urlPatterns = {"/etudiants/*"})
public class EtudiantController extends HttpServlet {

    private final EtudiantService etudiantService;
    private final NoteService noteService;
    private final MatiereService matiereService;
    private final UtilisateurService utilisateurService;
    private final CoursService coursService;
    private final EnseignantService enseignantService;

    public EtudiantController() {
        this.etudiantService = new EtudiantService(new EtudiantDAO(), new CoursDAO(), new InscriptionDAO(), new NoteDAO(), new UtilisateurDAO());
        this.noteService = new NoteService(new NoteDAO(), new EtudiantDAO(), new MatiereDAO());
        this.matiereService = new MatiereService(new MatiereDAO());
        this.utilisateurService = new UtilisateurService(new UtilisateurDAO(), new EtudiantDAO(), new EnseignantDAO());
        this.coursService = new CoursService(new CoursDAO(), new MatiereDAO(), new EnseignantDAO());
        this.enseignantService = new EnseignantService(new EnseignantDAO(), new MatiereDAO(), new CoursDAO());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        if (path == null || path.equals("/")) {
            afficherListeEtudiants(request, response);
        } else if (path.equals("/menu")) {
            afficherMenuEtudiant(request, response);
        } else if (path.equals("/cours")) {
            afficherCours(request, response);
        } else if (path.equals("/modifierContact")) {
            afficherFormulaireModifierContact(request, response);
        } else if (path.equals("/details")) {
            afficherDetailsEtudiant(request, response);
        } else if (path.equals("/detailsEtudiantEnseignant")) {
            afficherDetailsEtudiantPourEnseignant(request, response);
        } else if (path.equals("/ajouterEtudiant")) {
            afficherFormulaireAjouterEtudiant(request, response);
        } else if (path.equals("/supprimerEtudiant")) {
            supprimerEtudiant(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page introuvable.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        if (path.equals("/sauvegarderContact")) {
            sauvegarderContact(request, response);
        } else if (path.equals("/inscrire")) {
            inscrireCours(request, response);
        } else if (path.equals("/desinscrire")) {
            desinscrireCours(request, response);
        } else if (path.equals("/ajouterEtudiant")) {
            ajouterEtudiant(request, response);
        } else if (path.equals("/modifierEtudiant")) {
            modifierEtudiant(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page introuvable.");
        }
    }

    private void afficherMenuEtudiant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findById(userId);
        if (utilisateur.getRole() != Role.ETUDIANT) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/MenuEtudiant.jsp");
        dispatcher.forward(request, response);
    }

    private void afficherListeEtudiants(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findById(userId);
        if (utilisateur.getRole() != Role.ENSEIGNANT && utilisateur.getRole() != Role.ADMINISTRATEUR) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String keyword = request.getParameter("keyword");
        Long matiereId = request.getParameter("matiereId") != null ? Long.parseLong(request.getParameter("matiereId")) : null;

        List<Etudiant> etudiants;

        if (matiereId != null && keyword != null && !keyword.isEmpty()) {
            List<Etudiant> etudiantsParMatiere = etudiantService.findEtudiantsByCoursOrNotesMatiere(matiereId);
            etudiants = etudiantsParMatiere.stream()
                    .filter(etudiant -> etudiant.getNom().contains(keyword) || etudiant.getPrenom().contains(keyword))
                    .toList();
        } else if (matiereId != null) {
            etudiants = etudiantService.findEtudiantsByCoursOrNotesMatiere(matiereId);
        } else if (keyword != null && !keyword.isEmpty()) {
            etudiants = etudiantService.searchEtudiants(keyword);
        } else {
            etudiants = etudiantService.getAllEtudiants();
        }

        List<Matiere> matieres = matiereService.getAllMatieres();
        request.setAttribute("etudiants", etudiants);
        request.setAttribute("keyword", keyword);
        request.setAttribute("matiereId", matiereId);
        request.setAttribute("matieres", matieres);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/EtudiantsListe.jsp");
        dispatcher.forward(request, response);
    }

    private void afficherCours(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findByIdWithDetails(userId);
        if (utilisateur.getRole() != Role.ETUDIANT) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        List<Cours> tousLesCours = coursService.getTousLesCours();
        List<Cours> coursInscrits = etudiantService.getCoursInscrits(utilisateur.getEtudiant().getId());
        List<Cours> coursNonInscrits = tousLesCours.stream()
                .filter(cours -> !coursInscrits.contains(cours))
                .toList();

        request.setAttribute("coursNonInscrits", coursNonInscrits);
        request.setAttribute("coursInscrits", coursInscrits);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/CoursDisponibles.jsp");
        dispatcher.forward(request, response);
    }
    
    private void afficherFormulaireModifierContact(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findByIdWithDetails(userId);

        if (utilisateur.getRole() != Role.ETUDIANT) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Etudiant etudiant = utilisateur.getEtudiant();
        request.setAttribute("etudiant", etudiant);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/modifierContactEtudiant.jsp");
        dispatcher.forward(request, response);
    }

    private void afficherDetailsEtudiant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findByIdWithDetails(userId);

        if (utilisateur.getRole() != Role.ETUDIANT) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Etudiant etudiant = utilisateur.getEtudiant();
        request.setAttribute("etudiant", etudiant);

        Map<String, Object> notesData = noteService.getNotesAndAverages(etudiant.getId());
        request.setAttribute("notesData", notesData);

        Integer maxNoteCount = noteService.getMaxNoteCountForEtudiant(etudiant.getId());
        request.setAttribute("maxNoteCount", maxNoteCount);

        List<Inscription> inscriptions = etudiant.getInscriptions();
        request.setAttribute("inscriptions", inscriptions);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/detailsEtudiant.jsp");
        dispatcher.forward(request, response);
    }

    private void afficherDetailsEtudiantPourEnseignant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findById(userId);
        if (utilisateur.getRole() != Role.ENSEIGNANT && utilisateur.getRole() != Role.ADMINISTRATEUR) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Long id = Long.parseLong(request.getParameter("id"));
        Etudiant etudiant = etudiantService.getEtudiantById(id);
        request.setAttribute("etudiant", etudiant);

        Map<String, Object> notesData = noteService.getNotesAndAverages(id);
        request.setAttribute("notesData", notesData);

        Integer maxNoteCount = noteService.getMaxNoteCountForEtudiant(id);
        request.setAttribute("maxNoteCount", maxNoteCount);

        List<Inscription> inscriptions = etudiant.getInscriptions();
        request.setAttribute("inscriptions", inscriptions);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/detailsEtudiantEnseignant.jsp");
        dispatcher.forward(request, response);
    }

    private void afficherFormulaireAjouterEtudiant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findById(userId);
        if (utilisateur.getRole() != Role.ENSEIGNANT && utilisateur.getRole() != Role.ADMINISTRATEUR) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        request.setAttribute("etudiant", new Etudiant());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/ajouterEtudiant.jsp");
        dispatcher.forward(request, response);
    }

    private void supprimerEtudiant(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findById(userId);
        if (utilisateur.getRole() != Role.ENSEIGNANT && utilisateur.getRole() != Role.ADMINISTRATEUR) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Long id = Long.parseLong(request.getParameter("id"));
        etudiantService.deleteEtudiant(id);

        response.sendRedirect(request.getContextPath() + "/etudiants");
    }

    private void sauvegarderContact(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findByIdWithDetails(userId);
        if (utilisateur.getRole() != Role.ETUDIANT) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String contact = request.getParameter("contact");
        Etudiant etudiant = utilisateur.getEtudiant();
        etudiant.setContact(contact);
        etudiantService.updateEtudiant(etudiant);

        response.sendRedirect(request.getContextPath() + "/etudiants/details");
    }
    
    private void inscrireCours(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findByIdWithDetails(userId);
        if (utilisateur.getRole() != Role.ETUDIANT) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Long coursId = Long.parseLong(request.getParameter("coursId"));
        etudiantService.inscrireCours(utilisateur.getEtudiant().getId(), coursId);

        response.sendRedirect(request.getContextPath() + "/etudiants/cours");
    }

    private void desinscrireCours(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findByIdWithDetails(userId);
        if (utilisateur.getRole() != Role.ETUDIANT) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Long coursId = Long.parseLong(request.getParameter("coursId"));
        etudiantService.desinscrireCours(utilisateur.getEtudiant().getId(), coursId);

        response.sendRedirect(request.getContextPath() + "/etudiants/cours");
    }

    private void ajouterEtudiant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findById(userId);
        if (utilisateur.getRole() != Role.ENSEIGNANT && utilisateur.getRole() != Role.ADMINISTRATEUR) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String dateNaissance = request.getParameter("dateNaissance");
        String contact = request.getParameter("contact");

        Etudiant etudiant = new Etudiant();
        etudiant.setNom(nom);
        etudiant.setPrenom(prenom);
        etudiant.setDateNaissance(java.sql.Date.valueOf(dateNaissance));
        etudiant.setContact(contact);

        etudiantService.saveEtudiant(etudiant);

        response.sendRedirect(request.getContextPath() + "/etudiants");
    }

    private void modifierEtudiant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findById(userId);
        if (utilisateur.getRole() != Role.ENSEIGNANT && utilisateur.getRole() != Role.ADMINISTRATEUR) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Long etudiantId = Long.parseLong(request.getParameter("id"));
        Etudiant etudiant = etudiantService.getEtudiantById(etudiantId);
        if (etudiant == null) {
            response.sendRedirect(request.getContextPath() + "/etudiants");
            return;
        }

        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String dateNaissance = request.getParameter("dateNaissance");
        String contact = request.getParameter("contact");

        etudiant.setNom(nom);
        etudiant.setPrenom(prenom);
        etudiant.setDateNaissance(java.sql.Date.valueOf(dateNaissance));
        etudiant.setContact(contact);

        etudiantService.updateEtudiant(etudiant);

        response.sendRedirect(request.getContextPath() + "/etudiants");
    }


}