# Zerbitzu Geruza (Service Layer)

## 1. Helburua

Zerbitzu geruzaren helburua da aplikazioaren negozio-logika geruza bakar batean zentralizatzea.

Geruza honek honakoak bermatzen ditu:

- Negozio-arauen aplikazioa
- Datu-iraunkortasunaren isolamendua
- transakzioen eta baliabideen kudeaketa

## 2. Kokapena

- Modulua: **garajea-core**
- Mendekotasuna: **garajea-model**

Zerbitzu geruzak ez du web edo interfazearen mendekotasunik.

## 3. Zerbitzuak

DIY Garajea proiektuan, Zerbitzuak ez dira soilik entitateen ordezkari zuzenak; negozioko gaitasunak edo erabilera-kasuak inplementatzen dituzten osagaiak dira.

Adibidez: AutentifikazioZerbitzua, TxostenService, ...

Zerbitzu batek honako galdera honi erantzuten dio:
"Zer egin daiteke sisteman, negozioaren ikuspegitik?"

Adibideak:

- BezeroaService
- LangileaService
- ErreserbaService

Hala ere, proiektua handitu ahala, zeharkako asmoko, edota helburu orokorreko zerbitzuak ager litezke (adib. TxostenService)

Zerbitzuak klase konkretu gisa inplementatzen dira (interfaze + implementazio eredua erabili gabe), hezkuntzako testuinguruan sinpletasuna lehenetsiz.

Zerbitzu bakoitzak:

- Negozio-arauak aplikatzen ditu
- DAOak erabiltzen ditu datuak eskuratzeko
- Ez du Kontrolatzaile edota Bista geruzen erreferentziarik

## 3.1 Zerbitzuak eta erabilera-kasuak

Zerbitzuak erabiltzaileak ezagutzen dituen erabilera-kasuak inplementatzeko erabiltzen dira.

Adibide adierazgarriak:

- Bezero berri bat erregistratzea (BezeroaService)
- Bezero baten profila kargatzea edo eguneratzea (BezeroaService)
- Langile baten saioa hastea (LangileaService)
- Erreserba berri bat sortzea (ErreserbaService)
- Erreserben zerrendak irizpide desberdinen arabera kontsultatzea (ErreserbaService)

Zerrenda hau ez da itxia.
Zerbitzu berriak sortzen dira negozio-kasu berriak agertzen direnean.
Kasu-erabilera hauen ikuspegi orokorra `arkitektura/erabilera-kasuak.md` dokumentuan jasotzen da.

- [erabilera-kasuak](erabilera-kasuak.md)

## 3.2 Zer EZ da Zerbitzu bat

Ez da Zerbitzu bat sortzen honako kasu hauetan:

- Entitate baten CRUD eragiketa hutsak direnean
- Negozio-arau propiorik ez dagoenean
- DAO baten metodoak zuzenean delegatzen direnean

Adibidez, Materiala edo Kabina bezalako entitateek ez dute zertan Zerbitzu propioa izan behar, baldin eta ez badute negozio-logika berezirik.

### 3.3 Zerbitzuak eta Aggregate Root kontzeptua

Zerbitzuak normalean Aggregate Root bati lotuta daude.

Aggregate Root bat da:

- beste entitate (mendeko) batzuk barne hartzen dituen negozio-objektu nagusia
- transakzio-esparrua definitzen duena
- kanpotik zuzenean atzitu daitekeen entitate bakarra

Adibidez:

- Bezeroa Aggregate Root da, eta Ibilgailua entitate mendekoa da
- Erreserba Aggregate Root da, eta Erreserba_Materiala erlazioa haren barruan kudeatzen da

Ondorioz:

- Zerbitzuak Aggregate Root-en arabera diseinatu ohi dira
- Entitate mendekoek ez dute zertan Zerbitzu propioa izan

## 3.4 Geruzen arteko erantzukizunen banaketa (Controller / Service / DAO)

DIY Garajea proiektuan, aplikazioaren geruza nagusiek ardura zehatz eta bereiziak dituzte.

Helburua da:

- arduren nahasketa saihestea
- kodearen irakurgarritasuna handitzea
- negozio-logika isolatzea
- aplikazioaren eboluzioa erraztea

### 3.4.1 Controller

Controller-ak erabakitzen du zer egin nahi den eta zein parametroekin:

- HTTP parametroak parseatzen ditu
- balio lehenetsiak ezartzen ditu
- erabilera-kasu egokia deitzen du
- zerbitzuen arteko deirik EZ du egiten zuzenean

### 3.4.2 Service

Zerbitzu-geruzak erabakitzen du eragiketa hori baliozkoa den eta nola koordinatu behar den:

