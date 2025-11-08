<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="eu">
<head>
    <meta charset="UTF-8">
    <title>DIY Garajea</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="DIY Garajea: zure ibilgailua konpondu, mekanikoaren beharrik gabe edo haren laguntzarekin. Zure materialak ekarri edo bertan erosi!">
    <link rel="stylesheet" href="/css/styles.css">
</head>
<body>
    <header>
        <div class="logo-login-container">
            <div class="logo">
                <img src="img/garajea-740-300.png" alt="DIY Garajea">
            </div>
            <form class="login-form" method="post" action="login">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
                <label for="password">Pasahitza:</label>
                <input type="password" id="password" name="password" required>
                <button type="submit">Sartu</button>
            </form>
        </div>
    </header>

    <nav>
        <ul class="main-menu">
            <li><a href="#">Hasiera</a></li>
            <li><a href="#">Nola dabil?</a></li>
            <li><a href="#">Kabinen erreserba</a></li>
            <li><a href="#">Zure materiala</a></li>
            <li><a href="#">Kontaktua</a></li>
        </ul>
    </nav>

    <main>
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
    </main>
    <footer>
        <small>&copy; 2025 DIY Garajea – Zure ibilgailua, zure eskuekin</small>
    </footer>
</body>
</html>
