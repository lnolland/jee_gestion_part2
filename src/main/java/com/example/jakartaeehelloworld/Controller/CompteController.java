package com.example.jakartaeehelloworld.Controller;

import com.example.jakartaeehelloworld.Model.Role;
import com.example.jakartaeehelloworld.repository.EnseignantDAO;
import com.example.jakartaeehelloworld.repository.EtudiantDAO;
import com.example.jakartaeehelloworld.repository.UtilisateurDAO;
import com.example.jakartaeehelloworld.service.UtilisateurService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "CompteController", urlPatterns = {"/compte/*"})
public class CompteController extends HttpServlet {

    private final UtilisateurService utilisateurService;

    public CompteController() {
        this.utilisateurService = new UtilisateurService(new UtilisateurDAO(), new EtudiantDAO(), new EnseignantDAO());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        if (path == null || path.equals("/creation")) {
            afficherFormulaireCreation(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page introuvable.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        if (path.equals("/creation")) {
            creerCompte(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action introuvable.");
        }
    }

    private void afficherFormulaireCreation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/CreerCompte.jsp"); // Assurez-vous que ce JSP existe
        dispatcher.forward(request, response);
    }

    private void creerCompte(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String roleParam = request.getParameter("role");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String contact = request.getParameter("contact");

        Role role = null;
        try {
            role = Role.valueOf(roleParam.toUpperCase()); // Convertit la chaîne en énumération
        } catch (IllegalArgumentException | NullPointerException e) {
            request.setAttribute("errorMessage", "Rôle invalide.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/CreerCompte.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            utilisateurService.creerCompte(username, password, role, nom, prenom, contact);
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/CreerCompte.jsp");
            dispatcher.forward(request, response);
        }
    }
}
