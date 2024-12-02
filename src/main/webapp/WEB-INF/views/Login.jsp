 <%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Connexion</title>
</head>
<body>
    <h2>Connexion</h2>
    <form action="login" method="post">
        <label for="username">Nom d'utilisateur:</label>
        <input type="text" id="username" name="username" required>
        
        <label for="password">Mot de passe:</label>
        <input type="password" id="password" name="password" required>
        
        <input type="submit" value="Se connecter">
        
        <p>Pas de compte ? <a href="/JakartaEEHelloWorld_war_exploded/compte/creation">Créer un compte</a></p>
        
        <%-- Affichage du message d'erreur si présent --%>
		<c:if test="${not empty errorMessage}">
		    <p style="color:red;">${errorMessage}</p>
		</c:if>
        
    </form>
</body>
</html>
