# MVC eredua – Desktop (Swing)

## 1. Helburua

Dokumentu honen helburua da DIY Garajea proiektuan
**desktop aplikazioan (Swing)** Model-View-Controller (MVC) eredua
nola aplikatzen den azaltzea.

Dokumentu hau **mvc-web.md** dokumentuaren osagarria da,
baina biak independenteak dira:

- MVC eredua bera da
- Inplementazioa eta exekuzio-ingurunea desberdinak dira

Helburua da:

- arduren banaketa argia mantentzea
- web eta desktop esparruak ez nahastea
- etorkizuneko hedapenak erraztea

---

## 2. Ikuspegia (View)

### 2.1 Teknologia

Desktop ikuspegia **Java Swing** teknologian oinarritzen da:

- `JFrame`
- `JPanel`
- `JButton`, `JLabel`, `JTextField`, ...
- `JTable`, `JList`

### 2.2 Ardura nagusiak

Ikuspegiaren ardura nagusiak hauek dira:

- Datuen bistaratzea
- Erabiltzailearen interakzioa jasotzea
- Errore-mezuen eta informazio-mezuen erakusketa

### 2.3 Murrizketak

Ikuspegiak **ez du**:

- negozio-logikarik
- zerbitzu-deirik zuzenean
- DAO edo ServiceContext erabilerarik

Ikuspegiak soilik **kontrolatzaileari** deitzen dio
UI ekintzen bidez.

---

## 3. Kontrolatzailea (Controller)

### 3.1 Kontzeptua

Desktop esparruan, kontrolatzailea:

- Swing event handler-en bidez inplementatzen da
- botoien ekintzak, aukerak edo form-en bidalketak kudeatzen ditu

Ez dago HTTP eskaerarik, baina **kontzeptua berdina da**:

- UI ekintza = web-eko HTTP request baten baliokidea

### 3.2 Ardura nagusiak

Kontrolatzailearen ardurak:

- Sarrera-datuen balidazioa
- ServiceContext irekitzea
- Negozio-zerbitzuak deitzea
- Emaitzen arabera ikuspegia eguneratzea

### 3.3 Zer ez duen egiten

Kontrolatzaileak **ez du**:

- negozio-arauik inplementatzen
- SQL kontsultarik exekutatzen
- datu-base konexiorik zuzenean kudeatzen

---

## 4. Zerbitzu-geruzarekiko integrazioa

Desktop aplikazioak web aplikazioaren
**zerbitzu-geruza bera** erabiltzen du (`garajea-core`).

### 4.1 ServiceContext erabilera

Ekintza bakoitzean (adib. botoi baten klik-a),
ServiceContext bat irekitzen da:

```java
try (ServiceContext sc = serviceContextFactory.open()) {
    // Zerbitzu-deiak
}
```

Ezaugarriak:

- `ServiceContextFactory` → aplikazio-mailakoa
- `ServiceContext` → UI ekintza bakoitzeko
- `try-with-resources` eredua sistematikoki erabiltzen da

### 4.2 Antzekotasuna web esparruarekin

Web aplikazioan:

- HTTP eskaera bakoitzeko ServiceContext bat

Desktop aplikazioan:

- UI ekintza bakoitzeko ServiceContext bat

Horrela, bi esparruek
**bizitza-ziklo berdina** partekatzen dute.

---

## 5. Errore-kudeaketa

Desktop aplikazioan errore-kudeaketa
kontrolatzaile mailan egiten da.

### 5.1 Negozio-erroreak

- `ZerbitzuSalbuespena`
- Negozio-arauak betetzen ez direnean jaurtitzen da

Kudeaketa:

- Erabiltzaileari mezua erakutsi (`JOptionPane`)
- Ez da stack trace-a bistaratzen

### 5.2 Errore teknikoak

Adibideak:

- `SQLException`
- `NullPointerException`
- Baliabideak ixteko erroreak

Kudeaketa:

- Log-ean erregistratu (Logback)
- Erabiltzaileari mezu generikoa erakutsi

---

## 6. Fluxu orokorra

Desktop aplikazioko ekintza baten fluxu orokorra:

```
Erabiltzailearen ekintza
 └── Ikuspegia (Swing)
       └── Kontrolatzailea
             ├── Input balidazioa
             ├── ServiceContext.open()
             │     └── Zerbitzuak
             │           └── DAO
             └── Ikuspegiaren eguneraketa
```

Fluxu honek web aplikazioaren egitura bera jarraitzen du,
baina HTTPren ordez Swing ekintzak erabiliz.

---

## 7. Laburpena

DIY Garajea proiektuan desktop aplikazioak:

- MVC eredua errespetatzen du
- Web aplikazioaren zerbitzu-geruza bera erabiltzen du
- UI eta kontrol-fluxu propioa du

Honek aukera ematen du:

- arkitektura koherente bat mantentzeko
- web eta desktop kanalak independente garatzeko
- etorkizuneko funtzionalitate berriak modu garbian gehitzeko
