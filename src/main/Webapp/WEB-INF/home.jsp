<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>


<html>
<body>
<h2>Welcome, ${username}</h2>
${users}
    <c:forEach var = "i" begin = "1" end = "5">
        Item <c:out value = "${i.username}"/><p>
    </c:forEach>
</body>
</html>
