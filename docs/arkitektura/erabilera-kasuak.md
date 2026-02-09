# Erabilera-kasuak (Use Case Map)

Dokumentu honek DIY Garajea proiektuan
inplementatutako erabilera-kasuak nagusien
ikuspegi orokorra eskaintzen du.

Helburua ez da erabilera-kasuak guztien zerrenda zehatza ematea, baizik eta Zerbitzu geruzaren diseinua eta erabilera ulertzea.

Erabilera-kasuak eboluzionatzen doaz proiektuarekin batera.

---

## Bezeroa

Bezeroarekin lotutako erabilera-kasuak BezeroaService-n
zentralizatzen dira.

Adibideak:

- Bezero berri bat erregistratzea
- Bezero baten saioa hastea
- Bezeroaren profila kargatzea
- Bezeroaren datu pertsonalak eguneratzea
- Pasahitza aldatzea

---

## Langilea

Langileekin lotutako erabilera-kasuak LangileaService-n
kudeatzen dira.

Adibideak:

- Langile baten saioa hastea
- Langilearen profila kargatzea
- Lan-karga edo esleitutako erreserbak kontsultatzea

---

## Erreserbak

Erreserbekin lotutako erabilera-kasuak ErreserbaService-n
inplementatzen dira.

Adibideak:

- Bezero batek erreserba berri bat sortzea
- Erreserbak irizpide desberdinen arabera kontsultatzea:
  - Eguneko Erreserbak kabinaka bistaratzea:
[Eguneko Erreserbak kabinaka - Desktop](./eguneko-erreserbak-kabinaka-desktop-kasua.md)

## Beste erabilera-kasuak

Langileak desktop aplikazioa erabiltzean hainbat ekintza burutu ditzake:

- Datu-baseko taulak esportatu. Kasu honen azalpen sakonagoa irakurri hurrengo dokumentuan:
[Datu-baseko taulen esportazioa](./datubasea-taulen-esportazioa-kasua.md)
- Datu-baseko taulak inportatu. Kasu honen azalpen sakonagoa irakurri hurrengo dokumentuan:
[Datu-baseko taulen inportazioa](./datubasea-taulen-inportazioa-kasua.md)

---

## Oharrak

- Dokumentu honek ez du negozio-arauen xehetasunik jasotzen.
- Erabilera-kasu berriak sortzen dira sistemaren beharrak hazten diren heinean.
- Zerbitzuen diseinu-irizpideak docs/arkitektura/zerbitzu-geruza.md dokumentuan azaltzen dira.
