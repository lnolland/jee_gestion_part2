<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Détails de l'Enseignant</title>
</head>
<body>
    <h1>Détails de l'Enseignant</h1>

    <!-- Informations de l'enseignant -->
    <table border="1">
        <tr>
            <th>Nom</th>
            <td>${enseignant.nom}</td>
        </tr>
        <tr>
            <th>Prénom</th>
            <td>${enseignant.prenom}</td>
        </tr>
        <tr>
            <th>Contact</th>
            <td>${enseignant.contact}</td>
        </tr>
        <tr>
            <th>Matiéres</th>
            <td>
                <ul>
                    <c:forEach var="matiere" items="${enseignant.matieres}">
                        <li>${matiere.nom}</li>
                    </c:forEach>
                </ul>
            </td>
        </tr>
    </table>

    <h2>Cours Associés</h2>

    <!-- Liste des cours associés -->
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

    <br>
    <a href="/JakartaEEHelloWorld_war_exploded/admin/enseignants">Retour é la liste des enseignants</a>
</body>
</html>
