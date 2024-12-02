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
import java.util.stream.Stream;

@WebServlet(name = "AdminController", urlPatterns = {"/admin/*"})
public class AdminController extends HttpServlet {

    private final EnseignantService enseignantService;
    private final MatiereService matiereService;
    private final UtilisateurService utilisateurService;
    private final CoursService coursService;

    public AdminController() {
        this.matiereService = new MatiereService(new MatiereDAO());
        this.utilisateurService = new UtilisateurService(new UtilisateurDAO(), new EtudiantDAO(), new EnseignantDAO());
        this.coursService = new CoursService(new CoursDAO(), new MatiereDAO(), new EnseignantDAO());
        this.enseignantService = new EnseignantService(new EnseignantDAO(), new MatiereDAO(), new CoursDAO());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        if (path == null || path.equals("/menu")) {
            afficherMenuAdmin(request, response);
        } else if (path.equals("/enseignants")) {
            listerEnseignants(request, response);
        } else if (path.equals("/detailsEnseignant")) {
            afficherDetailsEnseignant(request, response);
        } else if (path.equals("/ajouterEnseignant")) {
            afficherFormulaireAjoutEnseignant(request, response);
        } else if (path.equals("/cours")) {
            gererCours(request, response);
        } else if (path.equals("/ajouterCours")) {
            ajouterCoursForm(request, response);
        } else if (path.equals("/modifierDateCours")) {
            afficherFormulaireModifierDate(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page introuvable.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        if (path.equals("/ajouterEnseignant")) {
            ajouterEnseignant(request, response);
        } else if (path.equals("/ajouterCours")) {
            ajouterCoursAdmin(request, response);
        } else if (path.equals("/sauvegarderDateCours")) {
            sauvegarderNouvelleDate(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page introuvable.");
        }
    }

    private void afficherMenuAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ADMINISTRATEUR) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/MenuAdmin.jsp");
        dispatcher.forward(request, response);
    }

    private void listerEnseignants(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ADMINISTRATEUR) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        List<Enseignant> enseignants = enseignantService.getAllEnseignants();
        request.setAttribute("enseignants", enseignants);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/ListeEnseignants.jsp");
        dispatcher.forward(request, response);
    }

    private void afficherDetailsEnseignant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ADMINISTRATEUR) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Long enseignantId = Long.parseLong(request.getParameter("id"));
        Enseignant enseignant = enseignantService.getEnseignantById(enseignantId);
        List<Cours> coursAssocies = coursService.getCoursParProfesseur(enseignantId);

        request.setAttribute("enseignant", enseignant);
        request.setAttribute("coursAssocies", coursAssocies);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/detailsEnseignantAdmin.jsp");
        dispatcher.forward(request, response);
    }

    private void afficherFormulaireAjoutEnseignant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ADMINISTRATEUR) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        request.setAttribute("enseignant", new Enseignant());
        request.setAttribute("matieres", matiereService.getAllMatieres());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/ajouterEnseignant.jsp");
        dispatcher.forward(request, response);
    }

    private void ajouterEnseignant(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ADMINISTRATEUR) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String contact = request.getParameter("contact"); 
        Long[] matiereIds = request.getParameterValues("matiereIds") != null
                ? Stream.of(request.getParameterValues("matiereIds")).map(Long::valueOf).toArray(Long[]::new)
                : new Long[0];

        Enseignant enseignant = new Enseignant();
        enseignant.setNom(nom);
        enseignant.setPrenom(prenom);
        enseignant.setContact(contact);

        enseignantService.ajouterEnseignantAvecMatieres(enseignant, matiereIds);
        response.sendRedirect(request.getContextPath() + "/admin/enseignants");
    }

    private void gererCours(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ADMINISTRATEUR) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        List<Cours> coursSansProf = coursService.getCoursSansProfesseur();
        List<Cours> coursAssocies = coursService.getTousLesCours();

        request.setAttribute("coursSansProf", coursSansProf);
        request.setAttribute("coursAssocies", coursAssocies);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/CoursListeAdmin.jsp");
        dispatcher.forward(request, response);
    }

    private void ajouterCoursForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ADMINISTRATEUR) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        request.setAttribute("matieres", matiereService.getAllMatieres());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/ajouterCours.jsp");
        dispatcher.forward(request, response);
    }

    private void ajouterCoursAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ADMINISTRATEUR) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String dateCours = request.getParameter("dateCours");
        Long matiereId = Long.parseLong(request.getParameter("matiereId"));

        coursService.ajouterCoursSansProfesseur(LocalDateTime.parse(dateCours), matiereId);
        response.sendRedirect(request.getContextPath() + "/admin/cours");
    }

    private void afficherFormulaireModifierDate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ADMINISTRATEUR) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Long coursId = Long.parseLong(request.getParameter("coursId"));
        Cours cours = coursService.getCoursById(coursId);

        request.setAttribute("cours", cours);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/modifierDateCoursAdmin.jsp");
        dispatcher.forward(request, response);
    }

    private void sauvegarderNouvelleDate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utilisateur utilisateur = utilisateurService.findByIdWithSpecialites(userId);
        if (utilisateur.getRole() != Role.ADMINISTRATEUR) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Long coursId = Long.parseLong(request.getParameter("coursId"));
        String nouvelleDate = request.getParameter("nouvelleDate");

        coursService.modifierDateCours(coursId, LocalDateTime.parse(nouvelleDate));
        response.sendRedirect(request.getContextPath() + "/admin/cours");
    }
}
