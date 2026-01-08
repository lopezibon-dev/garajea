# Bootstrap eta aplikazioaren hasieratzea

## 1. Helburua

Dokumentu honen helburua da DIY Garajea proiektuan
**bootstrap fasea** nola antolatzen den azaltzea,
bai web aplikazioan bai desktop aplikazioan.

Bootstrap fasea aplikazioa martxan jartzean
exekutatzen den hasieratze-prozesua da,
eta aplikazioaren bizi-ziklo osoan
partekatuko diren baliabideak prestatzen ditu.

Dokumentu honek ez du MVC eredua azaltzen,
baizik eta **aplikazioaren hasieratze-arkitektura**.

---

## 2. Bootstrap kontzeptua

Bootstrap fasearen helburu nagusiak hauek dira:

- Azpiegitura-parametroak kargatzea
- Negozio-konfigurazioa prestatzea
- `ServiceContextFactory` sortzea
- Aplikazio-mailako objektuak eskuragarri uztea

Printzipio nagusiak:

- Bootstrap logika exekuzio-ingurunearen araberakoa da
- Negozio-logika ez da bootstrap-ean kokatzen
- Behin exekutatzen da aplikazioaren hasieran

---

## 3. Web aplikazioko bootstrap-a

### 3.1 Exekuzio-ingurunea

Web aplikazioa servlet edukitzaile batean exekutatzen da
(adib. Tomcat).

Edukitzaileak aplikazioaren bizi-zikloa kontrolatzen du
eta hasieran callback bat eskaintzen du:
`ServletContextListener`.

### 3.2 Inplementazioa

Web bootstrap-a `WebAppInitializer` klasean
inplementatzen da.

Hasieratze-fluxua:

```text
Application startup
 └── WebAppInitializer (ServletContextListener)
       ├── InfraConfig kargatu
       ├── KonfigurazioaService kargatu
       └── ServiceContextFactory sortu
              └── ServletContext-en gorde
```

### 3.3 ServiceContextFactory-ren erabilera

`ServiceContextFactory` aplikazio-mailakoa da
eta `ServletContext`-ean gordetzen da.

Controller-ek honela eskuratzen dute:

```java
ServiceContextFactory scf =
    (ServiceContextFactory) getServletContext()
        .getAttribute("serviceContextFactory");
```

Ondoren, HTTP eskaera bakoitzeko:

```java
try (ServiceContext sc = scf.open()) {
    // Zerbitzu-deiak
}
```

---

## 4. Desktop aplikazioko bootstrap-a

### 4.1 Exekuzio-ingurunea

Desktop aplikazioan:

- Ez dago servlet edukitzailerik
- Ez dago `ServletContext`-ik
- Aplikazioa bera da exekuzio-ingurunearen jabea

Horregatik, bootstrap logika
`main()` metodotik abiatzen da.

### 4.2 Inplementazioa

Desktop bootstrap-a klase dedikatu batean
inplementatzen da: `DesktopAppBootstrap`.

Hasieratze-fluxua:

```text
Application startup
 └── DesktopAppBootstrap
       ├── InfraConfig kargatu
       ├── KonfigurazioaService kargatu
       └── ServiceContextFactory sortu
              └── singleton gisa mantendu
```

### 4.3 KonfigurazioaService-ren eskurapena (Desktop)

Desktop aplikazioan, `KonfigurazioaService` **bootstrap fasean sortzen da**
eta `ServiceContextFactory`-ri injektatzen zaio.

UI geruzak (Swing):

- **ez du inoiz** `KonfigurazioaService` zuzenean instantziatzen
- **ez du inplementazio konkreturik ezagutzen**
- beti `ServiceContext`-etik eskuratzen du

Horren arrazoia da:

- konfigurazioa aplikazio-mailakoa delako
- web eta desktop arkitekturen arteko koherentzia mantentzeko
- konfigurazioaren instantzia bakarra (singleton logikoa) bermatzeko

Patroi zuzena honakoa da:

UI ekintza
└── ServiceContextFactory.open()
    └── ServiceContext
        └── KonfigurazioaService

Adibidez, agenda sortzeko:

```java
try (ServiceContext sc =
         DesktopAppBootstrap.getServiceContextFactory().open()) {

    ErreserbaAgendaBuilder builder =
        new ErreserbaAgendaBuilder(
            sc.getKonfigurazioaService()
        );

    ...
}
```

Horrela, desktop aplikazioak web aplikazioaren bootstrap eredua
errespetatzen du, exekuzio-ingurunea desberdina izan arren.

### 4.4 ServiceContextFactory-ren erabilera

Desktop aplikazioan `ServiceContextFactory`:

- Behin sortzen da
- Aplikazio osoan partekatzen da
- Singleton gisa eskuratzen da

Controller-ek honela erabiltzen dute:

```java
try (ServiceContext sc =
        DesktopAppBootstrap
            .getServiceContextFactory()
            .open()) {

    // Zerbitzu-deiak
}
```

---

## 5. ServiceContext-en bizi-zikloa

Web eta desktop esparruetan
printzipio bera aplikatzen da:

- `ServiceContextFactory` → aplikazio-mailakoa
- `ServiceContext` → eskaera edo ekintza bakoitzeko

Konparaketa:

|       Kontzeptua       |    Web             |      Desktop       |
|------------------------|--------------------|--------------------|
| Ekintza-unitatea       | HTTP eskaera       | UI ekintza         |
| ServiceContext sorrera | request bakoitzean | ekintza bakoitzean |
| Baliabideen askapena   | request amaieran   | ekintza amaieran   |

Bi kasuetan:

- `try-with-resources` eredua erabiltzen da
- Datu-base konexioak automatikoki ixten dira

---

## 6. Diseinuaren justifikazioa

Diseinu honek honako abantailak ditu:

- Web eta desktop kanalek arkitektura koherentea dute
- Negozio-logika kanal independentea da
- Testagarritasuna errazten da
- Etorkizuneko kanal berriak (CLI, API) erraz gehitu daitezke

Singleton eredua desktop-ean
`ServletContext`-aren baliokidea da
eta ez da anti-patroi gisa ulertu behar
testuinguru honetan.

---

## 7. Laburpena

DIY Garajea proiektuan bootstrap faseak:

- Aplikazioaren hasieratzea zentralizatzen du
- Azpiegitura eta konfigurazioa prestatzen ditu
- `ServiceContextFactory` eskuragarri uzten du

Web eta desktop inplementazioak
exekuzio-ingurunearen araberakoak dira,
baina oinarri arkitektoniko bera partekatzen dute.
