# README - Arkitektura

## 1. Sarrera

Karpeta honek Garajea proiektuaren arkitektura-dokumentazioa biltzen du.
Helburua ez da inplementazio xehetasun guztiak azaltzea, baizik eta egitura kontzeptuala, geruzen arteko muga formalak eta diseinu-erabaki nagusiak dokumentatzea.

Dokumentazio honek arkitekturaren koherentzia bermatzeko balio du, eta proiektuaren bilakaeran erreferentzia-puntu egonkor gisa erabiltzen da.
Hemen jasotzen diren edukiek honakoak barne hartzen dituzte: egitura orokorra, MVC eredua (web eta desktop), zerbitzu-geruza, transakzio-kudeaketa eta kasu-erabileren diseinu analisia.

Dokumentazio hau proiektuaren parte integrala da, ez eranskin osagarri bat.

## 2. Proiektuaren Egituraren Mapa

Atal honek proiektuaren modulu bakoitzeko egitura nagusia azaltzen du, karpeta bakoitzaren ardura eta edukia labur deskribatuz.

---

### garajea-web modulua

`garajea-web\src\main\java\com\unieus\garajea\web`
Web geruzako sarrera-puntua. Controller-ak eta web logika nagusia.

`garajea-web\src\main\java\com\unieus\garajea\web\balidazioa`
Sarrerako datuen balidazio teknikoa (formatua, koherentzia minimoa).

`garajea-web\src\main\java\com\unieus\garajea\web\dto`
Web geruzarako DTO espezifikoak (view-aren eta controller-aren arteko transferentzia).

`garajea-web\src\main\java\com\unieus\garajea\web\el`
Expression Language eta view-arekin lotutako osagai lagungarriak.

`garajea-web\src\main\java\com\unieus\garajea\web\exception`
Web geruzako salbuespen espezifikoak.

`garajea-web\src\main\java\com\unieus\garajea\web\filter`
Servlet filter-ak (autentikazioa, kontrol teknikoak, etab.).

`garajea-web\src\main\java\com\unieus\garajea\web\init`
Web aplikazioaren hasierako konfigurazioa eta bootstrap logika.

`garajea-web\src\main\resources`
Konfigurazio fitxategiak eta kode ez diren baliabideak.

`garajea-web\src\main\webapp`
Web aplikazioaren baliabide publikoak (JSP, CSS, JS).

`garajea-web\src\main\webapp\WEB-INF`
Konfigurazio eta view babestuak (kanpotik zuzenean eskuragarri ez direnak).

`garajea-web\src\main\webapp\WEB-INF\tld`
Tag Library Definition fitxategiak (custom tag-ak).

`garajea-web\src\main\webapp\WEB-INF\views`
JSP view-ak (MVC-ren bistaren geruza).

---

### garajea-desktop modulua

`garajea-desktop\src\main\java\com\unieus\garajea\desktop`
Desktop aplikazioaren sarrera eta kontrol nagusia.

`garajea-desktop\src\main\java\com\unieus\garajea\desktop\bootstrap`
Aplikazioaren hasieratzea eta konfigurazio teknikoa.

`garajea-desktop\src\main\java\com\unieus\garajea\desktop\ui`
Erabiltzaile-interfazeko osagaiak (leihoak, panelak, ekintzak).

`garajea-desktop\src\main\resources`
Konfigurazio eta baliabide orokorrak.

`garajea-desktop\src\main\resources\images`
UI-n erabiltzen diren irudi eta baliabide grafikoak.

---

### garajea-core modulua

`garajea-core\src\main\java\com\unieus\garajea\core`
Negozio-logikaren sarrera eta zerbitzuen egitura nagusia.

`garajea-core\src\main\java\com\unieus\garajea\core\config`
Core mailako konfigurazio abstrakzioak.

`garajea-core\src\main\java\com\unieus\garajea\core\config\impl`
Konfigurazio inplementazio konkretuak.

`garajea-core\src\main\java\com\unieus\garajea\core\exception`
Negozio-mailako salbuespenak.

`garajea-core\src\main\java\com\unieus\garajea\core\presentation`
Core mailan sortzen diren aurkezpen-eredu abstraktuak. Ez dago teknologia zehatzik (ez Swing, ez Web); datuen egituraketa logikoa bakarrik.

`garajea-core\src\main\java\com\unieus\garajea\core\presentation\agenda`
Erreserba-zerrendetatik abiatuta agenda-egitura abstraktuak sortzen dituzten osagaiak (View-tik independienteak).

`garajea-core\src\main\java\com\unieus\garajea\core\services`
Use case edo kasu-erabileren inplementazioa (zerbitzuak).

`garajea-core\src\main\java\com\unieus\garajea\core\util`
Laguntza utilitarioak (ez negozio-arauak).

`garajea-core\src\main\resources`
Core moduluko konfigurazio eta baliabide osagarriak.

---

### garajea-model modulua

`garajea-model\src\main\java\com\unieus\garajea\model`
Persistenzia eta datu-ereduaren oinarrizko egitura.

`garajea-model\src\main\java\com\unieus\garajea\model\infraestructure`
Azpiegitura teknikoa (Datu-basera konexioa egiteko parametroak, etab.).

`garajea-model\src\main\java\com\unieus\garajea\model\util`
Persistenziarako utilitate teknikoak.

