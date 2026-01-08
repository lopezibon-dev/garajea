# DIY Garajea: Tailer Mekanikoaren Kudeaketa Sistema

## Lehenengo fasea

### 1. Ikuspegi Orokorra

DIY Garajearen proiektua, tailer mekaniko baterako kudeaketa sistema informatikoa da. Proiektuak bi alde nagusi ditu: Web aplikazioa eta Desktop aplikazioa.

### 2. Arkitektura Eredua: MVC Patroia (Model-View-Controller)

* Eredua (Model) geruza: MVC patroiaren Eredua geruza bi modulutan banatuko da: garajea-model (entitateak eta iraunkortasuna) eta garajea-core (Negozio Logika, Zerbitzuak)

* Ikuspegia (View) eta Kontrolatzailea (Controller) geruzak: Modulu desberdinetan (Web eta Desktop) inplementatuko dira, eta modulu bakoitzak bere kontrolatzaileak eta bistak ditu. garajea-core moduluarekiko menepekotasuna dute.

* Proiektua egitura antolatu batean eraikitzeko Maven erabiltzen da. Arkitektura moduluanitza da:

```mermaid
graph TB
garajea["garajea (gurasoa: POM)"]
garajea-model["garajea-model"]
garajea-core["garajea-core"]
garajea-desktop["garajea-desktop"]
garajea-web["garajea-web"]

garajea --> garajea-model
garajea --> garajea-core
garajea --> garajea-desktop
garajea --> garajea-web
```

Garajea modulua, proiektuaren modulu nagusia edo gurasoa da (edukiontzi bat), eta proiektu moduluanitzaren egitura orokorra definitzen du. Berez, karpeta batez eta pom.xml fitxategi batez osatzen da.

* Dena dela, moduluetan egituratutako kodeak zenbait menpekotasun ditu:

```mermaid
graph BT
garajea-model["garajea-model"]
garajea-core["garajea-core"]
garajea-desktop["garajea-desktop"]
garajea-web["garajea-web"]

garajea-core --> garajea-model
garajea-desktop --> garajea-core
garajea-web --> garajea-core
```

* Moduluak:
  * garajea-model: Entitateen definizioaz eta datuen Iraunkortasunaz (DAO patroia erabiliz) arduratzen den modulua da. Helburu nagusia datuen iraunkortasuna negozio-logikatik banatzea da.
    * Entitateak: sistema informatikoaren negozio-objektuak definitzen dituzte.
    * DAO (Data Access Object): Datuen atzipen-logika (CRUD eragiketak) enkapsulatzen du. Datuen iturburuari (MySQL kasu honetan) buruzko xehetasun guztiak (konexioa, datuen atzipena, etab.) isolatzen dira. Horrela, ez da datu-base sistemaren berariazko elementurik (adibidez, ResultSet delakoak edo SQL kontsultak) erakusten.

  * garajea-core: Negozioaren logika biltzen du, eta garajea-model moduluan definitutako DAOak erabiliko ditu datuak lortu, manipulatu eta gordetzeko. Beraz, garajea-model moduluarekiko menpekotasuna du. Orokorrean, banaketa honek mantentze lanak erraztuko ditu: adibidez datu-base sistema aldatu behar bada, garajea-model modulua (zehazki DAO-en inplementazioa) soilik aldatu behar da.

  * Web Modulua: Bezeroentzako interfazea, garaje-core moduluaren menpe dago. Hautazkoa: langileek ere erabili ahal izatea heuren egitekoak burutzeko.

  * Desktop Modulua: Langileentzako interfazea, garaje-core moduluaren menpe dago.

### 3. Erabilitako Teknologiak

* Programazio-lengoaia: Java  
* Proiektuaren azpiegituraren kudeatzailea (dependentziak, konpilazioa, enpaketatzea): Maven  
* Datu-base Sistema: MySQL  
* IDE: VS Code  
* Web Teknologiak:  
  * Responsive Diseinua (diseinu moldakorra)  
  * Servlets  
  * Filtroak  
  * JSP  
  * JSTL  
  * Expression Language

### 4. Garajea-model moduluko entitateen espezifikazioa

Entitateen eta erlazioen diagrama hemen dago:
[ERD Diagrama Osoa](./erd-mermaid.md)

#### 4.1 Entitateak eta harremanak

* Bezeroa - Ibilgailua harremana (1:N)
  * Bezero bakoitzak ibilgailu bat edo gehiago eduki ditzake.
  * IBILGAILUA taularen bezeroa_id eremua FOREIGN KEY da, BEZEROA taulatik kopiatua. Bezero bati lotuta dago ibilgailu bakoitza.

