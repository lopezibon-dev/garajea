<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://garajea.local/functions/datetime" prefix="dt" %>
<%@ page import="com.unieus.garajea.model.entities.Langilea" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.List" %>

<c:set var="langilea" value="${sessionScope.current_user}" scope="request"/>

<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Erreserba agendak, kabinaka"/>
</jsp:include>

    <style>
      .form-section {
          padding: 1.5em;
          border: 1px solid #ccc;
          border-radius: 4px;
      }

      :root {
        --agenda-flex: 240px;
        --agenda-min: 230px;
        --agenda-max: 350px;
      }

      .agenda-container {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        gap: 1rem;
        overflow-x: auto;
        padding: 10px;
      }

      /* Agenda elementua : oinarrizko bertsioa (mobile-first) */
      .agenda-col {
        flex: 1 1 var(--agenda-flex);
        /* oinarrizko zabalera */
        min-width: var(--agenda-min);
        /* gutxieneko zabalera */
        max-width: var(--agenda-max);
        /* zabalera handiena */
        border: 1px solid #ccc;
        background: #fff;
      }

      /* sm */
      @media (min-width: 576px) {
        :root {
          --agenda-flex: 250px;
        }

        .agenda-container {
          justify-content: flex-start;
        }

        .agenda-col {}
      }

      /* md (>=768px) */
      @media (min-width: 768px) {
        :root {
          --agenda-flex: 260px;
        }

        .agenda-col {
          flex-basis: 45%;
          min-width: 240px;
          max-width: 360px;
        }
      }

      /* lg (>=992px) – navbar expand-lg -rekin bat egin */
      @media (min-width: 992px) {
        :root {
          --agenda-flex: 280px;
          --agenda-max: 380px;
        }

        .agenda-col {
          flex-basis: 30%;
          /* normalean 3 zutabe, edo 4 lekurik badago */
          min-width: 260px;
          max-width: 380px;
        }
      }

      /* xl (>=1200px) */
      @media (min-width: 1200px) {
        :root {
          --agenda-flex: 300px;
          --agenda-max: 380px;
        }
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
        background-color: #d1e7dd;
        /* Berde argia */
        border-left: 5px solid #198754;
        color: #0f5132;
      }

      .blokea.librea {
        background-color: #f8f9fa;
        /* Grisa */
        color: #aaa;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .blokea.egunbanatzailea {
        background-color: #333333;
        /* grisa iluna */
        color: #eee;
        display: flex;
        align-items: center;
        justify-content: center;
      }

    </style>

        <h1>Langile Kontua: ${langilea.izena} ${langilea.abizenak}</h1>
    
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

        <c:if test="${not empty egunekoErreserbak}">
            <section class="form-section">
                <h2>Eguneko Erreserbak</h2>
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
                        <c:forEach var="erreserbaInfo" items="${egunekoErreserbak}">
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
          <c:forEach var="entry" items="${egunekoAgendenMapa.entrySet()}">
            <div class="agenda-col">
              <div class="agenda-header">${entry.key} agenda</div>
              <c:if test="${not empty entry.value}">
                <div class="timeline-body">
                    <c:forEach var="blokea" items="${entry.value}">
                    <c:choose>
                        <c:when test="${blokea.mota == 'ERRESERBA'}">
                        <div class="blokea erreserba" 
                            title="${blokea.erreserbaInfo.bezeroIzenAbizenak}">
                            <strong>${dt:formatDateTime(blokea.erreserbaInfo.hasiera, "HH:mm")} -
                                ${dt:formatDateTime(blokea.erreserbaInfo.amaiera, "HH:mm")}</strong><br>
                            ${blokea.erreserbaInfo.kabinaIzena} - ${blokea.erreserbaInfo.egoera} - Langilea: ${blokea.erreserbaInfo.langileIzena}
                            <br>
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
          </c:forEach>  
        </div>
   
<%-- footer.jsp txertatu --%>
<jsp:include page="../includes/footer.jsp"/>