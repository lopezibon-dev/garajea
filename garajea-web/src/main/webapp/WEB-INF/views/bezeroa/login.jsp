<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- header.jsp txertatu (web-orriko titulua parametro gisa pasatuz) --%>
<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Saioa Hasi"/>
</jsp:include>

        <section class="login-page">
            <h1>Bezeroen Saioa Hasi</h1>
            <p>Sartu zure datuekin zure ibilgailuak kudeatzeko eta erreserbak egiteko.</p>

            <c:if test="${not empty errorea}">
                <div style="color: red; font-weight: bold; margin-bottom: 1em; padding: 0.5em; border: 1px solid red; background: #ffebeb;">
                    ${errorea}
                </div>
            </c:if>

            <form action="<c:url value="/bezeroa/login"/>" method="POST" class="login-form" style="max-width: 350px;">
                <label for="emaila">Posta Elektronikoa:</label>
                <input type="email" id="emaila" name="emaila" required value="${param.emaila}">

                <label for="pasahitza">Pasahitza:</label>
                <input type="password" id="pasahitza" name="pasahitza" required>
                
                <button type="submit">Saioa Hasi</button>
            </form>
            
            <p style="margin-top: 1em;">Ez duzu konturik? <a href="<c:url value="/bezeroa/erregistratu"/>">Erregistratu orain</a></p>
        </section>

<%-- footer.jsp txertatu --%>
<jsp:include page="../includes/footer.jsp"/>