<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Menu Administrateur</title>
</head>
<body>
    <h2>Bienvenue, Administrateur ${sessionScope.username}</h2>
    <ul>
        <li><a href="/JakartaEEHelloWorld_war_exploded/admin/enseignants">Gérer les enseignants</a></li>
        <li><a href="/JakartaEEHelloWorld_war_exploded/etudiants">Gérer les étudiants</a></li>
        <li><a href="/JakartaEEHelloWorld_war_exploded/admin/cours">Gérer les cours</a></li>
        <li><a href="/JakartaEEHelloWorld_war_exploded/login/logout">Se déconnecter</a></li>
    </ul>
</body>
</html>
