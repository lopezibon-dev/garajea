<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://garajea.local/functions/datetime" prefix="dt" %>
<%@ page import="com.unieus.garajea.model.entities.Langilea" %>

<c:set var="langilea" value="${sessionScope.current_user}" scope="request"/>

<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Langile Profila"/>
</jsp:include>

        <style>
        /* Estilo osagarria formularioak maketatzeko */
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

        .agenda-container {
                display: flex;
                flex-direction: row;
                overflow-x: auto;
                gap: 10px;
                padding: 10px;
            }
            .agenda-col {
                min-width: 250px;
                border: 1px solid #ccc;
                background: #fff;
            }
            .agenda-header {
                background: #333;
                color: #fff;
                padding: 10px;
                text-align: center;
                font-weight: bold;
            }
            .blokea {
                width: 100%;
                border-bottom: 1px solid #eee;
                box-sizing: border-box;
                font-size: 0.8rem;
                padding: 4px;
                overflow: hidden;
            }
            .blokea.erreserba {
                background-color: #d1e7dd; /* Berde argia */
                border-left: 5px solid #198754;
                color: #0f5132;
            }
            .blokea.librea {
                background-color: #f8f9fa; /* Grisa */
                color: #aaa;
                display: flex;
                align-items: center;
                justify-content: center;
            }
            .blokea.egunbanatzailea {
                background-color: #333333; /* grisa iluna */
                color: #eee;
                display: flex;
                align-items: center;
                justify-content: center;
            }

        </style>
    <main>
        <h1>Langile Kontua: ${langilea.izena} ${langilea.abizenak}</h1>
        <p>Lanaldiaren hasiera: ${lanaldiHasiera}</p>
    
        <c:if test="${not empty arrakasta}">
            <div style="color: green; font-weight: bold; margin-bottom: 1em; padding: 0.5em; border: 1px solid green; background: #ebffe8;">
                ${arrakasta}
            </div>
        </c:if>
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

        <c:if test="${not empty langilearenErreserbak}">
            <section class="form-section">
                <h2>Zure Erreserbak</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Erreserba ID</th>
                            <th>Langilea</th>
                            <th>Kabina</th>
                            <th>Egoera</th>
                            <th>Bezeroa</th>
                            <th>Ibilgailua</th>
                            <th>Hasiera</th>
                            <th>Amaiera</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="erreserbaInfo" items="${langilearenErreserbak}">
                            <tr>
                                <td>${erreserbaInfo.erreserbaId}</td>
                                <td>${erreserbaInfo.langileIzena}</td>
                                <td>${erreserbaInfo.kabinaIzena}</td>
                                <td>${erreserbaInfo.egoera}</td>
                                <td>${erreserbaInfo.bezeroIzenAbizenak}</td>
                                <td>${erreserbaInfo.ibilgailuInfo}</td>
                                <td>${erreserbaInfo.hasiera}</td>
                                <td>${erreserbaInfo.amaiera}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </section>
        </c:if>

        <div class="agenda-container">
            <div class="agenda-col">
            <div class="agenda-header">${langilea.izena} langilearen agenda</div>
            <c:if test="${not empty langilearenAgenda}">
                <div class="timeline-body">
                    <c:forEach var="blokea" items="${langilearenAgenda}">
                    <c:choose>
                        <c:when test="${blokea.mota == 'ERRESERBA'}">
                        <div class="blokea erreserba" 
                            title="${blokea.erreserbaInfo.bezeroIzenAbizenak}">
                            <strong>${dt:formatDateTime(blokea.erreserbaInfo.hasiera, "HH:mm")} -
                                ${dt:formatDateTime(blokea.erreserbaInfo.amaiera, "HH:mm")}</strong><br>
                            ${blokea.erreserbaInfo.kabinaIzena} - ${erreserba.erreserbaInfo.egoera}<br>
                            ${blokea.erreserbaInfo.bezeroIzenAbizenak} - <i>${blokea.erreserbaInfo.ibilgailuInfo}</i>
                        </div>
                        </c:when>

                        <c:when test="${blokea.mota == 'EGUNBANATZAILEA'}">
                        <div class="blokea egunbanatzailea">
                            ${blokea.egunarenEtiketa}
                        </div>
                        </c:when>
                    </c:choose>
                    </c:forEach>
                </div>
            </c:if>  
            </div>
        </div>

    </main>    
<%-- footer.jsp txertatu --%>
<jsp:include page="../includes/footer.jsp"/>