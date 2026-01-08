<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- header.jsp txertatu (web-orriko titulua parametro gisa pasatuz) --%>
<jsp:include page="includes/header.jsp">
    <jsp:param name="title" value="Errorea"/>
</jsp:include>
    <main>
        <section>
            <h1>Errore bat gertatu da</h1>

            <c:if test="${not empty erroreak}">
                <div style="
                    color: red;
                    font-weight: bold;
                    margin-bottom: 1em;
                    padding: 0.5em;
                    border: 1px solid red;
                    background: #ffebeb;
                ">
                    <ul style="margin: 0; padding-left: 1.2em;">
                        <c:forEach var="errore" items="${erroreak}">
                            <li>${errore}</li>
                        </c:forEach>
                    </ul>
                </div>
                <a href="${pageContext.request.contextPath}/">
                        Hasierara itzuli
                    </a>
            </c:if>

            <c:if test="${not empty errorea}">
                <div style="color: red; font-weight: bold; margin-bottom: 1em; padding: 0.5em; border: 1px solid red; background: #ffebeb;">
                    ${errorea}
                </div>
                    <div  
                    style="margin-top: 2em; padding: 1.5em; border-radius: 8px;">
                    <p>${errorea}</p>
                    <a href="${pageContext.request.contextPath}/">
                        Hasierara itzuli
                    </a>
                </div>
            </c:if>

        </section>
    </main>
<%-- footer.jsp txertatu --%>
<jsp:include page="includes/footer.jsp"/>