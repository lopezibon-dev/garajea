<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- header.jsp txertatu (web-orriko titulua parametro gisa pasatuz) --%>
<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Erregistratu"/>
</jsp:include>
    <main>
        <div class="page-container">
            <h2>Bezeroen Erregistroa</h2>
            <section class="form-section">
            
                <c:if test="${not empty arrakasta}">
                    <div class="alert alert-success">
                        ${arrakasta}
                    </div>
                </c:if>

                <c:if test="${not empty erroreak}">
                    <div class="alert alert-error">
                        <ul>
                            <c:forEach var="errore" items="${erroreak}">
                                <li>${errore}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>

                <p>Bete formularioa zure kontua sortzeko.</p>

                <form class="garajea-form" method="post" action="<c:url value="/bezeroa/erregistratu"/>">

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

                    <div class="form-actions">
                        <button class="garajea-btn" type="submit">Erregistratu</button>
                    </div>

                </form>
                
                <p style="margin-top: 1em;">Baduzu konturik? <a href="<c:url value="/bezeroa/login"/>">Saioa Hasi</a></p>

            </section>

        </div>

    </main>    
<jsp:include page="../includes/footer.jsp"/>