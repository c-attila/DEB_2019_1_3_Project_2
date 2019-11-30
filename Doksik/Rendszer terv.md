#  Rendszerterv

## 1. A rendszer
	A program egy olyan stratégiai játék, amelynek segítségével a megrendelő fel 
tudja mérni a partnereinek döntéshozatali és stratégiai készségeit.

## 2. A rendszer célja
	A rendszer célja a vállalat partnereinek az igényeinek a felmérése illetve 
gondolkodásmódjuk feltérképezése. Ebben segít ez a rendszer, ami nem mást mint 
egy stratégiai játék. Ezzel a stratégiai játékkal szeretnénk felmérni az emberek 
döntéshozó képersségét, illetve stratégiai technikáját, gondolkodásmódját. Az így
megszerzett adatok segítségével a felhaszálókhoz a rájuk ható legjobb hírdetéséket
lehet eljuttatni.

## 3. Hogyan: a rendszer terve

### 3.1. Használt eszközök

- Programozási nyelv: Java
- Platform: Android
- Megjelenítés: XML
- Adatbázis: MySQL, Java Persistence API, SQLite

### 3.2. A program részletes működése

#### 3.2.1. Menük
	A játék főmenüjében navigálhatunk a játék egyes funckiói között. Egyik menüben 
megnézhetjük a már elért eredményeinket, illetve a különböző ranglistákat. Egy 
másik menüben kiválaszthatjuk, hogy milyen játékmódban szeretnénk játszani. Ezek 
lehetnek a következők: pontra menő és időre menő játékmódok. Ezen kívül ugyan itt
tudnák a játékosok kiválasztani az egyes változtatható paraméterek nagyságát.
Például, hogy mekkora legyen a bábuk látómezeje, a tábla mérete, az időre menő 
mód esetében a játékidő vagy a pontra menő esetében a pontkülönbség mértéke.
Ezeket a Java és XML-ek segítségével fogjuk megvalósítani. A ranglistákat és 
eredményeket megjelenítő menüben az adatokat egy adatbázisból fogjuk lekérni, 
valamint a lokális SQLite adatbázist is segítségül fogjuk hívni.
A játékmódot kiválasztó menüben a bekért paraméterek alapján fog a játék üzleti 
logikája futni.

#### 3.2.2. Játékmódok
	A játék alapvetően két játékmódot tartalmaz. Az egyik a pontra menő, a másik 
pedig időre menő játékmód. A pontra menő játékmód akkor ér véget, ha az egyik 
játékosnak bizonyos mennyiséggel több pontja van az ellenfelénél. Ez lehet 
bizonyos ponttal több, vagy valamennyivel többszöröse is. Az alapértelmezett érték 
az ellenfél pontjának kétszerese. Az időre menő játékmód értelemszerűen akkor 
ér véget, ha lejárt az idő. Mindkét mód esetében, ha elfogyik az összes 
megvásárolható mező, akkor a legtöbb ponttal rendelkező játékos nyer.
Gyakorlatban a játékmód kiválasztását úgy alkalmazzuk, hogy a kiválasztott 
módnak megfelelően fut le a program üzleti logikája. A mezőkbe bekért értékeket 
az üzleti logika átveszi és annak megfelelően használja fel, hogy a játékos 
milyen játékmódot választott ki a menüben.

#### 3.2.3. Játékmenet
	A játékmódok kiválasztása után elkezdődik a tényleges játék. Ekkor leképeződik 
az n-szer m-es tábla, aminek a paramétereit a játékosok választották ki. A 
mezőkön lévő megvásárolható javak és azoknak értékei (áraik, körönkénti 
profitjaik) random generálódnak le, így izgalmasabbá és változatosabbá téve a 
játékot. Ezeket a mezőket egy 2 dimenziós array-ben tároljuk és egy Mező osztályt 
használunk hozzá. Megjelenítéshez grid-eket fogunk használni.
A Mező osztálya: tartalmazni fog egy string-et, ami a mező, megvásárolható tulajdon 
neve, illetve két egész számot, ami a mező ára és körönkénti profitja. A táblán 
megjelenhetnek "kérdőjel mezők", amik meglepetést tartogathatnak. Ha a játékos 
rálép, meg kell vegye az ott kínált tulajdont, de kaphat valamiféle büntetést 
is, ami negatív hatással lehet a játékosra.
A Játékos osztálya: Két int-et tartalmaz, ami meghatározza, hogy hol található 
a táblán, egy másikat ami tárolja a jelenlegi vagyonát, illetve egy olyat, ami 
tárolja, hogy jelenleg mennyi körönkénti profitot kap. Az utóbbi kettő értéket 
a tábla mellett jelenítjük meg text formában. Ezen kívül a játékos kap még egy 
listát, amiben tároljuk, hogy melyik mezőket birtokolja.
Egy mezőre rálépve, ha azt még nem birtokolja senki, a játékos választhat, hogy 
megveszi-e azt a mezőt, vagy sem. Ha az ellenfél birtokolja, kötelezően ki kell 
fizesse az arra a mezőre vonatkozó körönkénti profitot. Ha ezt nem tudja, 
csődbe megy és az ellenfél nyert.

