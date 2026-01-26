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
Hau da, adib. HTTP eskaeratik (`HttpServletRequest`) datozen datuak.
Sarreraren interpretazioa erabilera-kasu bakoitzaren arabera egituratzen da.

Balidazio honen helburua da:

- Erabiltzaileak bidalitako datuen formatua eta oinarrizko koherentzia bermatzea
- Negozio-logikara datu baliogabeak ez pasatzea
- Akats sinpleak ahalik eta azkarren detektatzea

Kontrolatzaile-mailan egiten diren balidazio mota nagusiak hauek dira:

- parametro hutsa edo hutsik dagoena
- datu mota okerra (adib. zenbaki bat espero denean testua jasotzea)
- formatu okerra (emaila, data, ordua, etab.)
- balio-barrutiak edo luzera-mugak

Balidazio hauek **zerbitzuak deitu aurretik** egiten dira,
`ServiceContext` ireki baino lehen, alferrikako baliabide-kontsumoa saihesteko.

Kontuan izan balidazio honek **ez duela negozio-logika** ordezkatzen:
erreferentziazko osotasuna edo entitateen arteko erlazioak zerbitzu-geruzan balidatzen dira.

---

### 3.2 Sarrera-kontratuak eta DTOen erabilera

Web kontrolatzaileetan, endpoint (adib. `/ibilgailua/sortu`, `ibilgailua/ezabatu`, ...) bakoitzak bere **sarrera-kontratu esplizitua** du.

`HttpServletRequest` objektua **behin bakarrik** interpretatzen da, `doGet` edo `doPost` metodoaren hasieran, edozein negozio-logika exekutatu aurretik.
Interpretazio horrek honako urratsak barne hartzen ditu:

- Request-etik beharrezko parametroak irakurtzea.
- Sarrera-datuen balidazio sintaktikoa egitea.
- Erabilera-kasuaren sarrera-kontratua ordezkatzen duen DTO bat sortzea.

Kontrolatzaileko `handleX(...)` metodoek **ez dute zuzenean request-a
irakurtzen**, baizik eta aurretik interpretatutako eta balidatutako
datuak jasotzen dituzte (DTO bidez).

---

#### DTOak eta sarrera-kontratuak

Sarrera-kontratu bakoitzerako **DTO espezifiko bat definitzen da**,
jasotako eremu kopurua edozein dela ere.

Formulario-DTO batek honako datu motak izan ditzake:

- Erabiltzaileak HTML formulariotik bidalitako datuak.
- Kontrolatzaileak berak injektatutako datuak, baldin eta erabilera-kasuaren
  kontratuaren parte badira.

Adibidez, erabiltzailearen identifikatzailea (`bezeroaId`) saioaren edo
segurtasun-testuinguruaren bidez lor daiteke, eta DTOaren parte izan daiteke,
HTML formulariotik zuzenean etorri ez arren.

Horrela, honako helburuak lortzen dira:

- Request-aren interpretazioa eta negozio-logikaren exekuzioa bereiztea.
- Handler metodo sinpleak eta aurreikusteko errazak izatea.
- Etorkizunean Spring MVC edo antzeko framework batera migratzea erraztea.

### 3.3 Sarrera-datuen balidazioaren inplementazioa (BalidazioTresnak)

Web aplikazioko sarrera-datuen balidazioa zentralizatzeko,
`BalidazioTresnak` izeneko utilitate-klase estatikoa erabiltzen da.

Klase honen ezaugarri nagusiak:

- Metodo estatikoak soilik ditu (ezin da instanziatu)
- Kontrolatzaileetatik zuzenean erabiltzeko pentsatua dago
- Balidazio sinple eta berrerabilgarria eskaintzen du
- Ez du negozio-logikarik edo datu-basearekiko dependentziarik

`BalidazioTresnak` erabiliz, kontrolatzaileek HTTP parametroak modu koherentean
irakurri, garbitu (trim) eta balioztatu ditzakete, kode bikoizketa saihestuz.

### 3.3.1 `getRequiredX` eta `getOptionalX` metodoak

Balidazio-metodoek bi familia nagusi dituzte:

- `getRequiredX(...)`
- `getOptionalX(...)`

Bereizketa honek **kasuaren semantika** adierazten du:

- `getRequiredX(...)` metodoek:
  - parametroa derrigorrezkoa dela adierazten dute
  - parametroa falta bada edo hutsik badago, errorea gehitzen dute
  - erabiltzaileak zuzendu beharreko errorea dela suposatzen dute
