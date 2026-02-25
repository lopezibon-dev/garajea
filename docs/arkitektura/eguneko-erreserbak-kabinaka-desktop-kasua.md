# Eguneko Erreserben kabinaka bistaratzea (Desktop – Swing)

Dokumentu honek DIY Garajea proiektuko **“Eguneko Erreserbak kabinaka bistaratzea”** kasuaren azalpen tekniko sintetizatua jasotzen du, Desktop ingurunean (Swing) inplementatua. Dokumentazioaren fokua **ErreserbakPanel** panelaren diseinu‑erabakietan dago, ez inplementazio‑xehetasunetan.

---

## 1. ErreserbakPanel‑aren egoera (state)

ErreserbakPanel‑ak egoera minimo eta esplizitu bat mantentzen du, bistaren koherentzia eta eguneraketa kontrolatzeko.

- **LocalDate eguna**  
  Unean bistaratzen ari den eguna adierazten du. Panelaren egoera logikoaren oinarria da, eta datuen karga eta ber-renderizatzea baldintzatzen ditu.

- **JLabel egunaLabel**  
  Uneko eguna erabiltzaileari testu moduan erakusteko erabiltzen den etiketa. `eguna` atributuaren islapen bisuala da.

- **JPanel kabinaAgendakPanel**  
  Kabina guztien agenda‑zutabeak biltzen dituen edukiontzi nagusia. Panel honen barruan sortzen dira kabinaka antolatutako timeline itxurako egiturak.

- **Map<String, JPanel> kabinaTimelineMap**  
  Kabina bakoitzaren izena bere timeline‑aren panelarekin erlazionatzen duen mapa. Helburua da:
  - agendak modu kontrolatuan garbitzea,
  - kabina bakoitzaren edukia berriro populatzea,
  - UI‑aren berreraiketa partziala erraztea.

---

## 2. Erantzukizunen banaketa ErreserbakPanel barruan

Panelaren barne‑logika funtzio‑multzotan banatuta dago, UI‑aren egitura, datuen karga eta errenderrizazioa argi bereizteko.

### initUI

Swing interfazearen **eraikuntza estruktural eta estatikoa** egiten du:

- layout nagusiak definitzen ditu,
- panel eta osagai finkoak sortzen ditu,
- hasierako egitura bisuala prestatzen du.

Metodo honek **ez du daturik kargatzen**, eta ez dauka negozio‑logikarik.

### kargatuKabinenDatuak

Core moduluko zerbitzuetara deituz, **egun jakin baterako kabinen agenda‑datuak** eskuratzen ditu. Metodo honek:

- UI‑tik kanpo bizi den logika erabiltzen du,
- jasotako informazioa bistaratze‑faseari pasatzen dio,
- panelaren egoera (`eguna`) kontuan hartzen du.

### erakutsiKabinenAgendak

Kabina bakoitzerako agenda osoaren **renderizatzea** egiten du:

- `timelineBody` panelen barruko edukia (labelak) ezabatzen du,
- kabina bakoitzerako agenda bisual bat bistaratzen du kargatze-fasetik datuak jasoz,
- `sortuAgendaLerroa` erabiliko du agendako osagaiak renderizatzeko.

Metodo honek datu-egiturak UI osagai bihurtzen ditu, baina ez du daturik kalkulatzen.

### sortuAgendaLerroa

`AgendaBlokeaDTO` batetik abiatuta, **agenda‑osagai bisual bakarra** sortzen du. Osagaiak mota ezberdinetakoak izan daitezke, eta mota bakoitzerako, UI osagai egokia bueltatzen du.

Metodo hau unitate bisual txikien eraikuntzaz arduratzen da.

### Helper metodoak

Adibidez, `formateatuEguna` bezalako metodoek:

- formateo‑ardurak isolatzen dituzte,
- kode nagusiaren irakurgarritasuna hobetzen dute,
- errepikapena saihesten dute.

Ez dute egoerarik aldatzen, eta laguntza‑funtzio hutsa dute.

---

## 3. Swing maquetazioaren zuhaitza

Ondoko eskemak ErreserbakPanel‑aren egitura bisuala ulertzeko ikuspegi orokorra eskaintzen du:

```text
ErreserbakPanel (JPanel - BorderLayout)
 ├─ goikoKontrolPanel (JPanel - FlowLayout)
 │   ├─ egunaTextLabel (JLabel)
 │   ├─ egunaLabel (JLabel)
 │   └─ datePicker (DatePicker)
 └─ kabinaAgendakPanel (JPanel - GridLayout 1x5)
     ├─ agendaCol (JPanel - BorderLayout)
     │   ├─ kabinaLabel (JLabel)
     │   └─ scrollPane (JScrollPane)
     │       └─ timelineBody (JPanel - BoxLayout Y_AXIS)
     │           ├─ agendaLerroa (JLabel)
     │           ├─ agendaLerroa (JLabel)
     │           └─ ...
     ├─ agendaCol (...)
     ├─ agendaCol (...)
     ├─ agendaCol (...)
     └─ agendaCol (...)
```

Egitura honek aukera ematen du:

- kabina bakoitza zutabe independente gisa bistaratzea,
- agenda‑lerroen scroll bertikala kontrolatzea,
- kabinen artean koherentzia bisuala mantentzea.

---

Dokumentazio honek ErreserbakPanel‑aren diseinuaren ikuspegi kontzeptuala eskaintzen du, DIY Garajea proiektuaren helburu didaktikoekin eta MVC + DAO arkitekturarekin koherentea.
