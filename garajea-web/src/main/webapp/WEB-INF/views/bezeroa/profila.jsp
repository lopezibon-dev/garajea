<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.unieus.garajea.model.entities.Bezeroa" %>

<c:set var="bezeroa" value="${sessionScope.current_user}" scope="request"/>

<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Nire Profila"/>
</jsp:include>
    <main>
        <div class="page-container">

            <h2>Nire Kontua: ${bezeroa.izena} ${bezeroa.abizenak}</h2>

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

            <div class="profile-sections">
                
                <section class="form-section">

                    <h2>Datu Pertsonalak Eguneratu</h2>
                    <p>Kudeatu zure izena, abizenak, emaila eta telefonoa.</p>

                    <form class="garajea-form" method="post" action="<c:url value="/bezeroa/datuakEguneratu"/>">
                        
                        <label for="izena">Izena:</label>
                        <input type="text" id="izena" name="izena" required value="${bezeroa.izena}">

                        <label for="abizenak">Abizenak:</label>
                        <input type="text" id="abizenak" name="abizenak" required value="${bezeroa.abizenak}">

                        <label for="emaila">Posta Elektronikoa:</label>
                        <input type="email" id="emaila" name="emaila" required value="${bezeroa.emaila}">
                        
                        <label for="telefonoa">Telefonoa:</label>
                        <input type="tel" id="telefonoa" name="telefonoa" value="${bezeroa.telefonoa}">
                        <div class="form-actions">
                            <button class="garajea-btn" type="submit">Datuak Gorde</button>
                        </div>
                    </form>
                </section>
                
                <section class="form-section">
                    <h2>Pasahitza Aldatu</h2>
                    <p>Segurtasunagatik, pasahitza aldatzeko formulario bereizia erabiltzen dugu.</p>

                    <form class="garajea-form" method="post" action="<c:url value="/bezeroa/pasahitzaEguneratu"/>">
                        <label for="pasahitzaBerria">Pasahitz Berria:</label>
                        <input type="password" id="pasahitzaBerria" name="pasahitzaBerria" required>
                        <div class="form-actions">
                            <button class="garajea-btn garajea-btn--danger" type="submit">Pasahitza Eguneratu</button>
                        </div>
                    </form>
                </section>

            </div>

        </div>

    </main> 

<jsp:include page="../includes/footer.jsp"/>