### 3.3. Architekturális terv

#### 3.3.1. Az alkalmazás rétegei, fő komponensei, ezek kapcsolatai
    Az alkalmazás három rétegből áll: adatbázis-réteg, üzleti logikai réteg és 
grafikus felület. Ezeket az MVC tervezési minta alapján kapcsoljuk össze, bár 
ezt egy Androidos környezeten implementálva. A grafikus felület segítségével 
vehetjük igénybe az üzleti logika egyes funkcióit. Ez kezeli az adatbázis-
réteget is.

#### 3.3.2. Változások kezelése
	A program egyes részeit úgy írjuk meg, hogy azok paramétereit és 
függvényeit könnyen és gyorsan meg tudjuk változtatni, ha szükséges. Így a 
megrendelő is rugalmasan beleszólhat az alkalmazás fejlesztésébe.

#### 3.3.3. A rendszer bővíthetősége
	A rendszert úgy építjük fel, hogy könnyen bővíthetőek legyen a funkciói és, 
hogy akár teljesen új funkciókat is hozzá lehessen adni. Ezeket a szempontokat 
figyelembe véve valósítjuk meg a rendszer mind a három rétegét.

#### 3.3.4. Biztonsági funkciók
	A program alapvető biztonsági funkciókkal rendelkezik, tehát nem írhatjuk 
felül más eredményeit, nem szemetelhetjük (spamelhetjük) tele az adatbázist 
és nem csalhatunk a játékmenetben sem (nincsenek exploitok, buggok).

### 3.4. Implementációs terv

#### 3.4.1. Perzisztencia-osztályok
	A perzisztencia-kezeléshez használt osztályok olyan osztályok lesznek, 
melyek a játékosok eredményeit és a ranglistákat reprezentálják.

#### 3.4.2. Üzleti logika osztályai
	Az üzleti logikának olyan osztályai lesznek, amelyek reprezentálják a 
játékost, a mezőt és az egyéb megjelenő objektumokat.

#### 3.5. Karbantartási terv
	A program karbantartása a tervezésből kifolyólag egyszerű lesz és a 
fejlesztőcsapat 5 évig vállalja ennek kivitelezését.

## 4. Ütemterv:

- 2019.11.10 - A dokumentáció és prototípus befejezése.
- 2019.11.11 - A fejlesztés megkezdése. Grafikus felület kialakítása. Egyszerúbb 
funkciók kifejlesztése.
- 2019.11.18 - Az összetettebb funkciók fejlesztése.
- 2019.12.02 - Tesztelés megkezdése, az esetleges hibák javítása.
- 2019.12.08 - A projekt véglegesítésének céldátuma.

#### Mérföldkövek: 
- A dokumentáció véglegesítése.
- A prototípus elkészítése.
- A fejlesztés megkezdése.
- A grafikus felület kialakítása.
- Az egyszerűbb funkciók kifejlesztése.
- A bonyolultabb funkciók kifejlesztése.
- Tesztelés, illetve a felfedezett hibák kijavítása.
- A projekt véglegesítése.

#### Projektszerepkörök és felelősségek:
Front-end fejlesztő: Felelőssége a játék vizuális megjelenítésének a fejlesztése
Back-end fejlesztő: Felelőssége a játék funkcióinak, logikájának a fejlesztése,
az adatbázis kezelése.

