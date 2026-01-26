<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.unieus.garajea.model.entities.Bezeroa" %>

<c:set var="bezeroa" value="${sessionScope.current_user}" scope="request"/>

<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Ibilgailuen Formularioa"/>
</jsp:include>
  <main>
    <div class="page-container ibilgailua-page">

        <h2>Nire Kontua: ${bezeroa.izena} ${bezeroa.abizenak}</h2>

        <section class="form-section">

          <h2>
            <c:choose>
                <c:when test="${not empty ibilgailuaForm and ibilgailuaForm.ibilgailuaId ne null}">
                    Ibilgailua editatu
                </c:when>
                <c:otherwise>
                    Ibilgailu berria
                </c:otherwise>
            </c:choose>
          </h2>

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
          <c:set var="actionPath" value="${pageContext.request.contextPath}/ibilgailua" />
          <c:choose>
            <c:when test='${not empty ibilgailuaForm and ibilgailuaForm.ibilgailuaId ne null}'>
                <c:set var="actionPath" value="${actionPath}/eguneratu" />
            </c:when>
            <c:otherwise>
                <c:set var="actionPath" value="${actionPath}/sortu" />
            </c:otherwise>
          </c:choose>
          <form class="garajea-form"
              method="post"
              action="${actionPath}">
         
              <c:if test="${not empty ibilgailuaForm and ibilgailuaForm.ibilgailuaId ne null}">
                  <input type="hidden"
                          name="ibilgailuaId"
                          value="${ibilgailuaForm.ibilgailuaId}" />
              </c:if>

              <div class="form-field">
                  <label for="matrikula">Matrikula</label>
                  <input type="text"
                          id="matrikula"
                          name="matrikula"
                          value="${ibilgailuaForm.matrikula}" />
              </div>

              <div class="form-field">
                  <label for="marka">Marka</label>
                  <input type="text"
                          id="marka"
                          name="marka"
                          value="${ibilgailuaForm.marka}" />
              </div>

              <div class="form-field">
                  <label for="modeloa">Modeloa</label>
                  <input type="text"
                          id="modeloa"
                          name="modeloa"
                          value="${ibilgailuaForm.modeloa}" />
              </div>

              <div class="form-field">
                  <label for="urtea">Urtea</label>
                  <input type="number"
                          id="urtea"
                          name="urtea"
                          value="${ibilgailuaForm.urtea}" />
              </div>

              <div class="form-actions">
                  <button class="garajea-btn" type="submit">
                      <c:choose>
                          <c:when test="${not empty ibilgailuaForm and ibilgailuaForm.ibilgailuaId ne null}">
                              Eguneratu
                          </c:when>
                          <c:otherwise>
                              Sortu
                          </c:otherwise>
                      </c:choose>
                  </button>
              </div>
          </form>

        </section>
          
    </div>
  </main> 
<jsp:include page="../includes/footer.jsp"/>