- negozio-arauak aplikatzen ditu
- (negozio-)balidazioak egiten ditu
- transakzioak definitzen ditu
- DAOen arteko koordinazioa egiten du
- datuak berrantolatu ditzake, erabilera-kasuen arabera (adib. datuak taldekatu)
- ez du HTTP edo UI kontzepturik ezagutzen

### 3.4.3 DAO

DAOek erabakitzen dute datuak nola eskuratu edo gorde behar diren:

- SQL kontsultak definitzen ditu
- datu-basearekiko elkarrekintza burutzen du
- emaitzak normalean entitate, `List<entitate>`, edota DTO bihurtzen ditu
- kasu berezia: datu-atzipenak `<gakoa,balioa>` erlazio naturala sortzen duenean (adib. kontabilitatezko SQL kontsulta bat egitean: SELECT kabina_id, COUNT(*) FROM ERRESERBA GROUP BY kabina_id => `Map<Integer, Integer>`)
- ez du negozio-logikarik aplikatzen
- ez du egiturarik eraikitzen bista edota erabilera-kasu baterako pentsatuta

## 3.5 Zerbitzu geruzaren eta DAOen arteko kontratuak

Zerbitzu geruzak DAOen bidez eskuratutako datuetan
zenbait inbariante betetzen direla suposatzen da.

Bereziki:

- Erreserbak itzultzen dituzten DAO metodoek erreserba-zerrendak kronologikoki ordenatuta ematen dituzte (hasiera dataren arabera).
- Ordenazio hau domeinuko inbariante bat da:
  erreserba-zerrenda batek zentzua du soilik denbora-ordenean.
- Ondorioz, zerbitzu-geruzak ez du berriro ordenatzeko logikarik gehitzen, eta DAOaren kontratu hori oinarri gisa erabiltzen du.

Honek logika errepikapena saihesten du eta zerbitzuen kodea sinpleagoa eta adierazgarriagoa bihurtzen du.

## 3.6 Mendekotasunen Injekzioa eta Inbertsioaren Printzipioa (DIP)

### 3.6.1 Mendekotasun-Inbertsioaren Printzipioa (Dependency Inversion Principle)

Mendekotasun-Inbertsioaren Printzipioak honakoa dio:
> Goi-mailako moduluak ez dira behe-mailako moduluen mende egon behar.
> Biak abstrakzioen mende egon behar dira.

DIY Garajea proiektuan, honek esan nahi du:

- Zerbitzu-geruza (goi-mailako modulua) ez dela datu-atzipenaren inplementazio zehatzen mende egon behar.
- Zerbitzuek interfazeetan (DAO interfazeak) oinarritzen dutela beren logika.
- Inplementazio zehatza zerbitzua sortzean (eraikitzailean) injektatzen dela.

### 3.6.2 Mendekotasunen Injekzioa (Dependency Injection)

Mendekotasunen injekzioa printzipio hau aplikatzeko mekanismo praktikoa da.

Zerbitzuek ez dituzte beren mendekotasunak sortzen; kanpotik jasotzen dituzte, eraikitzailearen bidez.

Adibidea:

```java
public ErreserbaService(ErreserbaDAO erreserbaDAO,
                        KonfigurazioaService konfigurazioaService) {
    this.erreserbaDAO = erreserbaDAO;
    this.konfigurazioaService = konfigurazioaService;
}
```

Horrela, zerbitzuaren mendekotasunak esplizituak eta egiaztagarriak dira.

## 4. ServiceContext kontzeptua

### 4.1 Zer da ServiceContext?

`ServiceContext` **zerbitzuen esparrua (edo testuingurua)** da.

`ServiceContext`-en definizioa gehiago zehaztuz:

> Kontrolatzaile batek erabilera-kasu bat exekutatzeko irekitzen duen
> **zerbitzuen erabilera-esparru kontrolatua**.

Esparru kontrolatua dela zehazki esan nahi du:

- kudeatua: norbaitek (`ServiceContext`-ek) kudeatzen duela,
- mugatua: esparru hori mugatua dago denboran (try-with-resources bitartez hasi eta bukatu egiten da)

Zerbitzuen esparru hori sortzean:

- behar diren baliabide teknikoak jasotzen ditu (Mendekotasunen Injekzioa)
- zerbitzu guztiak sortzeko gaitasuna ematen du (eskaeraren arabera, ez guztiak batera)
- baliabide horien bizi-zikloa modu bateratuan kudeatzen du



#### Fluxu kontzeptuala

```text
HTTP Request (web) / UI Ekintza (desktop)
 └── Controller
       └── ServiceContextFactory.open()
             └── ServiceContext
                   ├── DAOFactory
                   └── KonfigurazioaService
```

Beraz, `ServiceContext` objektuak (web HTTP) eskaera edota (desktop) UI Ekintza baten bizi-zikloa antolatzen du.

### 4.2 Zer dauka barruan?

