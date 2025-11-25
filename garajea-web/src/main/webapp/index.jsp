<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- header.jsp txertatu (web-orriko titulua parametro gisa pasatuz) --%>
<jsp:include page="WEB-INF/views/includes/header.jsp">
    <jsp:param name="title" value="Hasiera"/>
</jsp:include>

        <h1>DIY Garajea</h1>
        <section>
            <h2>Erabili gure kabinak zure kabuz</h2>
            <p>
                <strong>Baztertzen dugu auto klasikoko garajeen eredua</strong>: hemen, <strong>zuk</strong> konpon dezakezu zure ibilgailua nahi duzun eran, mekaniko baten <strong>laguntzarik gabe</strong>—baina nahi izanez gero, langile bat ere kontrata dezakezu.
            </p>
        </section>
        <section>
            <h2>Zure materialak edo gureak</h2>
            <p>
                <strong>Malgutasuna</strong> da gure ezaugarri nagusietako bat: 
                zure piezak edo olioa ekarri nahi badituzu, <strong>ongi etorriak</strong> dira. Bestela, behar duzun guztia garajean bertan topatuko duzu eskuragarri.
            </p>
        </section>

        <section>
            <h2>Nolakoak dira gure instalazioak?</h2>
            <h3>Kabina moderno eta hornituak</h3>
            <p>
                Kabina bakoitzean <strong>erreminta ugari</strong> eta <strong>diagnosi elektronikorako makina</strong> bat dituzu; horri esker, zure ibilgailuaren arazoak identifikatu eta zuzendu ahal izango dituzu.
            </p>
            <h3>Erreserbak eta laguntza</h3>
            <p>
                Egin <strong>kabina erreserba</strong> eta, behar izanez gero, <strong>mekaniko baten laguntza</strong> ere bai. Zure garajearen esperientzia norberak erabakitzen du!
            </p>
        </section>

 <%-- footer.jsp txertatu --%>
<jsp:include page="WEB-INF/views/includes/footer.jsp"/>
