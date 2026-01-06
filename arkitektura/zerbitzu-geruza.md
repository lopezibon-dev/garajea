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

Zerbitzuak klase konkretu gisa inplementatzen dira
(interfaze + implementazio eredua erabili gabe),
hezkuntza arloan sinpletasuna lehenetsiz.

Adibideak:

- BezeroaService
- LangileaService
- ErreserbaService

Zerbitzu bakoitzak:

- Negozio-arauak aplikatzen ditu
- DAOak erabiltzen ditu datuak eskuratzeko
- Ez du Kontrolatzaile edota Bista geruzen erreferentziarik

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
arkitektura garbi eta hezkuntza-arlora zuzendua.
