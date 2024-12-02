<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Liste des Cours</title>
</head>
<body>
    <h1>Cours Sans Professeur</h1>
    <table border="1">
        <tr> 
            <th>Nom de la Matiére</th>
            <th>Date</th>
            <th>Action</th>
        </tr>
        <c:forEach items="${coursSansProf}" var="cours">
            <tr>
                <td>${cours.matiere.nom}</td>
                <td>${cours.dateCours}</td>
                <td>
                	<!-- Bouton pour modifier la date -->
                    <form action="/JakartaEEHelloWorld_war_exploded/admin/modifierDateCours" method="get" style="display:inline;">
                        <input type="hidden" name="coursId" value="${cours.id}" />
                        <button type="submit">Modifier la Date</button>
                    </form>
                    
                 	<form action="/JakartaEEHelloWorld_war_exploded/enseignant/supprimerCoursComplet" method="post" style="display:inline;">
                        <input type="hidden" name="coursId" value="${cours.id}" />
                        <button type="submit">Supprimer Complétement</button>
                	</form>
                </td>
            </tr>
        </c:forEach>
    </table>

    <h1>Cours Associés</h1>
    <table border="1">
        <tr>
            <th>Nom de la Matiére</th>
            <th>Date</th>
            <th>Enseignant</th>
            <th>Actions</th>
        </tr>
        <c:forEach items="${coursAssocies}" var="cours">
            <tr>
                <td>${cours.matiere.nom}</td>
                <td>${cours.dateCours}</td>
                <td>${cours.enseignant.nom} ${cours.enseignant.prenom}</td>
                <td>
                    <!-- Bouton pour modifier la date -->
                    <form action="/JakartaEEHelloWorld_war_exploded/admin/modifierDateCours" method="get" style="display:inline;">
                        <input type="hidden" name="coursId" value="${cours.id}" />
                        <button type="submit">Modifier la Date</button>
                    </form>
                    
                    <form action="/JakartaEEHelloWorld_war_exploded/enseignant/supprimerCoursComplet" method="post" style="display:inline;">
                        <input type="hidden" name="coursId" value="${cours.id}" />
                        <button type="submit">Supprimer Complétement</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
    <!-- Lien pour ajouter un cours-->    
    <button onclick="window.location.href='/JakartaEEHelloWorld_war_exploded/admin/ajouterCours'">Ajouter un cours</button>
    
</body>
</html>