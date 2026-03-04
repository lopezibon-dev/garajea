# Erreserba estatistiken txostenak

Dokumentu honek **erreserben estatistiken txostenak sortzeko prozesua** azaltzen du, datu‑baseko datuetatik abiatuta HTML, CSV eta Markdown (MD) formatuko txostenak sortu arte.

Helburua ez da inplementazio‑xehetasun guztietan sakontzea, baizik eta **eredu kontzeptuala (eredu semantikoa)** eta **datuen eraldaketa‑kate osoa** modu ulergarrian dokumentatzea.

---

## 1. Txostenaren eredu semantiko abstraktua

Txosten guztien oinarrian **eredu semantiko bakarra** dago, formatuarekiko (HTML, MD, …) independentea dena.

Eredu horrek honako esanahia du:

- Urte bateko erreserben estatistikak
  - urtea: zein urteko estatistikak diren
  - urteko erreserba kopuru osoa dauka
  - hilabetetan banatzen da
- Hilabete bakoitzak:
  - bere erreserba kopuru osoa dauka
  - kabinen araberako banaketa dauka
- Kabina bakoitzak:
  - hilabete jakin batean zenbat erreserba izan dituen adierazten du

Eredu hau **liburu baten antzera** uler daiteke:

- Dokumentua → urteko txostena
- Atalak → hilabeteak
- Azpi‑edukiak → kabinen zerrendak

Formatuaren (HTML, Markdown) araberako erabakiak **eredu hau kontutan harturik** hartzen dira.

---

## 2. Datuen jatorria: datu‑baseko errenkadak

Lehenengo pausuan, datu‑basetik **ezinbesteko datu agregatuak** lortzen dira:

- Hilabetea
- Kabinaren izena
- Erreserba kopurua

Pythonen, horren emaitza errenkada‑zerrenda bat da:

```text
(1, 'Kabina01', 18)
(1, 'Kabina02', 24)
(1, 'Kabina03', 22)
(1, 'Kabina04', 26)
(1, 'Kabina05', 20)
(2, 'Kabina01', 15)
...
```

Funtzio adibidea:

```Python
datu_errenkadak = fetch_erreserba_stats(urtea)
```

---

## 3. Datuen egituraketa: Python dict konplexua

Bigarren pausuan, errenkada linealak **egitura hierarkiko batean** bihurtzen dira. Egitura honek jada txostenaren eredu semantikoa islatzen du.

Adibide sinplifikatua:

```Python
stats = {
    "urtea": 2025,
    "urtekoGuztizkoa": 1472,
    "hilabeteak": {
        1: {
            "hilabetea": 1,
            "guztira": 110,
            "kabinak": {
                "Kabina01": 18,
                "Kabina02": 24,
                "Kabina03": 22
                # ...
            }
        },
        2: { ... }
        # ...
    }
}
```

Pausu honetan kalkulatzen dira:

- hilabeteko guztizkoak
- urteko guztizkoa

Funtzio adibidea:

```Python
stats = build_stats_dict(urtea, datu_errenkadak)
```

---

## 4. XML zuhaitzaren sorrera

Ondoren, Python‑eko 'dictionary' egitura konplexu horretatik **XML zuhaitz bat** sortzen da. XML honek eredu semantikoa modu esplizituan adierazten du.

### XML egitura kontzeptuala

- erro‑elementua: `erreserba_estatistikak`
- erroaren barruan: `hilabete` elementuak
- hilabete bakoitzaren barruan: `kabina` elementuak

Egitura hierarkikoa:

```text
erreserba_estatistikak
├── hilabete
│   ├── kabina
│   ├── kabina
├── hilabete
│   ├── kabina
│   ├── kabina
```

### XML adibidea

```xml
<erreserba_estatistikak urtea="2025" urtekoGuztizkoa="1472">

  <hilabete zenbakia="1" kopurua="110">
    <kabina izena="Kabina01" kopurua="18"/>
    <kabina izena="Kabina02" kopurua="24"/>
    <kabina izena="Kabina03" kopurua="22"/>
    <!-- ... -->
  </hilabete>

  <hilabete zenbakia="2" kopurua="106">
    <kabina izena="Kabina01" kopurua="15"/>
    <kabina izena="Kabina02" kopurua="19"/>
    <!-- ... -->
  </hilabete>
  <!-- ... -->
</erreserba_estatistikak>
```

Funtzio adibidea:

```Python
xml_file = DATA_DIR / f"stats_erreserbak_{urtea}.xml"
write_stats_xml(stats, xml_file)
```

---

## 5. XSLT bidezko eraldaketak

XML fitxategia **informazio‑iturri bakarra** da. Hortik abiatuta, XSLT erabiliz txosten desberdinak sortzen dira.

### 5.1 Irteera motak (`xsl:output`)

- HTML / XML txostenak:

```xml
<xsl:output method="html" encoding="UTF-8"/>
```

- Markdown edo testu arrunteko txostenak:

```xml
<xsl:output method="text" encoding="UTF-8"/>
```

Irteera motak baldintzatzen du Python‑etik nola idazten den emaitza.

---

### 5.2 Erabilitako XSLT kontzeptuak

XSL orrietan honako kontzeptuak erabili dira:

- `xsl:value-of`: balioak idazteko
- `xsl:apply-templates`: datu homogeneoen iterazio modularra

```xml
<xsl:apply-templates select="erreserba_estatistikak/hilabete" mode="mobile"/>
```

- `xsl:for-each`: egitura heterogeneoak (adib. taulak) sortzeko

```xml
<xsl:for-each select="erreserba_estatistikak/hilabete">
  <xsl:for-each select="kabina">
    ...
  </xsl:for-each>
</xsl:for-each>
```

- `mode`: bista desberdinak (mobile / desktop) bereizteko
- `xsl:template` + `xsl:param`: funtzio gisa erabiltzeko (adib. hilabete‑izena)
- `xsl:variable`: balio agregatuak kalkulatzeko

---

## 6. Sortutako txosten motak

### 6.1 Hilabeteka

#### HTML – mobile bista

```html
<section class="hilabete">
  <h2>Urtarrila – Guztira: 110</h2>
  <ul>
    <li>Kabina01: 18</li>
    <li>Kabina02: 24</li>
  </ul>
</section>
```

#### HTML – desktop bista

```html
<table>
  <thead>
    <tr>
      <th>Hilabetea</th><th>Guztira</th><th>Kabina01</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Urtarrila</td><td>110</td><td>18</td>
    </tr>
  </tbody>
</table>
```

#### Markdown

```text
## Urtarrila – Guztira: 110

- Kabina01: 18
- Kabina02: 24
...
```

---

### 6.2 Kabinaka

#### HTML – mobile

```html
<section class="kabina">
  <h2>Kabina01 – Guztira: 220</h2>
  <ul>
    <li>Urtarrila: 18</li>
    <li>Otsaila: 15</li>
    <!-- ... -->
  </ul>
</section>
<!-- ... -->
```

#### HTML – desktop

```html
<h2>Kabina01</h2>
<table>
  <thead>
    <tr>
      <th>Hilabetea</th>
      <th>Erreserbak</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Urtarrila</td>
      <td>18</td>
    </tr>
  </tbody>
  <tfoot>
    <tr>
      <th>Guztira</th>
      <th>220</th>
    </tr>
  </tfoot>
</table>
```

---

## 7. Laburpena

- XML fitxategiak **eredu semantiko bakarra** adierazten du
- Python‑ek datuak prestatzen eta agregatzen ditu
- XSLT‑k formatuaren araberako bistak sortzen ditu
- HTML eta Markdown **eduki beraren serializazio desberdinak** dira
