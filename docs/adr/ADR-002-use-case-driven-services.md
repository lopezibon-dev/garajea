# ADR-002: Zerbitzuak erabilera-kasuen arabera diseinatzea

## Egoera

Onartua.

## Testuingurua

Zerbitzu Geruza definitzean, honako diseinu-aukerak aztertu dira:

- Zerbitzu bat sortzea entitate bakoitzeko (CRUD-eredua)
- Zerbitzuak negozioko erabilera-kasuen arabera diseinatzea

Entitateetan oinarritutako Zerbitzuen ikuspegia
sinplea izan daiteke hasieran, baina honako arazoak sortzen ditu:

- Zerbitzu hutsak (DAO delegatzaileak)
- Negozio-logika sakabanatzea
- Transakzio-mugen definizio lausoa

Proiektuaren helburua ez da negozio-logika exhaustiboa,
baizik eta arkitektura-eredu egokiak ikastea eta aplikatzea.

## Erabakia

Zerbitzuak **erabiltzaileak ezagutzen dituen erabilera-kasuen**
arabera diseinatzea erabaki da, eta ez entitateen arabera soilik.

Horren ondorioz:

- Zerbitzu batek sistemaren gaitasun bat ordezkatzen du
- Zerbitzuak normalean Aggregate Root bati lotuta daude
- Entitate mendekoek ez dute zertan Zerbitzu propioa izan
- Zerbitzuak transakzio-muga naturalak dira

Adibidez:

- BezeroaService bezeroarekin lotutako erabilera-kasuak biltzen ditu
- ErreserbaService erreserben sorkuntza eta kudeaketa koordinatzen du

## Ondorioak

Diseinu honek honako abantailak eskaintzen ditu:

- Negozio-logika koherenteagoa eta adierazgarriagoa
- Zerbitzu gutxiago baina zentzuzkoagoak
- Transakzioen kudeaketa argiagoa
- Aggregate Root kontzeptuarekin bateragarritasuna

Aldi berean, Zerbitzuen diseinuak
pentsamendu kontzeptual handiagoa eskatzen du,
baina hori proiektuaren helburu didaktikoarekin bat dator.