#### Projektmunkások és felelősségeik:
Mándrik Alex András: az üzleti logika fejlesztése, illetve a vizuális megjelenítés.
Cornea Attila: az adatbázis kezelése, illetve az üzleti logika fejlesztése.
Murvai András: az üzleti logika fejlesztése
Kovács Attila: az üzleti logika fejlesztése

## 5. Források
	A projektre olyan forrásokat fogunk alokálni, amiket a megrendelőtől kaptunk 
a program elkészítésére előlegként, illetve amiketünk a cég erre a célra tart fenn.

## 6. Üzleti folyamatok modellje
Üzleti szereplők: játékosok.

**Üzleti folyamatok:**
- Játékmód beállítása, kiválasztása - Több játékmód áll rendelkezésre, hogy mindenki
megtalálja a számára megfelelőt. Ezek a játékmódok különböző hosszúságúak és nehézségűek.
Egyedi játékmód is beállítható.
- Lépésszám meghatározása - A lépések számának meghatározása valamilyen módszerrel 
(pl. dobókockával).
- Lépés - Lépésszámnyi lépés megtétele a mezőkön.
- Mező megvásárlása vagy hanyagolása - Annak eldöntése, hogy a játékos meg akarja-e
vásárolni a mezőt, vagy sem.
- Egyenleg újraszámolása körönként - Minden kör elején az egyenleg újraszámolásra
kerül, ugyanis minden játékosnak van egy körönkénti profitja, ami minden körben
hozzá kell adódjon az egyenleghez.
- Egyenleg újraszámolása vásárláskor - Vásárláskor a vásárlás költségét ki kell vonni 
az egyenlegből.
- A játék végének a megállapítása - A játék végének meghatározása játékmódtól függően.
- Rangsor felállítása - A rangsor felállítása a játék végére elért egyenleg alapján.

#### Követelmények:
**Funkcionális és nemfunkcionális követelmények:** 
  - A menüelemek a megfelelő oldalra irányítsanak.
  - Ha a játék a háttérbe kerül, akkor a játék állása kerüljön mentésre, hogy 
amikor újból a játék kerül előtérbe, lehessen folytatni a játékot.
  - Tökéletes szinkronban kell lennie a játékosoknak.
  - A játékos számára csak azon funkciók legyenek elérhetőek, melyek a játék jelenlegi
szakaszában elérhetőek.
  - Ha a játékosnak nem elég az egyenlege a vásárláshoz, akkor ne tudjon 
vásárolni.
  - Több játékmódnak kell lennie, hogy mindenki megtalálja a neki megfelelőt. Illetve 
egy egyedi játékmód beállítás is legyen, ha valaki szeretne saját beállításokkal 
játszani.
  - A lépések számának meghatározása a pálya méretéhez képest reális kell legyen.
  - A lépés funkciónál a visszalépés lehetősége beállítható legyen. Pl. csak az
előzőre ne lehessen visszalépni, vagy egyik bejárt mezőre se lehessen lépni.
  - A vásárlás lehetősége eldönthető kell legyen. Illetve ha a játékos olyan mezőre
lép, amely valamilyen büntetést, negatív dolgot takar, akkor automatikus végrehajtás
kell érvénybe lépjen.
  - Az egyenleg újraszámolás pontos kell legyen. Minden játékos egyenlege az aktuális
profitjával kell növekedjen minden kör elején. Illetve vásárláskor az egyenlegből
rögtön levonásra kell kerüljön a vásárlás költsége.
  - A játék végének meghatározása igazságos kell legyen. Meg kell találni az igazságos
nyerési szabályt. Pl. akkor nyer valaki, ha az ellenfelénél x-szer nagyobb egyenlege 
van. Vagy akkor nyer, ha lejárt az idő.
  - A ranglista felállítása játékmódonként kell történjek, hogy igazságos legyen.

## 7. Adatbázis terv

### 7.1. Logikai adatmodell
	Az eredmények tárolására egy adatbázist használunk, amihez a remotemysql.com 
szolgáltatását fogjuk igénybe venni. Továbbá az adatok tárolásához az OrmLite-ot 
használjuk. Ezt az adatbázist használjuk arra, hogy a játékosok eredményeit 
lementsük és arra is, hogy a ranglistát és más eredményeket megjelenítsünk. A 
játszma végén mindig mentésre kerül a játékos ideje és pontszáma egy táblába és 
megjelenítéskor ugyan ebből a táblából kérdezzük vissza.

