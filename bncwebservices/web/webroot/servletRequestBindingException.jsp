<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:choose>
<c:when test="${header.accept=='application/json'}">
{"error":{"java.class":"ServletRequestBindingException", "message":"Unable to bind request. Please check all HTTP Request Headers and Query Parameters."}}
</c:when>

<c:otherwise>
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<error><class>ServletRequestBindingException</class><message>Unable to bind request. Please check all HTTP Request Headers and Query Parameters.</message></error>
</c:otherwise>
</c:choose>
