<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Modifier le Contact</title>
</head>
<body>
    <h2>Modifier le Contact</h2>
    <form action="/JakartaEEHelloWorld_war_exploded/enseignant/sauvegarderContact" method="post">
        <label for="contact">Nouveau Contact :</label>
        <input type="text" id="contact" name="contact" value="${enseignant.contact}" required />
        <button type="submit">Enregistrer</button>
    </form>

    <!-- Bouton pour retourner aux détails -->
    <button onclick="window.location.href='/JakartaEEHelloWorld_war_exploded/enseignant/details'">Retour aux Détails</button>
</body>
</html>