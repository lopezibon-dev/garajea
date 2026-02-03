# MVC eredua DIY Garajea proiektuan

## 1. Helburua

Dokumentu honen helburua da DIY Garajea proiektuan
Model-View-Controller (MVC) patroia nola aplikatzen den azaltzea.

Arkitektura honek arduren banaketa argia ezartzen du,
kodearen mantentzea eta ulermena erraztuz,
hezkuntza-testuinguru batean sinpletasuna lehenetsiz.

## 2. MVC patroia: ikuspegi orokorra

MVC patroiak hiru geruza nagusi bereizten ditu:

- Eredua (Model)
- Kontrolatzailea (Controller)
- Bista (View)

Geruza bakoitzak ardura zehatzak ditu eta
ez du beste geruzen  barne-inplementazioa ezagutu behar.

## 3. Eredua (Model)

### 3.1 Kokapena

Ereduaren logika bi modulutan banatzen da:

- **garajea-model**
- **garajea-core**

### 3.2 **garajea-model** modulua

- Entitateak (Bezeroa, Ibilgailua, Erreserba, ...)
- DAO interfazeak eta inplementazioak (JDBC)
- Datu-basearekiko elkarrekintza
- DTO, irakurketa "aberatsak" egiteko

#### 3.2.1 DAOen kontratuak eta inbarianteak

- Erreserbak itzultzen dituzten DAO kontsultak
  kronologikoki ordenatuta daude beti (`ORDER BY hasiera`).
- Ordenazio hau domeinuko inbariante bat da:
  erreserba-zerrendek zentzua dute soilik denbora-ordenan.
- Zerbitzu-geruzak ordenazioa emanda datorrela suposatu dezake,
  logika errepikapena saihestuz.

### 3.3 **garajea-core** modulua

- Negozio-zerbitzuak (Service Layer)
- Negozio-arauen balidazioa
- ServiceContext eta ServiceContextFactory

Modulu honetan gehiago sakontzeko:
[Zerbitzu geruza (Service Layer)](arkitektura/zerbitzu-geruza.md)

### 3.4 Printzipioak

- Ez dago HTTP, JSP erreferentziarik
- Ez dago bistaren araberako logikarik
- Negozio-arauak zerbitzuetan zentralizatzen dira

## 4. Kontrolatzailea (Web Controller)

### 4.1 Teknologia

- Jakarta Servlet API
- Servlet espezifikoak (BezeroaServlet, LangileaServlet, ...)

### 4.2 Ardura nagusiak

- Web esparruan HTTP eskaerak prozesatu
- Sarrera-datuen balidazioa (null, hutsik, formatua)
- `ServiceContext` irekitzea
- Negozio-zerbitzuak orkestratzea
- Zein bista erakutsi erabakitzea

### 4.3 Ez dagoena

- Ez dago negozio-logikarik
- Ez dago SQL kontsultarik
- Ez dago datu-base konexiorik zuzenean kudeatzen

## 5. Bista (View)

### 5.1 Teknologia

- JSP
- JSTL
- Expression Language (EL)

### 5.2 Ardura nagusiak

- Datuen bistaratzea
- Balidazio-erroreen erakusketa
- Arrakasta-mezuen erakusketa
- HTML egitura eta estiloa

### 5.3 Murrizketak

- Ez dago zerbitzu-deirik
- Ez dago negozio-arauik
- Ez dago DAO edo `ServiceContext` erabilerarik

### 5.4 Data eta orduen formateatzea (java.time)

JSP + EL ingurunean, Expression Language-k ez du zuzenean `java.time` APIko metodoen deia onartzen (adib. `LocalDateTime.format()`).

Hori dela eta, DIY Garajea proiektuan **EL funtzio pertsonalizatuak** erabiltzen dira data eta orduen formateatzea egiteko.

#### Irizpideak

- Ez da formateatzerik egiten backend-ean
- Ez da Java kodea erabiltzen JSP fitxategietan

#### Inplementazioa

- Utilitate-klasea:
  - `com.unieus.garajea.web.el.DateTimeFunctions`
- TLD fitxategia:
  - `/WEB-INF/tld/datetime-functions.tld`

#### Erabilera adibidea

```jsp
<%@ taglib uri="http://garajea.local/functions/datetime" prefix="dt" %>

${dt:format(erreserbaInfo.hasiera, "HH:mm")}
${dt:format(erreserbaInfo.hasiera, "yyyy-MM-dd HH:mm:ss")}
```

Sistema honek LocalDate eta LocalDateTime motak onartzen ditu, java.time API-a  erabiliz.

## 6. Fluxu orokorra

HTTP eskaera baten prozesamenduaren fluxu orokorra honakoa da:

1. Nabigatzailea → Controller
2. Controller → ServiceContextFactory
3. ServiceContext → Zerbitzuak
4. Zerbitzuak → DAO
5. DAO → Datu-basea
6. Emaitza → Controller
7. Controller → Bista (JSP)

## 7. Laburpena

DIY Garajea proiektuan MVC patroia
arduren banaketa argia eta zorrotza bermatzeko erabiltzen da,
alde batetik kodearen argitasuna eta mantengarritasuna,
eta bestetik hezkuntza arloan sinpletasuna lehenetsiz.
