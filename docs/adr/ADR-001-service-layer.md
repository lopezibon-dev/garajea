# ADR-001: Service Layer erabiltzea

## Egoera

Onartua.

## Testuingurua

DIY Garajea proiektua geruza anitzeko arkitektura batean oinarritzen da
(MVC patroia), non Web eta Desktop interfazeek negozio-logikatik
bereizita egon behar duten.

Hasierako diseinuan, honako aukera hauek aztertu dira:

- Kontrolatzaileetatik zuzenean DAOak erabiltzea
- Negozio-logika geruza espezifiko batean zentralizatzea

Helburua da:

- negozio-logika isolatzea
- datu-iraunkortasuna kapsulatzea
- transakzioen kudeaketa modu bateratuan egitea
- hezkuntza-helbururako arkitektura argia eskaintzea

## Erabakia

Negozio-logika **Zerbitzu Geruza (Service Layer)** batean zentralizatzea erabaki da.

Zerbitzu geruzak honako ardura hauek izango ditu:

- negozio-arauen aplikazioa
- DAOen erabilera datuak eskuratzeko
- transakzioen eta baliabideen kudeaketa
- Web eta Desktop geruzen arteko isolamendua

Kontrolatzaileek ez dute zuzenean DAOekin lan egingo;
beti Zerbitzu Geruzaren bidez sartuko dira negozio-logikara.

## Ondorioak

Aukera honek honako ondorio positiboak ditu:

- Arkitektura argiagoa eta mantengarriagoa
- Negozio-logikaren berrerabilpena (Web eta Desktop)
- Testatzeko errazagoa den kodea
- Hezkuntza-helbururako egokia den geruza-banaketa

Bestalde, geruza gehigarri bat sartzeak
hasierako konplexutasun pixka bat gehitzen du,
baina onartutako kostua da proiektuaren helburua kontuan hartuta.