### 7.2. Tárolt eljárások
	Az adatbázis eléréséhez főleg Object-Relational Mapping-et fogunk használni, 
ezért SQL eljárásokra kevesebb szükség lesz. Tárolt eljárásként azokat a 
szkripteket fogjuk használni, amik az adatbázis-tábla esetleges újragenerálásához
szükségesek.

### 7.3. Fizikai adatmodellt legeneráló SQL szkript
	A fizikai adatmodellt egy pár soros SQL szkripttel fogjuk legenerálni egy 
remotemysql.com-os adatbázisra. Erre remélhetőleg csak egyszer lesz szükség.

## 8. Tesztterv
	A program kisebb metódusait, funkcióit unit test-ekkel fogjuk tesztelni, 
az alkalmazás nagyobb funkcióit pedig kézzel. Így, azt is kiszurjuk, ha 
valamilyen szemantikai, matematikai hiba van a programban, de azt is, ha emberi 
vagy tervezési hiba.

## 9. Telepítési terv

	A program esetében a letöltés a Google Playről lesz megoldható. Mivel az egyik 
csapattagunknak van Google Developer Accountja, amit néhány évvel ezelőtt vásárolt 
meg, ezért ez nem jelent gondot. Onnantól kezdve az áruházból egyetlen kattintással 
lehet letölteni az alkalmazás, és magától telepítődik is. Követelmény szempontjából 
a minimum az Androis 4.4-es verziója, ennél magasabb verziószámok esetén is gond 
nélkül futtatható az alkalmazás. 

## 10. Funkcionális terv

- Rendszerszereplők: Az applikáció, ami egy játék az internetről lesz letölthető, 
tehát elsősorban az ilyen stratégiai játék szeretők a célközönség. 
    
- Rendszerhasználati esetek és lefutásaik: Ha megnyitjuk az alkalmazást akkor a 
kezdőképernyő fogadja a felhasználót. Innentől kezdve választhatunk a különbötő 
menüpontok közül, amik a Játék és a Beállítások. Később különböző játékmódok közül 
tudunk majd választani. Ha kiválasztnuk egy manüpontot, akkor az alkalmazás arra a 
fragmensre vált. 

- Ha a Játék gombra kattintunk, akkor tudjuk a játékmódokat kiválasztani. Ezekből
lesznek előre beépített játékmódok is, és lehet majd sajátot is beállítani. Ha 
kiválasztjuk valamelyiket, akkor indul a játék. 
	A Beállítások gombra kattintva különbőző, az ilyen telefonos játékoknál megszokott
beállításokat módosíthatjuk kedvünkre. 

- A játék lényege az abban áll, hogy a karakterünkkel különböző mezőkre léphetünk, 
amik egy név-ár szövegpárral rendelkeznek. Ilyenkor ekkor eldönthetjük, hogy 
megvesszük-e az mező tartalmát(pl. benzinkút, étterem stb.). 
	Egy bizonyos összeggel indítunk, és ha ez elfogy, akkor veszítünk. 
	A különböző "ingatlanok" profitot is termelnek a játékban, körönként kiosztva 
ezzel az összeget a tulajdonosának. A játék logikája ezáltal ebben rejlik: jobb
ingatlanokat vásárolni, jobban gazdálkodni az adott összeggel. 

- Menü hierarchiák: Az applikáció indításakor a főmenü jelenik meg a felhasználó
előtt. Itt különböző opciókat kínál az alkalmazás, mint a Játék és a Beállítások. 

- Képernyőtervek: Megtekinthető a Github repóban. 
    
## 11. Fizikai környezet

- A felhasználók környezete: Úgy fejlesztjük az applikációt, hogy a felhasználó 
Androidot futtató mobiltelefonon használhatja az alkalmazást. 
    
- Az alkalmazást a felhasználó tölti le az internetről. 
    
- Fejlesztői környezet: Az alkalmazás, mivel Androidra lesz fejlesztve, ezért 
Kotlin nyelven lesz megírva, mivel a modern követelmények így kívánják.

	A projekt fájljainak tárolásához és verziókezeléséhez [GitHub](https://github.com/mndalex/DEB_2019_1_3_Project_2)-ot használunk. 
	A lefejlesztéshez [Android Studio](https://developer.android.com/studio/)-t használunk. 
	Az adatok helyileg tárolódnak el.