`garajea-model\src\main\java\com\unieus\garajea\model\entities`
Datu-ereduko entitateak (sinpleak, negozio-logikarik gabe).

`garajea-model\src\main\java\com\unieus\garajea\model\dto`
Persistenzia mailako DTO espezifikoak.

`garajea-model\src\main\java\com\unieus\garajea\model\dao`
DAO interfaz eta definizioak.

`garajea-model\src\main\java\com\unieus\garajea\model\dao\impl`
DAO inplementazio konkretuak (JDBC + SQL kapsulatuta).

`garajea-model\src\main\resources`
Persistenziari lotutako konfigurazio edo baliabide teknikoak.

---

## 3. Dokumentazio Indizea

### [bootstrap.md](bootstrap.md)

Aplikazioen hasieratze-fluxua azaltzen du (web eta desktop).
Bootstrap mekanismoa, konfigurazio-karga eta testuinguruaren sorrera deskribatzen dira.
Azpiegitura teknikoaren aktibazioa eta abiarazte-fasearen ardura banaketa azaltzen du.

---

### [datubasea-taulen-esportazioa-kasua.md](datubasea-taulen-esportazioa-kasua.md)

Datu-baseko taulen esportazio kasu-erabileraren analisi arkitektonikoa.
Zerbitzu-geruzaren erabakiak, transakzionaltasuna eta fluxu logikoa dokumentatzen dira.
Geruzen arteko erantzukizun-banaketa zehazten da.

---

### [datubasea-taulen-inportazioa-kasua.md](datubasea-taulen-inportazioa-kasua.md)

Taulen inportazio prozesuaren diseinu kontzeptuala.
Atomicitatea, errore-kudeaketa eta zerbitzuaren erabakiak azaltzen dira.
Azpiegitura eta negozio-erabakiak bereizten dira.

---

### [eguneko-erreserbak-kabinaka-desktop-kasua.md](eguneko-erreserbak-kabinaka-desktop-kasua.md)

Desktop aplikazioko kasu-erabilera zehatz baten diseinua.
Agenda-egitura sortzea eta aurkezpen abstraktuaren erabilera azaltzen dira.
UI eta core arteko dependentzia kontrolatua dokumentatzen da.

---

### [erabilera-kasuak.md](erabilera-kasuak.md)

Proiektuaren kasu-erabilera nagusien zerrenda eta definizio orokorra.
Sistema mailako ikuspegia ematen du, inplementazio zehatzik gabe.
Diseinuaren oinarri kontzeptuala finkatzen du.

---

### [erroreak-eta-balidazioak-desktop.md](erroreak-eta-balidazioak-desktop.md)

Desktop geruzan erroreen tratamendua eta balidazio-estrategia.
Controller, zerbitzu eta DAO mailako erantzukizun-banaketa azaltzen da.
Fluxu ez-transakzional eta transakzionalen portaera dokumentatzen da.

---

### [erroreak-eta-balidazioak-web.md](erroreak-eta-balidazioak-web.md)

Web aplikazioan balidazio eta erroreen kudeaketa arkitektonikoa.
Filter, controller eta zerbitzuen arteko rolak definitzen dira.
HTTP testuinguruan koherentzia eta trazabilitatea bermatzeko irizpideak jasotzen dira.

---

### [mvc-desktop.md](mvc-desktop.md)

Desktop aplikazioaren MVC egitura zehazten du.
Controller, UI eta zerbitzu-geruzaren arteko elkarreragina azaltzen da.
Dependentzien norabidea eta testuinguruaren erabilera formalizatzen dira.

---

### [mvc-web.md](mvc-web.md)

Web aplikazioaren MVC arkitektura.
Servlet/controller eredua, view-ak eta zerbitzuen integrazioa azaltzen dira.
Request bakoitzeko ServiceContext eredua dokumentatzen da.

---

### [zerbitzu-geruza.md](zerbitzu-geruza.md)

Zerbitzu-geruzaren diseinu printzipio eta arauak.
Transakzio-erabakiak, ServiceContext erabilera eta atomicitate-irizpideak jasotzen ditu.
Negozio-logikaren kokapena eta geruzen arteko muga formala definitzen dira.

---

### [desktop-ui-egitura.md](desktop-ui-egitura.md)

Dokumentu honek Desktop aplikazioko pantaila bakoitzaren Swing osagaien egitura hierarkikoa jasotzen du.
Helburua ez da diseinu grafikoa edo layout zehatza azaltzea,
baizik eta ikuspegi arkitektonikotik panel bakoitzaren konposizio estrukturala dokumentatzea.

### README.md

Karpeta honen sarrera-dokumentua.
Arkitekturaren mapa, egitura eta dokumentazio indizea jasotzen ditu.
Erreferentzia-puntu nagusia da arkitektura ulertzeko.

---

## 4. Diseinu-printzipioen erreferentzia

Karpeta honetako dokumentazio guztia
[00-oinarri-arkitektonikoa.md](00-oinarri-arkitektonikoa.md) dokumentuan ezarritako printzipio eta muga formaletan oinarritzen da.

Dokumentu hori da proiektuaren marko arkitektoniko normatiboa.
Hemen jasotako kasu, azalpen eta diseinu-erabaki guztiak haren barruan ulertu behar dira.

Arkitektura mailako aldaketek dokumentu horrekin koherente izan behar dute.

---
