<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="header">
    <!-- Lien vers le menu selon le r�le -->
    <c:choose>
        <c:when test="${sessionScope.userRole == 'ADMINISTRATEUR'}">
            <a href="/admin/menu">Menu Admin</a>
        </c:when>
        <c:when test="${sessionScope.userRole == 'ENSEIGNANT'}">
            <a href="/enseignant/menu">Menu Enseignant</a>
        </c:when>
        <c:when test="${sessionScope.userRole == 'ETUDIANT'}">
            <a href="/etudiants/menu">Menu �tudiant</a>
        </c:when>
        <c:otherwise>
            <a href="/">Accueil</a>
        </c:otherwise>
    </c:choose>

    <!-- Lien pour la d�connexion -->
    <a href="/login/logout">D�connexion</a>
</div>
