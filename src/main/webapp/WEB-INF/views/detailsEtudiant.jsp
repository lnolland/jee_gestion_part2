<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Détails de l'étudiant</title>
</head>
<body>
    <h2>Détails de l'étudiant</h2>
    <p><strong>ID :</strong> ${etudiant.id}</p>
    <p><strong>Nom :</strong> ${etudiant.nom}</p>
    <p><strong>Prénom :</strong> ${etudiant.prenom}</p>
    <p><strong>Date de Naissance :</strong> ${etudiant.dateNaissance}</p>
    <p><strong>Contact :</strong> ${etudiant.contact}</p>
    <button onclick="window.location.href='modifierContact'">Modifier mon contact</button>

    <!-- Table des notes -->
    <h3>Mes Notes</h3>
    <table border="1">
        <tr>
            <th>Matiére</th>
            <c:forEach var="index" begin="1" end="${maxNoteCount}">
                <th>Note ${index}</th>
            </c:forEach>
            <th>Moyenne de l'étudiant</th>
            <th>Moyenne Générale de la Matiére</th>
        </tr>

        <c:forEach var="entry" items="${notesData}">
            <tr>
                <td>${entry.value['matiereNom']}</td>
                <c:forEach var="note" items="${entry.value['notes']}">
                    <td>${note.note}</td>
                </c:forEach>
                <c:forEach var="i" begin="1" end="${maxNoteCount - fn:length(entry.value['notes'])}">
                    <td></td>
                </c:forEach>
                <td>${entry.value['moyenneEtudiant']}</td>
                <td>${entry.value['moyenneGenerale']}</td>
            </tr>
        </c:forEach>
    </table>

    <!-- Table des cours inscrits -->
    <h3>Mes Cours Inscrits</h3>
    <table border="1">
        <tr>
            <th>Nom du Cours</th>
            <th>Date du Cours</th>
            <th>Enseignant</th>
            <th>Action</th>
        </tr>
        <c:forEach var="inscription" items="${etudiant.inscriptions}">
            <tr>
                <td>${inscription.cours.matiere.nom}</td>
                <td>${inscription.cours.dateCours}</td>
                <td>${inscription.cours.enseignant.nom} ${inscription.cours.enseignant.prenom}</td>
                <td>
                    <form action="/etudiants/desinscrire" method="post">
                        <input type="hidden" name="coursId" value="${inscription.cours.id}" />
                        <button type="submit">Se désinscrire</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>

    <!-- Bouton pour générer le relevé de notes -->
    <button onclick="window.location.href='/etudiants/genererReleve?id=${etudiant.id}'">Relevé de notes</button>
</body>
</html>
