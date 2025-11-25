<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="eu">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title} | DIY Garajea Proiektua</title>
    <link rel="stylesheet" href="<c:url value="/css/styles.css"/>">
</head>
<body>
    <header>
        <div class="logo-login-container">
            <div class="logo">
                <img src="<c:url value="/img/garajea-740-300.png"/>" alt="DIY Garajea Logoa"> 
            </div>
            
            <c:choose>
                <c:when test="${not empty sessionScope.current_user}">
                    <%-- Vista de usuario logeado --%>
                    <p style="color: #fff; margin: 0; font-weight: bold;">Kaixo, ${sessionScope.current_user.izena}!</p>
                    <a href="<c:url value="/bezeroa/logout"/>" style="color: #f39c12; font-weight: bold;">(Saioa Amaitu)</a>
                </c:when>
                <c:otherwise>
                    <div class="login-form" style="display: none;"></div>
                </c:otherwise>
            </c:choose>
        </div>
    </header>
<nav>
        <ul class="main-menu">
            <li><a href="<c:url value="/"/>">Hasiera</a></li>
            <li><a href="<c:url value="/nolaDabil"/>">Nola dabil?</a></li>
            <li><a href="<c:url value="/kabinaErreserba"/>">Kabinen erreserba</a></li>
            <li><a href="<c:url value="/zureMateriala"/>">Zure materiala</a></li>
            <li><a href="<c:url value="/kontaktua"/>">Kontaktua</a></li>
            
            <c:choose>
                <c:when test="${empty sessionScope.current_user}">
                   
                    <li><a href="<c:url value="/bezeroa/erregistratu"/>">Erregistratu</a></li>
                    <li><a href="<c:url value="/bezeroa/login"/>">Saioa Hasi</a></li>
                </c:when>
                <c:otherwise>
                     <li><a href="<c:url value="/bezeroa/profila"/>">Nire Profila</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
    <main>
  