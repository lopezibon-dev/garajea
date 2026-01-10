# Erroreak eta Balidazioak – Desktop Aplikazioa (DIY Garajea)

## 1. Helburua

Dokumentu honen helburua da DIY Garajea proiektuaren **desktop aplikazioan (Swing)**
erroreen eta balidazioen kudeaketa nola egiten den azaltzea.

Dokumentu hau **web aplikaziorako erroreen dokumentaziotik bereizita** dago,
bi exekuzio-inguruneek (web eta desktop) dituzten desberdintasun arkitektonikoak
argi islatzeko.

Helburu nagusiak:

- Web eta desktop esparruak ez nahastea
- Negozio-erroreen kudeaketa bateratua mantentzea
- UI mailako erabakiak desktop testuingurura egokitzea
- Etorkizuneko funtzionalitateetarako oinarri sendoa ezartzea

---

## 2. Printzipio Orokorrak (Desktop)

Desktop aplikazioan honako printzipio hauek aplikatzen dira:

- Balidazioak **UI mailan eta Service Layer-ean** banatzen dira
- Ez dago HTTP, Servlet edo Filter mekanismorik
- Salbuespenak gorantz propagatzen dira UIra
- UIak erabakitzen du nola erakutsi errorea
- Errore teknikoak **logean gordetzen dira**, ez erabiltzaileari erakusten

Printzipio hauek web arkitekturarekin koherenteak dira,
baina exekuzio-ingurunera egokituta.

---

## 3. Balidazioa Desktop UI mailan

### 3.1 Zer balidatzen da

Swing UIko ekintzetan (adib. botoiak, formularioak):

- Eremu hutsak
- null balioak
- Formatu okerrak (emaila, zenbakiak, datak)
- UI egoera baliogabeak (adib. ezer ez hautatua)

Balidazio hauek **zerbitzuak deitu aurretik** egiten dira.

### 3.2 Erabilitako salbuespena

Balidazio hauek huts egiten dutenean:

- `InputBalidazioSalbuespena`

Salbuespena **UI mailan harrapatzen da**, eta normalean:

- `JOptionPane` bidezko mezu argia erakusten da
- edo UI osagaietan bertan (etiketak, koloreak) adierazten da

---

## 4. Balidazioa Zerbitzu Geruzan (Berdina Web eta Desktop)

### 4.1 Zer balidatzen da

Service Layer-ean:

- Negozio-arauak
- Entitateen arteko koherentzia
- Murrizketa funtzionalak (egoerak, baldintzak, existentzia)

### 4.2 Erabilitako salbuespena

- `ZerbitzuSalbuespena`

Salbuespena:

- UI mailara propagatzen da
- Ez du UI logikarik ezagutzen

Desktop aplikazioan, UIak erabakitzen du:

- Mezu zehatza erakutsi ala generikoa
- Eragiketa bertan behera utzi ala UI egoera leheneratu

---

## 5. Errore Teknikoak Desktop aplikazioan

### 5.1 Zer dira errore teknikoak

Errore teknikoak dira:

- `SQLException`
- `NullPointerException`
- Konexio edo baliabide erroreak
- Programazio-akatsak

Errore hauek **ez dira erabiltzailearen errua** eta
**ez dira UI mailan konpontzen**.

### 5.2 Kudeaketa-printzipioa

- Ez dira UI logikan tratatzen
- Ez dira berreskuragarriak erabiltzaile mailan
- Gorantz propagatzen dira
- Log sisteman erregistratzen dira

---

## 6. Errore teknikoen kudeaketa (Desktop)

### 6.1 Ez dago Filter mekanismorik

Desktop aplikazioan:

- Ez dago `Filter` edo antzeko middleware mekanismorik
- Errore teknikoak **UI ekintzen goiko mailan** harrapatzen dira

Adibidez:

- botoi baten `ActionListener`
- edo pantaila nagusiko kontrol-fluxuan

### 6.2 Jarduera estandarra

Errore teknikoa gertatzen denean:

1. Errorea logean gordetzen da (Logback)
2. Erabiltzaileari mezu generikoa erakusten zaio
3. Aplikazioa egoera koherente batean mantentzen da

Adibideko mezu tipikoa:

> "Errore tekniko bat gertatu da. Saiatu berriro edo jarri harremanetan administratzailearekin."

---

## 7. Logging sistema (Desktop)

Desktop aplikazioan:

- **Logback** erabiltzen da
- Log fitxategia: `C:/logs/garajea_desktop.log`
- Stack trace osoa gordetzen da
- Errotazio automatikoa aktibatuta dago

Erabiltzaileari:

- Ez zaio inoiz stack trace edo xehetasun teknikorik erakusten

---

## 8. Fluxu orokorra (Desktop)

UI Ekintza (Swing)
└── UI balidazioa
└── ServiceContextFactory.open()
    └── Zerbitzuak
        └── DAO
↑
└── Salbuespenen kudeaketa UI mailan

---

## 9. Ondorioa

Desktop aplikazioko erroreen eta balidazioen kudeaketa
DIY Garajea proiektuan:

- Web arkitekturarekin koherentea da
- Baina exekuzio-ingurunera egokituta dago
- Sinpletasuna eta mantengarritasuna lehenesten ditu
- Etorkizuneko funtzionalitateetarako oinarri sendoa eskaintzen du

