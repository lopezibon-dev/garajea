# Erroreak eta Balidazioak – DIY Garajea

## 1. Helburua

Dokumentu honen helburua da DIY Garajea proiektuan
balidazioen eta erroreen kudeaketaren estrategia azaltzea.

Helburu nagusiak hauek dira:

- Arduren banaketa argia mantentzea
- Negozio-erroreak eta errore teknikoak bereiztea
- Erabiltzaileari informazio segurua soilik erakustea
- Kodearen sinpletasuna eta koherentzia bermatzea

## 2. Printzipio Orokorrak

DIY Garajea proiektuan honako printzipio hauek aplikatzen dira:

- Balidazioak geruzen arabera banatzen dira
- Negozio-erroreak ez dira errore teknikoekin nahasten
- Errore teknikoak ez dira erabiltzaileari erakusten
- try-with-resources eredua sistematikoki erabiltzen da
- Salbuespenak dira erroreak adierazteko mekanismo nagusia

## 3. Balidazioa Kontrolatzaileetan (Controller)

### 3.1 Zer balidatzen da

Kontrolatzaileek **sarrera-datuen balidazioa** egiten dute.
Hau da, adib. HTTP eskaeratik datozen datuak.

Balidazio mota nagusiak:

- null edo hutsik dagoen balioa
- datu mota okerra
- formatu okerra (emaila, zenbakiak, etab.)
- balio-barrutiak  (adib. luzera minimoa)

Balidazio hauek **zerbitzuak deitu aurretik** egiten dira,
alferrikako baliabide-kontsumoa saihesteko.

### 3.2 Erabilitako salbuespena

Balidazio hauek huts egiten dutenean,
honako salbuespena erabiltzen da:

- InputBalidazioSalbuespena

Salbuespen honek:

- errore-mezu bakarra, edo
- errore-zerrenda bat

izan dezake.

Salbuespen hau **kontrolatzailean bertan harrapatzen da**
eta normalean erabiltzailea jatorrizko bista berdinera itzultzen da
(mezu egokiarekin).

### 3.3 Adibide kontzeptuala

Controller
├── HTTP parametroak hartu
├── Balidazioa
│ └── InputBalidazioSalbuespena
└── ServiceContext ireki

## 4. Balidazioa Zerbitzu Geruzan (Service Layer)

### 4.1 Zer balidatzen da

Zerbitzu geruzan **negozio-logika** balidatzen da:

- negozio-arauak
- entitateen arteko koherentzia
- murrizketak (adib. email bikoiztua)

### 4.2 Erabilitako salbuespena

Negozio-arauak betetzen ez direnean,
honako salbuespena erabiltzen da:

- ZerbitzuSalbuespena

Salbuespen honek:

- errore-mezu bakarra, edo
- errore-zerrenda

izan dezake.

Zerbitzu geruzak **ez du erabakitzen nola erakutsi errorea**;
salbuespena gorantz propagatzen da.

## 5. Errore Teknikoak

### 5.1 Zer dira errore teknikoak

Errore teknikoak aplikazioaren funtzionamenduarekin lotutakoak dira,
ez negozioarekin.

Adibideak:

- SQLException
- NullPointerException
- baliabideak ixteko erroreak
- JSP sintaxi edo exekuzio erroreak

Errore hauek **ez dira berreskuragarriak erabiltzaile mailan**.

### 5.2 Kudeaketa-printzipioa

- Ez dira kontrolatzaileetan tratatzen
- Ez dira bistetan kudeatzen
- Gorantz propagatzen dira
- Web moduluan zentralizatuta harrapatzen dira (SalbuespenFilter)

## 6. SalbuespenFilter bidezko errore-kudeaketa zentralizatua

### 6.1 Testuingurua

Aplikazioaren sendotasuna (robustness) bermatzeko eta erabiltzaileari errore tekniko gordinak ez erakusteko, salbuespenen kudeaketa zentralizatua ezarri da web moduluan.

## 6.2 Soluzio Teknikoa (Web esparruan): `SalbuespenFilter`

