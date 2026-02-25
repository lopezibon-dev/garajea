# Desktop UI – Osagaien Egituraren Dokumentazioa

Dokumentu honek Desktop aplikazioko pantaila bakoitzaren Swing osagaien egitura hierarkikoa jasotzen du.

Helburua ez da diseinu grafikoa edo layout zehatza azaltzea,
baizik eta ikuspegi arkitektonikotik panel bakoitzaren konposizio estrukturala dokumentatzea.

Honek aukera ematen du:

- Pantailen arteko koherentzia aztertzeko
- Osagaien erabilera errepikakorrak identifikatzeko
- UI egituraren konplexutasuna aztertzeko
- Errefaktorizazio posibleak azkar detektatzeko

Dokumentu hau ez da MVC arkitekturaren azalpena, baizik eta aurkezpen geruzaren egitura teknikoaren dokumentazioa (Desktop esparruan).

Pantaila guztiak (LoginFrame ezik) MainFrame edukiontzi nagusian kargatzen diren JPanel edo JFrame egiturak dira.

Dokumentazio irizpideak:

- Osagai estrukturalak soilik dokumentatzen dira.
- LayoutManager zehatzak ez dira adierazten, erabakigarriak ez badira.
- Panel bitartekariak izen propioarekin jasotzen dira.
- SwingWorker edo atzeko logika ez da hemen dokumentatzen.

## 0. Desktop Aplikazioaren Egitura Nagusia

```text
MainFrame (JFrame)
 ├─ JMenuBar
 └─ contentPanel (JPanel - BorderLayout)
    └─ (kargatutako JPanel dinamikoa: kasu bakoitza)
```

Oharra:
MainFrame aplikazioaren edukiontzi nagusia da.
Erabilera-kasu bakoitza contentPanel barruan kargatzen da.

---

## 1. Login Pantaila

```text
LoginFrame (JFrame)
 └─ panel (JPanel - GridBagLayout)
     ├─ logoLabel (JLabel)
     ├─ emailLabel (JLabel)
     ├─ emailField (JTextField)
     ├─ pasahitzaLabel (JLabel)
     ├─ pasahitzaField (JPasswordField)
     └─ loginBtn (JButton)
```

Oharra:
Login pantaila aplikazioaren sarrera-puntua da,
eta MainFrame sortu aurretik exekutatzen da.

## 2. Langilearen Profila

Profil-panel honek hiru zona estruktural ditu (goikoa, erdikoa eta behekoa).
Erdiko zatian JTabbedPane bidez bi azpipanel independente kargatzen dira.

```text
LangileaProfilPanel (JPanel - BorderLayout)
 ├─ izenaLabel (JLabel)
 ├─ tabs (JTabbedPane)
 │   ├─ agendaPanel (LangileErreserbakAgendaPanel)
 │   └─ tablePanel (LangileErreserbakTablePanel)
 └─ oinekoPanela (JPanel - FlowLayout)
     └─ berrizKargatuBtn (JButton)
```

### 2.1 Agenda Azpipanela

```text
LangileErreserbakAgendaPanel (JPanel - BorderLayout)
 └─ scrollPane (JScrollPane)
     └─ agendaContentPanel (JPanel - BoxLayout Y_AXIS)
```

Oharra:
Panel honek scroll bidez kudeatzen duen eduki bertikala dauka.
Agenda-edukia dinamikoa da, baina egitura estrukturala konstantea da.

### 2.2 Taula Azpipanela

```text
LangileErreserbakTablePanel (JPanel - BorderLayout)
 └─ scrollPane (JScrollPane)
     └─ taula (JTable)
```

Oharra:
Panel hau JTable batean oinarritzen da, JScrollPane baten barruan kapsulatuta.
Datuen karga eta eguneraketa ez dira dokumentu honen parte.

## 3. Eguneko Erreserbak Kabinaka

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

Oharra:
Panel honek bi zona nagusi ditu: goiko kontrol-eremua eta kabinen agenda-eremua.
KabinaAgendakPanel 1x5 GridLayout egitura finko batean oinarritzen da.
Kabina bakoitza zutabe independiente gisa modelatzen da JPanel - GridLayout horren barruan.
Kabina bakoitzaren edukia timelineBody panel dinamiko batean kargatzen da.

## 4. Datubasea – Taulen Esportazioa

```text
ExportatuTaulaPanel (JPanel - BorderLayout)
 ├─ goikoPanela (JPanel - FlowLayout)
 │   ├─ taulaLabel (JLabel)
 │   ├─ taulaCombo (JComboBox)
 │   └─ esportatuBotoia (JButton)
 └─ egoeraLabel (JLabel)
```

Oharra:
Panel honek ekintza bakarreko egitura sinplea dauka.
Goiko panelean taularen hautaketa eta ekintza-botoia daude,
eta behealdean egoeraren mezua erakusten da.

## 5. Datubasea – Taulen Inportazioa

```text
InportatuTaulaPanel (JPanel - BorderLayout)
 ├─ goikoPanela (JPanel - FlowLayout)
 │   ├─ taulaLabel (JLabel)
 │   ├─ taulaCombo (JComboBox)
 │   └─ inportatuBotoia (JButton)
 └─ egoeraLabel (JLabel)
```

Oharra:
Esportazio-panelaren antzeko egitura dauka.
Konposizio estrukturala berdina da, baina ekintza desberdina exekutatzen du.