- `getOptionalX(...)` metodoek:

- parametroa hautazkoa dela adierazten dute
- parametroa falta bada, null itzultzen dute, errorea gehitu gabe
- parametroa existitzen bada baina baliogabea bada, errorea gehitzen dute

Metodo egokia aukeratzea **kontrolatzailearen ardura da**, kasu bakoitzeko erabilera-espezifikazioaren arabera.

---

### 3.4 Erabilitako salbuespena

Balidazio hauek huts egiten dutenean, `InputBalidazioSalbuespena` salbuespena erabiltzen da.

Salbuespen honek:

- errore-mezu bakarra, edo
- errore-zerrenda bat

izan dezake, balidazioan zehar metatutako erroreen arabera.

Salbuespen hau **kontrolatzailean bertan harrapatzen da** eta normalean:

- erabiltzailea jatorrizko bista berdinera itzultzen da, edo
- errorearen kritikotasunaren arabera, errore-orri orokorra bistaratzen da

Horrela, erabiltzaileak errore guztiak batera ikus ditzake, eta ez bata bestearen atzetik.

---

### 3.5 Adibide kontzeptualak

#### Adibide orokorra

Controller
├── HTTP parametroak hartu
├── Input balidazioa (BalidazioTresnak)
│   └── InputBalidazioSalbuespena
├── ServiceContext.open()
│   └── Zerbitzu-deiak
└── Bista (JSP)

#### Erabilera-kasua: langile baten `log in` eragiketa

POST /langilea/login
└── Controller
    ├── HTTP param → SaioHasieraDTO
    ├── Input balidazioa (BalidazioTresnak)
    ├── ServiceContext.open()
    │   └── LangileaService.saioaHasi()
    ├── Sesioa
    └── Redirect / Forward (Bista)

### 3.6 Eboluzio-aukerak

Diseinu honek aukera ematen du etorkizunean:

- Balidazio-logika zorrozteko `BalidazioTresnak` aldatuz
- Emailaren balidazio sendoagoa gehitzeko
- Bean Validation (Jakarta / Hibernate Validator) integratzeko
- Spring framework-ean oinarritutako arkitektura migratzeko

Hori guztia egungo kontrolatzaileak eta fluxua hautsi gabe.

## 4. Balidazioa Zerbitzu Geruzan (Service Layer)

### 4.1 Zer balidatzen da

Zerbitzu geruzan **negozio-logika** balidatzen da:

- negozio-arauak
- entitateen arteko koherentzia
- murrizketak (adib. email bikoiztua)

### 4.2 Erabilitako salbuespena

Negozio-arauak betetzen ez direnean `ZerbitzuSalbuespena` salbuespena erabiltzen da.

Salbuespen honek:

- errore-mezu bakarra, edo
- errore-zerrenda

izan dezake, balidazioan zehar metatutako erroreen arabera.

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

### 6.2 Soluzio Teknikoa (Web esparruan): `SalbuespenFilter`

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
| Parametro hutsa                      | `InputBalidazioSalbuespena` | `errorea.jsp` edo bista bera | Errorearen kritikotasunaren arabera                   |
| Email bikoiztua                      | `ZerbitzuSalbuespena`       | Formulario bera              | Jakinarazi helbide elektronikoa lehendik dagoela      |
| Kredentzial txarrak                  | `ZerbitzuSalbuespena`       | `login.jsp`                  | Errore-mezu generikoa erakutsi                        |
| JSP akatsa                           | `Exception`                 | `errorea.jsp`                | Zerbitzariaren barne-errorea                          |
| `SQLException`                       | `Exception`                 | `errorea.jsp`                | Datu-basearen errorea                                 |
| ID gaizki eratua                     | `ZerbitzuSalbuespena`       | `errorea.jsp`                | Errore kritikoa: ezin da testuingurua berreskuratu    |
| Parametro baliogabea (adib. filtroa) | `InputBalidazioSalbuespena` | Bista bera                   | Filtroa ez aplikatu, lehenetsitako balioak erabili    |

## 9. Ondorioa

Balidazioen eta erroreen kudeaketa DIY Garajea proiektuan
sinpletasuna, segurtasuna eta mantengarritasuna lehenesten dituen
eredu argi eta hezkuntza-arlokoan oinarritzen da.
