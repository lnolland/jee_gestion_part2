<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Modifier la Date du Cours</title>
</head>
<body>
    <h1>Modifier la Date du Cours</h1>
    <form action="/JakartaEEHelloWorld_war_exploded/admin/sauvegarderDateCours" method="post">
        <input type="hidden" name="coursId" value="${cours.id}" />
        <label for="nouvelleDate">Nouvelle Date :</label>
        <input type="datetime-local" id="nouvelleDate" name="nouvelleDate" required />
        <button type="submit">Enregistrer</button>
    </form>
</body>
</html>
