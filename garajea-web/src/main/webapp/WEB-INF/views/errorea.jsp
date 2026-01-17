<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="includes/header.jsp">
    <jsp:param name="title" value="Errorea"/>
</jsp:include>

    <main>
        <div class="page-container">
            <section>

                <h2>Errore bat gertatu da</h2>

                <c:if test="${not empty erroreak}">
                    <div class="alert alert-error">
                        <ul>
                            <c:forEach var="errore" items="${erroreak}">
                                <li>${errore}</li>
                            </c:forEach>
                        </ul>
                    </div>
                    <a href="${pageContext.request.contextPath}/">
                            Hasierara itzuli
                        </a>
                </c:if>

            </section>
        </div>
    </main>
<jsp:include page="includes/footer.jsp"/>