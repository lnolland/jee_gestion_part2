<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Ajouter Enseignant</title>
</head>
<body>
    <h2>Ajouter un Enseignant</h2>
    <form action="${enseignant.id != null ? '/JakartaEEHelloWorld_war_exploded/admin/modifierEnseignant' : '/JakartaEEHelloWorld_war_exploded/admin/ajouterEnseignant'}" method="post">
	    <input type="hidden" name="id" value="${enseignant.id}" />
	
	    <label for="nom">Nom :</label>
	    <input type="text" id="nom" name="nom" value="${enseignant.nom}" required /><br>
	
	    <label for="prenom">Prénom :</label>
	    <input type="text" id="prenom" name="prenom" value="${enseignant.prenom}" required /><br>
	
	    <label for="contact">Contact :</label>
	    <input type="text" id="contact" name="contact" value="${enseignant.contact}" required /><br>
	
	    <label for="matieres">Matiéres :</label>
	    <select id="matieres" name="matiereIds" multiple>
	        <c:forEach var="matiere" items="${matieres}">
	            <option value="${matiere.id}" 
	                <c:if test="${enseignant.matieres.contains(matiere)}">selected</c:if>>
	                ${matiere.nom}
	            </option>
	        </c:forEach>
	    </select><br>
	
	    <button type="submit">${enseignant.id != null ? "Modifier" : "Ajouter"}</button>
	</form>

</body>
</html>