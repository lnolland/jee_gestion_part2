<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Modifier le Contact</title>
</head>
<body>
    <h2>Modifier le Contact</h2>
    <form action="/JakartaEEHelloWorld_war_exploded/etudiants/sauvegarderContact" method="post">
        <label for="contact">Contact :</label>
        <input type="text" id="contact" name="contact" value="${etudiant.contact}" required />
        <button type="submit">Sauvegarder</button>
    </form>
</body>
</html>
