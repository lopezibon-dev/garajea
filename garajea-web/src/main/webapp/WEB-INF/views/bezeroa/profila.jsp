<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.unieus.garajea.model.entities.Bezeroa" %>

<c:set var="bezeroa" value="${sessionScope.current_user}" scope="request"/>

<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Nire Profila"/>
</jsp:include>

        <style>
        /* Estilo osagarria formularioak banatzeko */
        .profile-container {
            display: flex;
            flex-direction: column;
            gap: 2em;
        }
        @media (min-width: 700px) {
            .profile-container {
                flex-direction: row;
                justify-content: space-between;
            }
            .profile-container > * {
                flex: 1;
                min-width: 48%;
            }
        }
        .form-section {
            padding: 1.5em;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        </style>

        <h1>Nire Kontua: ${bezeroa.izena} ${bezeroa.abizenak}</h1>

        <c:if test="${not empty arrakasta}">
            <div style="color: green; font-weight: bold; margin-bottom: 1em; padding: 0.5em; border: 1px solid green; background: #ebffe8;">
                ${arrakasta}
            </div>
        </c:if>
        <c:if test="${not empty errorea}">
            <div style="color: red; font-weight: bold; margin-bottom: 1em; padding: 0.5em; border: 1px solid red; background: #ffebeb;">
                ${errorea}
            </div>
        </c:if>
        
        <div class="profile-container">
            <section class="form-section">
                <h2>Datu Pertsonalak Eguneratu</h2>
                <p>Kudeatu zure izena, abizenak, emaila eta telefonoa.</p>

                <form action="<c:url value="/bezeroa/datuakEguneratu"/>" method="POST" class="login-form">
                    
                    <label for="izena">Izena:</label>
                    <input type="text" id="izena" name="izena" required value="${bezeroa.izena}">

                    <label for="abizenak">Abizenak:</label>
                    <input type="text" id="abizenak" name="abizenak" required value="${bezeroa.abizenak}">

                    <label for="emaila">Posta Elektronikoa:</label>
                    <input type="email" id="emaila" name="emaila" required value="${bezeroa.emaila}">
                    
                    <label for="telefonoa">Telefonoa:</label>
                    <input type="tel" id="telefonoa" name="telefonoa" value="${bezeroa.telefonoa}">

                    <button type="submit">Datuak Gorde</button>
                </form>
            </section>
            
            <section class="form-section">
                <h2>Pasahitza Aldatu</h2>
                <p>Segurtasunagatik, pasahitza aldatzeko formulario bereizia erabiltzen dugu.</p>

                <form action="<c:url value="/bezeroa/pasahitzaEguneratu"/>" method="POST" class="login-form">
                    <label for="pasahitzaBerria">Pasahitz Berria:</label>
                    <input type="password" id="pasahitzaBerria" name="pasahitzaBerria" required>
                    
                    <button type="submit" style="background: #e74c3c;">Pasahitza Eguneratu</button>
                </form>
            </section>
        </div>
        
<%-- footer.jsp txertatu --%>
<jsp:include page="../includes/footer.jsp"/>