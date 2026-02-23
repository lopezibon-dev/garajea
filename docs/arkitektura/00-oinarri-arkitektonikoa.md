# 00 – Oinarri Arkitektonikoa

## 1. Helburua

Garajea proiektuaren arkitektura-printzipio eta muga formalak dokumentatzea.
Dokumentu honek sistemaren egitura kontzeptuala eta geruzen arteko dependentzia-arauak ezartzen ditu.

Hemen definitutako irizpideek lehentasuna dute beste edozein dokumentu edo inplementazio zehatzen gainetik.

---

## 2. Proiektuaren izaera

* Hezkuntza-proiektua da, arkitektura zuzena lantzeko sortua.
* Ez da produktu komertzial bat.
* Helburua da diseinu garbia, koherentzia eta geruzen arteko bereizketa zorrotza.

Konplexutasun gehikuntzak justifikatu behar dira.

---

## 3. Arkitektura Orokorra

Sistema aplikazio banatua da:

* Web aplikazioa
* Desktop aplikazioa

Biak partekatzen dute:

* Model modulua (entitateak + DAO)
* Core modulua (negozio-logika)

Maven multi-modulu egitura derrigorrezkoa da.
Ez da onartzen dependentzia-saltoarik.

---

## 4. Geruzen Banaketa

Arkitektura geruzatua eta zorrotza da:

1. Aurkezpena (web / desktop)
2. Zerbitzu-geruza (core)
3. Persistenzia (model – DAO)

### Debeku formalak

* DAO ez da inoiz deitzen aurkezpen geruzatik.
* Zerbitzuek ez dute zuzenean JDBC edo SQL erabiltzen.
* Ez da onartzen Connection, ResultSet edo SQL agertzea model modulutik kanpo.

---

## 5. ServiceContext eredua

* UI ekintza bakoitzak ServiceContext propioa du.
* Controller-ak ireki eta itxi egiten du.
* Zerbitzuek testuingurua existitzen dela suposatzen dute.
* DAO-ek ez dute testuingurua sortzen edo kudeatzen.

Transakzionaltasuna:

* Erabakia zerbitzuarena da (kasu-erabileraren arabera).
* Inplementazio teknikoa azpiegiturarena da.
* Ez dira transakzioak irekitzen beharrezkoa ez bada.

---

## 6. Balidazio eta Errore Kudeaketa

Balidazioa geruzaka banatzen da:

* Controller: formatua eta koherentzia teknikoa
* Zerbitzua: negozio-arauak
* DAO: integritate minimo teknikoa

Entitateek ez dute negozio-logika konplexurik.

---

## 7. Dokumentazioaren Printzipioa

Dokumentazioa arkitekturaren parte da, ez eranskin bat.

* Diseinu-erabakiak esplizituak izan behar dira.
* Kasu-erabilerak aztertu eta justifikatu behar dira.
* Aldaketa estrukturalek koherentzia mantendu behar dute dokumentu honekin.

---
