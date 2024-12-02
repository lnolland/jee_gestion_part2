<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Ajouter une Note</title>
</head>
<body>

    <!-- Titre de la page -->
    <h2>Ajouter une Note pour ${etudiant.nom} ${etudiant.prenom}</h2>  
    
    <!-- Formulaire pour ajouter une note -->
    <form action="ajouterNote" method="post">
    <label for="etudiant">étudiant :</label>
    <select id="etudiant" name="etudiantId" required>
        <c:forEach items="${etudiants}" var="etudiant">
            <option value="${etudiant.id}">${etudiant.nom} ${etudiant.prenom}</option>
        </c:forEach>
    </select>

    <label for="matiere">Matiére :</label>
    <select id="matiere" name="matiereId" required>
        <c:forEach items="${matieres}" var="matiere">
            <option value="${matiere.id}">${matiere.nom}</option>
        </c:forEach>
    </select>

    <label for="note">Note :</label>
    <input type="number" id="note" name="note" step="0.1" required>

    <button type="submit">Ajouter</button>
</form>

</body>
</html>