* Bezeroa - Erreserba harremana (1:N)
  * Bezero bakoitzak erreserba bat edo gehiago sortu ditzake.
  * ERRESERBA taulako bezeroa_id FOREIGN KEY da, eta ezin du NULL balioa izan (NOT NULL).

* Ibilgailua - Erreserba harremana (1:N)
  * Erreserba bakoitza bezeroaren ibilgailu bati dagokio.
  * Denboran zehar ibilgailu batek hainbat erreserbatan parte har dezake.
  * ERRESERBA taulako ibilgailua_id FOREIGN KEY da, eta ezin du NULL balioa izan (NOT NULL).

* Kabina - Erreserba harremana (1:N)
  * Kabina batek denboran zehar hainbat erreserba izan ditzake.
  * ERRESERBA taulako kabina_id FOREIGN KEY da, eta ezin du NULL balioa izan (NOT NULL).

* Langilea - Erreserba harremana (1:N)*
  * Erreserbak teknikari baten laguntza eska dezake, baina hautazkoa da.
  * Horregatik, ERRESERBA taulan langilea_id FOREIGN KEY da, NULL balioa izan dezakeena. langilea_id balioa NULL bada, erreserbari ez zaio teknikaririk esleitu.

* Erreserba - Faktura harremana (1:1)*
  * 1:1 erlazio partziala: erreserba batek faktura bat izan dezake (baina ez da derrigorrezkoa)
  * Burututako erreserba batek faktura bakarra sortzen du.
  * Beste edozein egoeratan, erreserbak ez du fakturarik sortuko.
  * Faktura bat beti dago erlazionatuta erreserba batekin.

* Kabina - Goragailua harremana (1:1)
  * Kabina bakoitzak goragailu bakarra du.
  * GORAGAILUA taulako kabina_id atributua FOREIGN KEY da, KABINA taularen kabina_id eremuari dagokiona, eta UNIQUE murrizpena du, kabina bakoitzean goragailu bakarra egon dadin.

* Kabina - Makina harremana (1:N)
  * Kabina bakoitzak makina bat edo gehiago eduki ditzake.
  * MAKINA taularen kabina_id eremua FOREIGN KEY da, eta kabina bati lotuta dago makina bakoitza.

* Erreserba - Materiala harremana (N:M)
  * Bezeroak erreserba batean hainbat material aukeratu dezake, eta materialak hainbat erreserbatan erabiltzen dira.
  * Horretarako, tarteko taula bat definitzen da: ERRESERBA_MATERIALA
  * erreserba_id: FK, ERRESERBA taulatik kopiatua
  * materiala_id: FK, MATERIALA taulatik kopiatua
  * kopurua: erreserban eskatutako material baten kopurua
  * (erreserba_id, materiala_id) PRIMARY KEY da.

#### 4.2 Datu-integritate eta negozio-arauak

* Bezeroak bere ibilgailuak kudeatu ditzake.
* Erreserben baliozkotasuna (denbora-tarteen gainjartzea ekidin): Erreserba bat sortzean, hasiera eta amaiera eremuak balioztatu behar dira, kabina_id berberarekin ez baitaiteke beste erreserbarik egon denbora-tarte komunean. Hau da, bi erreserben denbora-tarteek ezin dute bat egin, zati batean ere. Beraz, erreserba guztiak baliozkoak direla bermatu behar da.
* Erreserba batek hainbat egoera izan ditzake bere bizitzan zehar: zain, martxan, burutua, ezeztatua.
* Aurretik azaldu bezala, burututako erreserbak faktura bat sortu du, eta fakturak erreserbaren erreserba_id jasoko du.
* Faktura sortzeko logikak sinplea izan behar du, eta barne hartu behar ditu kabinaren kostua, erabilera-denbora, langileen laguntza (hala badagokio) eta garajeko kontsumitu materialaren kostua (ERRESERBA_MATERIALA taulatik datorrena).

### 5. Dokumentazio osagarria

* [MVC Web](arkitektura/mvc-web.md)
* [MVC Desktop](arkitektura/mvc-desktop.md)
* [Bootstrap](arkitektura/bootstrap.md)
* [Zerbitzu geruza (Service Layer)](arkitektura/zerbitzu-geruza.md)
* [Erroreak eta balidazioak - Web](arkitektura/erroreak-eta-balidazioak-web.md)
* [Erroreak eta balidazioak - Desktop](arkitektura/erroreak-eta-balidazioak-desktop.md)

## Etorkizunerako garapen-aukerak

* Python erabiltzea, adibidez datu-base bateko taula bat esportatzeko edo inportatzeko.  
* I18n (Internazionalizazioa) kontuan hartuta garatzea. Horrela, gaztelania eta ingelesa gehituz erabiltzaileak erabili ditzakeen hizkuntzak bezala.
