<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:choose>
<c:when test="${header.accept=='application/json'}">
{"error":{"java.class":"NotSupportedException", "message":"Request method ${pageContext.request.method} is not supported"}}
</c:when>

<c:otherwise>
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<error><class>NotSupportedException</class><message>Request method ${pageContext.request.method} is not supported</message></error>
</c:otherwise>
</c:choose>
