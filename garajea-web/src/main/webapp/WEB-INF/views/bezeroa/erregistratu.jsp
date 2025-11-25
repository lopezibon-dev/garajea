<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- header.jsp txertatu (web-orriko titulua parametro gisa pasatuz) --%>
<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Erregistratu"/>
</jsp:include>

        <section class="register-page">
            <h1>Bezeroen Erregistroa</h1>
            <p>Bete formularioa zure kontua sortzeko.</p>

            <c:if test="${not empty errorea}">
                <div style="color: red; font-weight: bold; margin-bottom: 1em; padding: 0.5em; border: 1px solid red; background: #ffebeb;">
                    ${errorea}
                </div>
            </c:if>

            <form action="<c:url value="/bezeroa/erregistratu"/>" method="POST" class="login-form" style="max-width: 450px;">

                <label for="izena">Izena:</label>
                <input type="text" id="izena" name="izena" required value="${param.izena}">

                <label for="abizenak">Abizenak:</label>
                <input type="text" id="abizenak" name="abizenak" required value="${param.abizenak}">

                <label for="emaila">Posta Elektronikoa:</label>
                <input type="email" id="emaila" name="emaila" required value="${param.emaila}">
                
                <label for="telefonoa">Telefonoa:</label>
                <input type="tel" id="telefonoa" name="telefonoa" value="${param.telefonoa}">

                <label for="pasahitza">Pasahitza:</label>
                <input type="password" id="pasahitza" name="pasahitza" required>

                <button type="submit">Erregistratu</button>
            </form>
            
            <p style="margin-top: 1em;">Baduzu konturik? <a href="<c:url value="/bezeroa/login"/>">Saioa Hasi</a></p>
        </section>
        
<%-- footer.jsp txertatu --%>
<jsp:include page="../includes/footer.jsp"/>