`ServiceContext` bat sortzen denean, barnean honako elementuak ditu:

- `DAOFactory` (konexio eta DAO-en sorkuntza kudeatzeko)
- `KonfigurazioaService` (aplikazio-mailako negozio-konfigurazioa partekatzeko)
- Zerbitzuen instantziak sortzeko metodoak, eskaeraren arabera, eta get___Service metodoen bidez

`ServiceContext`-ek **ez ditu zerbitzu guztiak aldez aurretik sortzen**;
zerbitzuak eskatzen direnean instanziatzen dira.

### 4.3 Non eta noiz sortzen da?

- Kontrolatzaile-mailan sortzen da
- `ServiceContextFactory.open()` metodoaren bidez sortzen da
- Kontrolatzaileek `ServiceContext` bakarra erabiltzen dute (web) HTTP eskaera / (desktop) UI ekintza bakoitzeko
- `ServiceContext`-en barnean erabilera-kasua exekutatzen da

### 4.4 Harremana (web) Servlet ingurunearekin

Web aplikazioan:

- `ServiceContextFactory` aplikazioaren hasieran sortzen da
- `ServletContext`-ean `ServiceContextFactory` hau gordetzen da, eskuragarri azpiegitura-objektu gisa
- (Servlet) Kontrolatzaile bakoitzak fabrikari `open()` deia egiten dio eskaera bakoitzeko `ServiceContextFactory.open()`

Oso garrantzitsua:

- `ServiceContext` **ez da** `ServletContext`
- `ServiceContext` **ez da** `HttpServletRequest`
- `ServiceContext` web ingurunetik independentea den kontzeptua da

Servlet ingurunea (`ServletContext`) soilik erabiltzen da `ServiceContextFactory` eskuratzeko leku edo edukiontzi gisa.

### 4.4 Zergatik erabiltzen da? Zer arazo konpontzen ditu?

- Printzipioz, datu-base konexio bakarra egiteko (web HTTP) eskaera edota (desktop) UI Ekintza bakoitzeko
- Baliabideen itxiera automatikoa egiteko; baliabide teknikoak (konexioak) kontrolpean daude
- Eragiketa transakzionala burutzeko konexio gehigarri bat sortzen da, (normalean) oso bizi laburrekoa
- Zerbitzuei mendekotasunak modu esplizituan injektatzea
- Zerbitzuen sorkuntza zentralizatua
- Kontrolatzaileak JDBC edo DAO xehetasunetatik isolatzea

## 5. Zer da ServiceContextFactory?

- `ServiceContextFactory` aplikazioaren hasieran sortzen da
- (web edota desktop) aplikazioaren esparruan gordetzen da, eta aplikazioaren bizi-zikloan eskuragarri dago
- `ServiceContextFactory`-k `ServiceContext` objektuak sortzeko ardura du
- Kontrolatzaileek JDBC edo DAO inplementazioak ezagutzen ez dituztenez, fabrika honen bidez bakarrik erabili ditzakete zerbitzuak
- Gainera, `InfraConfig` (datu-baseko konfigurazio-parametroak) eta KonfigurazioaService negozio-logikako parametroak (adib. DIY Garajean, lanaldi_hasiera, eta lanaldi_bukaera) gordetzen ditu, `ServiceContext` instantzia bakoitzari injektatuz

## 6. try-with-resources eredua

Kontrolatzaileek honela erabiltzen dute try-with-resources:

```java
try (ServiceContext zerbitzuEsparrua = ServiceContextFactory.open()) {
    zerbitzuEsparrua.getSomeService().metodoa();
}
```

### 6.1 try-with-resources ereduaren fluxua

```text
Controller
 └── ServiceContextFactory
        └── open()
             ├── new JDBCDAOFactory(infra)
             └── new JDBCServiceContext(
                    DAOFactory,
                    konfigurazioaService
                )
```

### 6.2 try-with-resources ereduaren abantailak

Eredu honek honako hauek bermatzen ditu:

- baliabideak beti ixten direla
- errore teknikoak ez direla isiltzen (baizik eta propagatzen dira)

## 7. Salbuespenak zerbitzu geruzan

Zerbitzu geruzak `ZerbitzuSalbuespena` erabiltzen du negozio-erroreak adierazteko.

Ez dira erabiltzen:

- HTTP egoera-kodeak
- JSP bistak
- mezu tekniko gordinak (raw)

Errore teknikoak (SQL, baliabideak ixtea) RuntimeException gisa igotzen dira,
eta web moduluko filtroak (WebFilter) kudeatzen ditu.

## 8. Laburpena

Zerbitzu geruzak DIY Garajea proiektuan negozioaren nukleo argia eta isolatua eskaintzen du, arkitektura garbiarekin eta hezkuntza-arlora zuzendua.
