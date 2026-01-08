<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- header.jsp txertatu (web-orriko titulua parametro gisa pasatuz) --%>
<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Saioa Hasi"/>
</jsp:include>
    <main>
        <section class="login-page">
            <h1>Langilearen saioa hasi</h1>

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
            </c:if>

            <form action="<c:url value="/langilea/login"/>" method="POST" class="login-form" style="max-width: 350px;">
                <label for="emaila">Posta Elektronikoa:</label>
                <input type="email" id="emaila" name="emaila" required value="${param.emaila}">

                <label for="pasahitza">Pasahitza:</label>
                <input type="password" id="pasahitza" name="pasahitza" required>
                
                <button type="submit">Saioa Hasi</button>
            </form>
        </section>
    </main>
<%-- footer.jsp txertatu --%>
<jsp:include page="../includes/footer.jsp"/>