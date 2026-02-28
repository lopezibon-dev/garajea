<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="eu">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title} | DIY Garajea Proiektua</title>
    <link rel="stylesheet" href="<c:url value="/css/styles.css"/>">
    <link
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
    rel="stylesheet">

</head>
<body>
	<style>
			/* lg breakpoint-etik aurrera */
		@media (min-width: 992px) {
			.navbar>.container {
					display: flex;
					align-items: center;
					justify-content: center; /* A + BUTTON + DIV.collapse erdiratu aitarekiko*/
			}

			.navbar-collapse-auto {
					flex: 0 0 auto;	/* zabalera = barneko edukia (UL-a) */
			}
		}
	</style>
	<header>
		<div class="logo-container">
				<div class="logo">
						<img src="<c:url value="/img/garajea-740-300.png"/>" alt="DIY Garajea Logoa"> 
				</div>
		</div>
  </header>
	<nav class="navbar navbar-expand-md navbar-dark bg-dark">
		<div class="container">
			<a class="navbar-brand" href="${pageContext.request.contextPath}/">
				DIY Garajea
			</a>

			<button class="navbar-toggler d-lg-none" type="button"
					data-bs-toggle="collapse"
					data-bs-target="#navbarContent"
					aria-controls="navbarContent"
					aria-expanded="false"
					aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>

			<div class="collapse navbar-collapse navbar-collapse-auto" id="navbarContent">

				<c:choose>
					<c:when test="${empty sessionScope.current_user_type}">
						<ul class="navbar-nav mb-lg-0">
							<li class="nav-item">
								<a class="nav-link" href="${pageContext.request.contextPath}/">
									Hasiera
								</a>
							</li>
							<li class="nav-item">
								<a class="nav-link" href="${pageContext.request.contextPath}/bezeroa/erregistratu">
									Erregistratu
								</a>
							</li>						
							<li class="nav-item">
								<a class="nav-link" href="${pageContext.request.contextPath}/bezeroa/login">
									Bezeroa Login
								</a>
							</li>
							<li class="nav-item">
								<a class="nav-link" href="${pageContext.request.contextPath}/langilea/login">
									Langilea Login
								</a>
							</li>
						</ul>
					</c:when>
					<c:when test="${sessionScope.current_user_type eq 'bezeroa'}">
						<ul class="navbar-nav mb-lg-0">
							<li class="nav-item">
								<a class="nav-link" href="${pageContext.request.contextPath}/bezeroa/profila">
									Profila
								</a>
							</li>
							<li class="nav-item">
								<a class="nav-link" href="${pageContext.request.contextPath}/ibilgailua">
									Nire Ibilgailuak
								</a>
							</li>	
							<!--  						
							<li class="nav-item">
								<a class="nav-link" href="${pageContext.request.contextPath}/bezeroa/erreserbak">
									Nire Erreserbak
								</a>
							</li>
							-->
						</ul>

						<ul class="navbar-nav ms-auto">
							<li class="nav-item">
								<a class="nav-link" href="${pageContext.request.contextPath}/logout">
									Irten
								</a>
							</li>
						</ul>
					</c:when>
					<c:when test="${sessionScope.current_user_type eq 'langilea'}">
						<ul class="navbar-nav mb-lg-0">
							<li class="nav-item">
								<a class="nav-link" href="${pageContext.request.contextPath}/langilea/profila">
									Profila
								</a>
							</li>
							   <li class="nav-item">
								<a class="nav-link" href="${pageContext.request.contextPath}/erreserba/zerrendak/kabinaka">
									Erreserbak
								</a>
							</li>
						</ul>

						<ul class="navbar-nav ms-auto">
							<li class="nav-item">
								<a class="nav-link" href="${pageContext.request.contextPath}/logout">
									Irten
								</a>
							</li>
						</ul>
					</c:when>

				</c:choose>

			</div>
		</div>
	</nav>