Jakarta Servlet APIaren **Filter** mekanismoa erabiltzen da.

Filtro honek aukera ematen du:

- kontrolatzaileetan  (`Servlets`) gertatzen diren erroreak harrapatzeko
- bistetan (`JSPs`) gertatzen diren erroreak harrapatzeko
- fluxua eten gabe eta baliabide estatikoak (JS, CSS, irudiak) oztopatu gabe.

### 6.3 Funtzionamendua

- **Mekanismoa:** Filtroak `try-catch` bloke baten bidez inguratzen du eskaeraren prozesamendua (`chain.doFilter()`).
- Edozein `Exception` harrapatzen da
- Errorea logean gordetzen da
- Erabiltzaileari mezu generikoa erakusten zaio

### 6.4 Logging sistema

- **Logback** erabiltzen da
- Log fitxategia: `C:/logs/garajea_web.log`
- Traza osoa (stack trace) gordetzen da
- Erabiltzaileari ez zaio inoiz erakusten

### 6.5 Erabiltzaile interfazea

Errore teknikoa gertatzen denean:

- Erabiltzaileari mezu segurua erakusten zaio
- Xehetasun teknikoak ezkutatzen dira
- Jatorrizko web-orrira edota `errorea.jsp` bistara bideratzen da

### 6.6 Iragazitako bideak (URL Patterns)

Filtroa endpoint (bide) zehatzetan bakarrik exekutatzen da, errendimendua optimizatzeko:

- `/bezeroa/*`
- `/langilea/*`
- `/erreserbak/*`

### 6.7 Log Fitxategien Errotazioa

Mantentze-lanak errazteko, log sistemak errotazio automatikoa du:

1. Egunero fitxategi berri bat sortzen da
2. Fitxategien izen-eredua: adib. `garajea_web.YYYY-MM-DD.log`.
3. Gehienez 30 eguneko historia gordetzen da.

## 7. Fluxu orokorra (laburpena)

HTTP Request
└── Controller
├── Input balidazioa
├── ServiceContext.open()
│ └── Zerbitzuak
│ └── DAO
└── View (JSP)
↑
└── SalbuespenFilter (errore teknikoak)

## 8. Errore moten laburpen-taula (Web)

| Errore mota                          | Salbuespena                 | Bista                        | UI jokabidea / Iradokizuna                            |
| ------------------------------------ | --------------------------- | ---------------------------- | ----------------------------------------------------- |
| Eremu hutsa                          | `InputBalidazioSalbuespena` | Formulario bera              | Errorea formulario gainean bistaratzea                |
| Email bikoiztua                      | `ZerbitzuSalbuespena`       | Formulario bera              | Jakinarazi helbide elektronikoa lehendik dagoela      |
| Kredentzial txarrak                  | `ZerbitzuSalbuespena`       | `login.jsp`                  | Errore-mezu generikoa erakutsi                        |
| JSP akatsa                           | `Exception`                 | `errorea.jsp`                | Zerbitzariaren barne-errorea                          |
| `SQLException`                       | `Exception`                 | `errorea.jsp`                | Datu-basearen errorea                                 |
| ID gaizki eratua                     | `InputBalidazioSalbuespena` | `errorea.jsp`                | Errore kritikoa: ezin da testuingurua berreskuratu    |
| Parametro baliogabea (adib. filtroa) | `InputBalidazioSalbuespena` | Bista bera                   | Iragazkia ez aplikatu, emaitza guztiak erakutsi       |
| Data okerra                          | `InputBalidazioSalbuespena` | Bista bera                   | Lehenetsitako balioak erabili (adib. uneko hilabetea) |
| Parametro hutsa                      | `InputBalidazioSalbuespena` | `errorea.jsp` edo bista bera | Errorearen kritikotasunaren arabera                   |

## 9. Ondorioa

Balidazioen eta erroreen kudeaketa DIY Garajea proiektuan
sinpletasuna, segurtasuna eta mantengarritasuna lehenesten dituen
eredu argi eta hezkuntza-arlokoan oinarritzen da.
