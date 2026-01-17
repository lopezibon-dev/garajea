<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.unieus.garajea.model.entities.Bezeroa" %>

<c:set var="bezeroa" value="${sessionScope.current_user}" scope="request"/>

<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Bezeroaren Ibilgailuen zerrenda"/>
</jsp:include>
  <main>
    <div class="page-container ibilgailua-page">

      <h1>Nire Kontua: ${bezeroa.izena} ${bezeroa.abizenak}</h1>

      <section class="list-section">

        <h2>Nire ibilgailuak</h2>

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

        <div class="list-actions">
            <a class="garajea-btn"
                href="${pageContext.request.contextPath}/ibilgailua/berria">
                + Ibilgailu berria
            </a>
        </div>

        <c:choose>
            <c:when test="${empty ibilgailuak}">
                <p>Ez dago ibilgailurik erregistratuta.</p>
            </c:when>

            <c:otherwise>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Matrikula</th>
                            <th>Marka</th>
                            <th>Modeloa</th>
                            <th>Urtea</th>
                            <th>Ekintzak</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ibilgailua" items="${ibilgailuak}">
                            <tr>
                                <td>${ibilgailua.matrikula}</td>
                                <td>${ibilgailua.marka}</td>
                                <td>${ibilgailua.modeloa}</td>
                                <td>${ibilgailua.urtea}</td>
                                <td class="row-actions">

                                    <a class="garajea-btn garajea-btn--small"
                                        href="${pageContext.request.contextPath}/ibilgailua/editatu?id=${ibilgailua.ibilgailuaId}">
                                        Editatu
                                    </a>

                                    <form method="post"
                                          action="${pageContext.request.contextPath}/ibilgailua/ezabatu"
                                          class="inline-form"
                                          onsubmit="return confirm('Ziur zaude ibilgailua ezabatu nahi duzula?');">

                                        <input type="hidden"
                                                name="ibilgailuaId"
                                                value="${ibilgailua.ibilgailuaId}" />

                                        <button type="submit"
                                                class="garajea-btn garajea-btn--danger garajea-btn--small">
                                            Ezabatu
                                        </button>
                                    </form>

                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>

      </section>
          
    </div>
  </main> 
<jsp:include page="../includes/footer.jsp"/>