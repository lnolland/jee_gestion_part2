<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Ajouter un Cours</title>
</head>
<body>
    <h2>Ajouter un Cours</h2>

    <form action="${role == 'ADMINISTRATEUR' ? '/JakartaEEHelloWorld_war_exploded/admin/ajouterCours' : '/JakartaEEHelloWorld_war_exploded/enseignant/ajouterCours'}" method="post">
        <label for="dateCours">Date et Heure :</label>
        <input type="datetime-local" id="dateCours" name="dateCours" required>
        
        <label for="matiereId">Matiére :</label>
        <select id="matiereId" name="matiereId" required>
            <c:forEach items="${matieres}" var="matiere">
                <option value="${matiere.id}">${matiere.nom}</option>
            </c:forEach>
        </select>
        
        <!-- Si l'utilisateur est un enseignant, transmettre l'enseignantId -->
        <c:if test="${role == 'ENSEIGNANT'}">
            <input type="hidden" name="enseignantId" value="${enseignantId}">
        </c:if>

        <button type="submit">Ajouter</button>
    </form>
</body>
</html>
