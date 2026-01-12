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

Zerbitzu batek honako galdera honi erantzuten dio:
"Zer egin daiteke sisteman, negozioaren ikuspegitik?"

Adibideak:

- BezeroaService
- LangileaService
- ErreserbaService

Eta hala ere, proiektua handitu ahala, zeharkako asmoko, edota helburu orokorreko zerbitzuak ager litezke (adib. TxostenService)

Zerbitzuak klase konkretu gisa inplementatzen dira (interfaze + implementazio eredua erabili gabe), hezkuntza arloan sinpletasuna lehenetsiz.

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

Adibidez, Materiala edo Kabina bezalako entitateek ez dute zertan Zerbitzu propioa izan, baldin eta ez badute negozio-logika berezirik.

### 3.3 Zerbitzuak eta Aggregate Root kontzeptua

Zerbitzuak normalean Aggregate Root bati lotuta daude.

Aggregate Root bat da:

- beste entitate batzuk barne hartzen dituen negozio-objektu nagusia
- transakzio-esparrua definitzen duena
- kanpotik zuzenean atzitu daitekeen entitate bakarra

Adibidez:

- Bezeroa Aggregate Root da, eta Ibilgailua entitate mendekoa da
- Erreserba Aggregate Root da, eta Erreserba_Materiala erlazioa haren barruan kudeatzen da

Ondorioz:

- Zerbitzuak Aggregate Root-aren mailan diseinatzen dira
- Entitate mendekoek ez dute zertan Zerbitzu propioa izan

## 3.4 Zerbitzu geruzaren eta DAOen arteko kontratuak

Zerbitzu geruzak DAOen bidez eskuratutako datuetan
zenbait inbariante betetzen direla suposatzen da.

Bereziki:

- Erreserbak itzultzen dituzten DAO metodoek erreserba-zerrendak kronologikoki ordenatuta ematen dituzte (hasiera dataren arabera).
- Ordenazio hau domeinuko inbariante bat da:
  erreserba-zerrenda batek zentzua du soilik denbora-ordenan.
- Ondorioz, zerbitzu-geruzak ez du berriro ordenatzeko logikarik gehitzen,
  eta DAOaren kontratu hori oinarri gisa erabiltzen du.

Honek logika errepikapena saihesten du
eta zerbitzuen kodea sinpleagoa eta adierazgarriagoa bihurtzen du.

## 3.5 Geruzen arteko erantzukizunen banaketa (Controller / Service / DAO)

DIY Garajea proiektuan, aplikazioaren geruza nagusiek ardura zehatz eta bereiziak dituzte.

Helburua da:

- arduren nahasketa saihestea
- kodearen irakurgarritasuna handitzea
- negozio-logika isolatzea
- aplikazioaren eboluzioa erraztea

### 3.5.1 Controller

Controller-ak erabakitzen du zer egin nahi den eta zein parametroekin:

- HTTP parametroak parseatzen ditu
- balio lehenetsiak ezartzen ditu
- erabilera-kasu egokia deitzen du
- zerbitzuen arteko deirik EZ du egiten zuzenean

### 3.5.2 Service

Zerbitzu-geruzak erabakitzen du eragiketa hori baliozkoa den eta nola koordinatu behar den:

- negozio-arauak aplikatzen ditu
- (negozio-)balidazioak egiten ditu
- transakzioak definitzen ditu
- DAOen arteko koordinazioa egiten du
- ez du HTTP edo UI kontzepturik ezagutzen

### 3.5.3 DAO

DAOek erabakitzen dute datuak nola eskuratu edo gorde behar diren:

- SQL kontsultak definitzen ditu
- datu-basearekiko elkarrekintza burutzen du
- emaitzak entitate edo DTO bihurtzen ditu
- ez du negozio-logikarik aplikatzen

## 4. ServiceContext kontzeptua

### 4.1 Zer da ServiceContext?

ServiceContext objektuak eskaera (request) baten bizi-zikloa antolatzen du.

Barnean honako hauek ditu:

- DAOFactory
- Zerbitzuen instantziak
- KonfigurazioaService

Kontrolatzaileek ServiceContext bakarra erabiltzen dute
HTTP eskaera bakoitzeko.

### 4.2 Fluxu kontzeptuala

Request
 └── Controller
       └── ServiceContextFactory.open()
             └── ServiceContext
                   ├── DAOFactory
                   └── KonfigurazioaService

### 4.3 Zergatik erabiltzen da?

- Printzipioz, datu-base konexio bakarra request bakoitzeko
- Eragiketa transakzionala burutzeko konexio gehigarri bat sortzen da, oso bizi laburrekoa
- Baliabideen itxiera automatikoa
- Zerbitzuen sorkuntza zentralizatua

## 5. ServiceContextFactory

ServiceContextFactory-k ServiceContext objektuak sortzeko ardura du.

Kontrolatzaileek ez dute JDBC edo DAO inplementazioak ezagutzen;
fabrika honen bidez bakarrik irekitzen dute testuingurua.

## 6. try-with-resources eredua

Kontrolatzaileek honela erabiltzen dute:

```java
try (ServiceContext ctx = scf.open()) {
    ctx.getSomeService().metodoa();
}
```

### 6.1 Inplementazioaren ikuspegia

Controller
 └── ServiceContextFactory
        └── open()
             ├── new JDBCDAOFactory(infra)
             └── new JDBCServiceContext(
                    daoFactory,
                    konfigurazioaService
                )

### 6.2 Abantailak

Eredu honek honako hauek bermatzen ditu:

- baliabideak beti ixten direla
- errore teknikoak ez direla isiltzen
- try-with-resources erabilera segurua

## 7. Salbuespenak zerbitzu geruzan

Zerbitzu geruzak ZerbitzuSalbuespena erabiltzen du negozio-erroreak adierazteko.

Ez dira erabiltzen:

- HTTP egoera-kodeak
- JSP bistak
- mezu tekniko gordinak

Errore teknikoak (SQL, baliabideak ixtea)
RuntimeException gisa igotzen dira,
eta web moduluko filtroak kudeatzen ditu.

## 8. Laburpena

Zerbitzu geruzak DIY Garajea proiektuan
negozioaren nukleo argia eta isolatua eskaintzen du,
arkitektura garbiarekin eta hezkuntza-arlora zuzendua.
