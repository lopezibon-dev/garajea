# Datu-baseko taulen inportazioa

Dokumentu honek *DIY Garajea* proiektuko **CSV fitxategiak datu-baseko tauletara inportatzeko erabilera-kasua** azaltzen du.
Helburua da inplementatutako irtenbidea modu argi eta egituratuan azaltzea, erabaki arkitektonikoak eta fluxua ulertzeko moduan.

---

## 1. Ikuspegi orokorra

Datubaseko taulen inportazioaren kasuaren helburua da **CSV fitxategi bat MySQL datu-baseko taula batera inportatzea**, Desktop aplikaziotik abiatuta.

Kasu honetan, **Java eta Pythonen arteko erantzukizunen banaketa argia** ezartzen da:

- **Java** desktop aplikazioa da erabilera-kasuaren orkestradorea:
  - Erabiltzailearen interakzioa kudeatzen du
  - Taulak zerrendatzen ditu
  - Python scriptaren exekuzioa abiarazten du, **SwingWorker bidez asinkronoki**
  - Exekuzioaren emaitza interpretatzen du, **DatubaseaService**-en
  - Errore funtzionalak `ZerbitzuSalbuespena` bidez igortzen dira
- **Python scripta** da lan teknikoaren **exekutatzailea**:
  - Datubasera konektatzen da
  - CSV fitxategiaren edukiak irakurtzen du
  - Taula hustu egiten du (`TRUNCATE` bidez)
  - CSV-ko datuak taulan txertatzen ditu
  - Irteera-kode baten bidez emaitza adierazten du
  
Bi inguruneen arteko komunikazioa irteera-kodeen (exit code) bidez egiten da soilik.
Ez da informaziorik trukatzen `stdout` edo `stderr`-en edukia analizatuz.

---

## 2. Erabilera-kasuaren fluxua

Erabilera-kasuaren exekuzio-fluxua honakoa da:

### 2.1 Taulen karga datu-basetik

- Desktop aplikazioak datu-basean dauden taulen zerrenda lortzen du.
- Horretarako, metadatuak erabiltzen dira, ez taulen edukiak:
  `DatubaseaService` → `DatubaseaMetaDAO` erabiltzen da

### 2.2 Taularen hautaketa

Erabiltzaileak inportatu nahi duen taula hautatzen du interfaze grafikoan.
Une honetan ez da inportaziorik egiten; hautaketa soilik erregistratzen da.

### 2.3 Python scriptaren exekuzio asinkronoa

Taula hautatu ondoren, erabiltzaileak *Inportatu* botoia sakatzen du, eta horrela desktop aplikazioak Python scriptaren exekuzioa abiarazten du:

- Beste prozesu batean
- UI-a blokeatu gabe (Python scripta `SwingWorker` bidez exekutatzen da)
- Parametro minimoak pasatuz

### 2.4 Emaitzaren interpretazioa

- Python scriptak amaitzean, Java aplikazioak irteera-kodea jasotzen du:
- Irteera-kode nagusiak:
  - 0: inportazioa ondo burutu da
  - 2: taula ez da existitzen edo ez du zutaberik
  - 6: CSV fitxategia ez da aurkitu
  - beste edozein: errore generikoa
- Java-k ez du `stdout` edo `stderr` aztertzen
- Irteera-kodearen interpretazioa zerbitzu-geruzan gertatzen da
- Irteera-kodea 0 ez bada, `ZerbitzuSalbuespena` jaurtitzen da, dagokion mezuarekin

### 2.5 Erabiltzaileari erantzuna ematea

Azkenik, desktop aplikazioak erabiltzaileari mezu argi bat erakusten dio:

- Arrakasta-mezua, inportazioa ondo joan bada
- Errore-mezua, bestela

---

## 3. Erabaki arkitektonikoak

Erabilera-kasu honetarako hartutako erabaki nagusiak hauek dira:

- **`DatubaseaService`** erabiltzen da **fatxada** gisa
  - Desktop aplikazioak ez du DAOekin zuzenean lan egiten.
  - **`DatubaseaMetaDAO`** erabiltzen da datu-baseko metadatuak lortzeko
(taulen izenak, egitura orokorra).
  - Domeinuko entitateetatik independentea da
- **`PythonInportazioExecutor`** klasea
  - Python prozesuaren exekuzioaz arduratzen den osagaia da
  - `ProcessBuilder`-en erabilera kapsulatzen du
- **Sarrera-fitxategien bideen kudeaketa ez da inoiz egiten Java-n.**
  - ez da UI-n edo zerbitzuan erabakitzen
  - Java-k ez daki non dagoen CSV fitxategia
- **`data/datubasea` karpeta**:
  - repositoriotik kanpo dago (`.gitignore` bidez)
- **Komunikazio-arau zorrotza**:
  - `exit code` da komunikazio-bide bakarra
  - `stdout` / `stderr` es dira parseatzen
  
 Erabaki hauek guztiak proiektuaren garbitasuna, mantengarritasuna eta ikasgaitasuna indartzeko hartu dira.

---

## 4. Ibilbideen kudeaketa (Path)

Ibilbideen kudeaketa bi mailatan bereizten da, ardurak argi banatuz:

### Java aldean

- Java desktop aplikazioak **JVM-aren exekuzio-direktorioa** erabiltzen du oinarri gisa:

```java
  System.getProperty("user.dir")
```

- Direktorio hori `garajea` proiektuaren erroarekin bat dator
- Hortik abiatuta, Java-k modu esplizituan eraikitzen ditu:
  - Python interpretearen bidea
    (`scripts/.venv/Scripts/python.exe`)
  - Esportazio-scriptaren bidea
    (`scripts/esportatu_taula.py`)
- Java-k **ez du erabakitzen**:
  - sarrerako karpetarik
  - CSV fitxategien kokapena
- Java-k soilik pasatzen dizkio scriptari:
  - taularen izena
  - CSV fitxategiaren izen logikoa
  
### Python aldean

Python scriptak berak kudeatzen ditu bide guztiak:

- Python scriptak **bere burua kokatzen du**:

```python
  Path(__file__).resolve()
```

- Hortik abiatuta:
  - proiektuaren erroa kalkulatzen du
  - `data/datubasea` karpeta definitzen du
  - CSV fitxategiaren bide osoa eraikitzen du
- Banaketa honek honako abantailak ematen ditu:
  - *working directory*-arekiko independentzia
  - Proiektuaren egitura-aldaketen aurrean sendotasuna
  - Java eta Python proiektuen arteko koherentzia
  - `hardcoded` bideen eta mendekotasun inplizituen ezabapena
  
---

## 5. Fluxuaren diagrama sinplea

### Bloke-diagrama

```mermaid
flowchart LR
    UI[Java Desktop UI]
    Service[DatubaseaService]
    Executor[PythonInportazioExecutor]
    Python[Python Inportazio Scripta]
    CSV[CSV fitxategia]

    UI --> Service
    Service --> Executor
    Executor --> Python
    Python --> CSV
```

Diagrama honek argi erakusten du kasuaren ibilbidea, arduren banaketa eta osagaien arteko harremana modu sinplean azalduz.
