package com.example.jakartaeehelloworld.Controller;

import com.example.jakartaeehelloworld.Model.Utilisateur;
import com.example.jakartaeehelloworld.Model.Role;
import com.example.jakartaeehelloworld.repository.UtilisateurDAO;
import com.example.jakartaeehelloworld.util.HibernateUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.hibernate.Session;

@WebServlet(name = "LoginController", urlPatterns = {"/login", "/login/logout"})
public class LoginController extends HttpServlet {

    private final UtilisateurDAO utilisateurDAO;

    public LoginController() {
        this.utilisateurDAO = new UtilisateurDAO(); // Assurez-vous d'avoir un DAO pour `Utilisateur`
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();

        if ("/login/logout".equals(action)) {
            logout(request, response);
        } else {
            // Redirige vers le JSP sous WEB-INF/views
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/Login.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processLogin(request, response);
    }

    private void processLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Utilisateur utilisateur = utilisateurDAO.findByUsernameAndPassword(session, username, password);

            if (utilisateur != null) {
                HttpSession httpSession = request.getSession();
                httpSession.setAttribute("userId", utilisateur.getId());
                httpSession.setAttribute("username", utilisateur.getUsername());
                httpSession.setAttribute("role", utilisateur.getRole());
                httpSession.setAttribute("userRole", utilisateur.getRole().toString());

                if (utilisateur.getRole() == Role.ENSEIGNANT) {
                    httpSession.setAttribute("specialites", utilisateur.getEnseignant().getMatieres());
                } else {
                    httpSession.setAttribute("specialites", null);
                }

                switch (utilisateur.getRole()) {
                    case ADMINISTRATEUR:
                        response.sendRedirect(request.getContextPath() + "/admin/menu");
                        break;
                    case ENSEIGNANT:
                        response.sendRedirect(request.getContextPath() + "/enseignant/menu");
                        break;
                    case ETUDIANT:
                        response.sendRedirect(request.getContextPath() + "/etudiants/menu");
                        break;
                    default:
                        request.setAttribute("errorMessage", "Rôle utilisateur inconnu.");
                        RequestDispatcher dispatcher = request.getRequestDispatcher("Login.jsp");
                        dispatcher.forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "Nom d'utilisateur ou mot de passe incorrect");
                RequestDispatcher dispatcher = request.getRequestDispatcher("Login.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Erreur lors de la connexion : " + e.getMessage(), e);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false); // Obtenir la session en cours si elle existe
        if (session != null) {
            session.invalidate(); // Invalider la session
        }
        response.sendRedirect(request.getContextPath() + "/login"); // Rediriger vers la page de connexion
    }
}
