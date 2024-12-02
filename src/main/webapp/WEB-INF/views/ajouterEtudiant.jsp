<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Ajouter / Modifier un Étudiant</title>
</head>
<body>
    <h2>${etudiant.id != null ? "Modifier" : "Ajouter"} un Étudiant</h2>

    <form action="${etudiant.id != null ? '/JakartaEEHelloWorld_war_exploded/etudiants/modifierEtudiant' : '/JakartaEEHelloWorld_war_exploded/etudiants/ajouterEtudiant'}" method="post">
        <input type="hidden" name="id" value="${etudiant.id}" />

        <label for="nom">Nom:</label>
        <input type="text" id="nom" name="nom" value="${etudiant.nom}" required/><br>

        <label for="prenom">Prénom:</label>
        <input type="text" id="prenom" name="prenom" value="${etudiant.prenom}" required/><br>

        <label for="dateNaissance">Date de Naissance:</label>
        <input type="date" id="dateNaissance" name="dateNaissance" value="${etudiant.dateNaissance}" required/><br>

        <label for="contact">Contact:</label>
        <input type="text" id="contact" name="contact" value="${etudiant.contact}" required/><br>

        <button type="submit">${etudiant.id != null ? "Mettre à jour" : "Ajouter"}</button>
    </form>

    <button onclick="window.location.href='/etudiants'">Annuler</button>
</body>
</html>