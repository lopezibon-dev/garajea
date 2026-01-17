<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Saioa Hasi"/>
</jsp:include>

    <main>
        <div class="page-container">

            <h2>Langilearen saioa hasi</h2>

            <section class="form-section">
                
                <c:if test="${not empty erroreak}">
                    <div class="alert alert-error">
                        <ul>
                            <c:forEach var="errore" items="${erroreak}">
                                <li>${errore}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>

                <form class="garajea-form" method="post" action="<c:url value="/langilea/login"/>">
                    <label for="emaila">Posta Elektronikoa:</label>
                    <input type="email" id="emaila" name="emaila" required value="${param.emaila}">

                    <label for="pasahitza">Pasahitza:</label>
                    <input type="password" id="pasahitza" name="pasahitza" required>
                    
                    <div class="form-actions">
                        <button class="garajea-btn" type="submit">Saioa Hasi</button>
                    </div>
                </form>

            </section>

        </div>

    </main>

<jsp:include page="../includes/footer.jsp"/>