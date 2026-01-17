<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://garajea.local/functions/datetime" prefix="dt" %>
<%@ page import="com.unieus.garajea.model.entities.Langilea" %>

<c:set var="langilea" value="${sessionScope.current_user}" scope="request"/>

<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Langile Profila"/>
</jsp:include>

    <main>

        <div class="page-container">

            <h2>Langile Kontua: ${langilea.izena} ${langilea.abizenak}</h2>
            <p>Lanaldiaren hasiera: ${lanaldiHasiera}</p>

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

        </div>

    </main>    

<jsp:include page="../includes/footer.jsp"/>