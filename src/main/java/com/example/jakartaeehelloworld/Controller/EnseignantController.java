package com.example.jakartaeehelloworld.Controller;

import com.example.jakartaeehelloworld.Model.*;
import com.example.jakartaeehelloworld.repository.CoursDAO;
import com.example.jakartaeehelloworld.repository.EnseignantDAO;
import com.example.jakartaeehelloworld.repository.EtudiantDAO;
import com.example.jakartaeehelloworld.repository.InscriptionDAO;
import com.example.jakartaeehelloworld.repository.MatiereDAO;
import com.example.jakartaeehelloworld.repository.NoteDAO;
import com.example.jakartaeehelloworld.repository.UtilisateurDAO;
import com.example.jakartaeehelloworld.service.CoursService;
import com.example.jakartaeehelloworld.service.EnseignantService;
import com.example.jakartaeehelloworld.service.EtudiantService;
import com.example.jakartaeehelloworld.service.MatiereService;
import com.example.jakartaeehelloworld.service.NoteService;
import com.example.jakartaeehelloworld.service.UtilisateurService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "EnseignantController", urlPatterns = {"/enseignant/*"})
public class EnseignantController extends HttpServlet {

    private final CoursService coursService;
    private final MatiereService matiereService;
    private final UtilisateurService utilisateurService;
    private final EnseignantService enseignantService;

    public EnseignantController() {
         this.matiereService = new MatiereService(new MatiereDAO());
         this.utilisateurService = new UtilisateurService(new UtilisateurDAO(), new EtudiantDAO(), new EnseignantDAO());
         this.coursService = new CoursService(new CoursDAO(), new MatiereDAO(), new EnseignantDAO());
         this.enseignantService = new EnseignantService(new EnseignantDAO(), new MatiereDAO(), new CoursDAO());
     }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        switch (path) {
            case "/menu":
                afficherMenu(request, response, userId);
                break;
            case "/details":
                afficherDetailsEnseignant(request, response, userId);
                break;
            case "/cours":
                afficherCours(request, response, userId);
                break;
            case "/ajouterCours":
                afficherFormulaireAjouterCours(request, response, userId);
                break;
            case "/modifierDateCours":
                afficherFormulaireModifierDate(request, response, userId);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page introuvable.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        switch (path) {
            case "/affecterCours":
                affecterCours(request, response, userId);
                break;
            case "/ajouterCours":
                ajouterCours(request, response, userId);
                break;
            case "/supprimerCours":
                supprimerCours(request, response, userId);
                break;
            case "/sauvegarderDateCours":
                sauvegarderDateCours(request, response, userId);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action introuvable.");
        }
    }

    private void afficherMenu(HttpServletRequest request, HttpServletResponse response, Long userId) throws ServletException, IOException {
        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ENSEIGNANT) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/MenuEnseignant.jsp");
        dispatcher.forward(request, response);
    }

    private void afficherDetailsEnseignant(HttpServletRequest request, HttpServletResponse response, Long userId) throws ServletException, IOException {
        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ENSEIGNANT) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        request.setAttribute("enseignant", utilisateur.getEnseignant());
        List<Cours> coursAssocies = coursService.getCoursParProfesseur(utilisateur.getEnseignant().getId());
        request.setAttribute("coursAssocies", coursAssocies);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/detailsEnseignant.jsp");
        dispatcher.forward(request, response);
    }

    private void afficherCours(HttpServletRequest request, HttpServletResponse response, Long userId) throws ServletException, IOException {
        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ENSEIGNANT) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        List<Cours> coursAssocies = coursService.getCoursParProfesseur(utilisateur.getEnseignant().getId());
        List<Cours> coursSansProf = coursService.getCoursSansProfesseurByEnseignantMatiere(utilisateur.getEnseignant().getId());
        request.setAttribute("coursAssocies", coursAssocies);
        request.setAttribute("coursSansProf", coursSansProf);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/CoursListe.jsp");
        dispatcher.forward(request, response);
    }

    private void afficherFormulaireAjouterCours(HttpServletRequest request, HttpServletResponse response, Long userId) throws ServletException, IOException {
        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ENSEIGNANT) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        List<Matiere> matieres = matiereService.getMatieresByEnseignant(utilisateur.getEnseignant().getId());
        request.setAttribute("matieres", matieres);
        request.setAttribute("enseignantId", utilisateur.getEnseignant().getId());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/ajouterCours.jsp");
        dispatcher.forward(request, response);
    }

    private void afficherFormulaireModifierDate(HttpServletRequest request, HttpServletResponse response, Long userId) throws ServletException, IOException {
        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ENSEIGNANT) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        Long coursId = Long.parseLong(request.getParameter("coursId"));
        Cours cours = coursService.getCoursById(coursId);
        request.setAttribute("cours", cours);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/modifierDateCours.jsp");
        dispatcher.forward(request, response);
    }

    private void affecterCours(HttpServletRequest request, HttpServletResponse response, Long userId) throws IOException {
        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ENSEIGNANT) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        Long coursId = Long.parseLong(request.getParameter("coursId"));
        coursService.affecterProfesseurAuCours(coursId, utilisateur.getEnseignant().getId());
        response.sendRedirect(request.getContextPath() + "/enseignant/cours");
    }

    private void ajouterCours(HttpServletRequest request, HttpServletResponse response, Long userId) throws IOException {
        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ENSEIGNANT) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        LocalDateTime dateCours = LocalDateTime.parse(request.getParameter("dateCours"));
        Long matiereId = Long.parseLong(request.getParameter("matiereId"));
        coursService.ajouterCours(dateCours, matiereId, utilisateur.getEnseignant().getId());
        response.sendRedirect(request.getContextPath() + "/enseignant/cours");
    }

    private void supprimerCours(HttpServletRequest request, HttpServletResponse response, Long userId) throws IOException {
        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ENSEIGNANT) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        Long coursId = Long.parseLong(request.getParameter("coursId"));
        coursService.supprimerAffectationProfesseurAuCours(coursId);
        response.sendRedirect(request.getContextPath() + "/enseignant/cours");
    }

    private void sauvegarderDateCours(HttpServletRequest request, HttpServletResponse response, Long userId) throws IOException {
        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ENSEIGNANT) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        Long coursId = Long.parseLong(request.getParameter("coursId"));
        LocalDateTime nouvelleDate = LocalDateTime.parse(request.getParameter("nouvelleDate"));
        coursService.modifierDateCours(coursId, nouvelleDate);
        response.sendRedirect(request.getContextPath() + "/enseignant/cours");
    }
}
