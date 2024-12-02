<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Détails de l'Enseignant</title>
</head>
<body>
    <h2>Détails de l'Enseignant</h2>
    <p><strong>ID :</strong> ${enseignant.id}</p>
    <p><strong>Nom :</strong> ${enseignant.nom}</p>
    <p><strong>Prénom :</strong> ${enseignant.prenom}</p>
    <p><strong>Contact :</strong> ${enseignant.contact}</p>
    <button onclick="window.location.href='/JakartaEEHelloWorld_war_exploded/enseignant/modifierContact'">Modifier le Contact</button>

    <h3>Matiéres Enseignées</h3>
    <ul>
        <c:forEach var="matiere" items="${enseignant.matieres}">
            <li>${matiere.nom}</li>
        </c:forEach>
    </ul>

    <h3>Cours Associés</h3>
    <table border="1">
        <tr>
            <th>Matiére</th>
            <th>Date</th>
        </tr>
        <c:forEach var="cours" items="${coursAssocies}">
            <tr>
                <td>${cours.matiere.nom}</td>
                <td>${cours.dateCours}</td>
            </tr>
        </c:forEach>
    </table>

    <!-- Bouton pour retourner au menu -->
    <button onclick="window.location.href='/JakartaEEHelloWorld_war_exploded/enseignant/menu'">Retour au menu</button>
</body>
</html>
