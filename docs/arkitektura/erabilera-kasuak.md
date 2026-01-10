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
- Langile batek erreserba bat sortzea
- Erreserbak irizpide desberdinen arabera kontsultatzea
- Erreserba baten egoera eguneratzea

---

## Oharrak

- Dokumentu honek ez du negozio-arauen xehetasunik jasotzen.
- Erabilera-kasu berriak sortzen dira sistemaren beharrak hazten diren heinean.
- Zerbitzuen diseinu-irizpideak arkitektura/zerbitzu-geruza.md dokumentuan azaltzen dira.
