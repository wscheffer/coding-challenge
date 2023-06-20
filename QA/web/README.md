# QA Coding Challenge: UI automation
### Test scenario:
The German version of the [Porsche Finder] needs to be tested. The Porsche Finder is used to find an used Porsche according to desired criteria and to contact the distributor. The goal of the coding challenge is to ensure that the applicant has a strong knowledge of test automation.

On the landing page the first step is to restrict the search for a specific location with the following criteria:

- Stadt/ Postleitzahl: **10245** (postal code)
- Distanz: **50 km** (distance)
- Porsche Zentrum: **Porsche Zentrum Berlin** (distributor)
- Modell: **911** (model)

Afterwards you should confirm the set filters via the button `> x  Porsche verfügbar` to get an overview of all available vehicles that match to the chosen criteria.

To limit the selection even further, the filter **Preis und Zustand:** (Price and Condition) should be used on the newly loaded page and the following criteria should be set:
- Fahrzeugtyp: **Gebrauchtfahrzeuge** (vehicle type)
- Laufleistung: **0 - 200.000** (mileage)
- Preis: **100.000 - 300.000** (price)
- Erstzulassung: **2000 - 2019** (registration date)
- Maximale Anzahl Vorbesitzer: **6** (amount of previous owners)

After the filter has been confirmed select **Preis: Niedrigster** (price: lowest) from the dropdown menu. Then the cheapest vehicle should be displayed via the button `> Fahrzeugdetails` (vehicle details).

Useful test cases should be written for this page in order to be able to guarantee within the scope of a CI that previous functionalities continue to run correctly after a change or an update of features.

**Functionalities:**
- Calling up the vehicle details page
    - Checking the price and vehicle model
- Set filters must match with the result of the search results page.
- The sorting should sort in ascending order.    

Note: An already existing CI pipeline is assumed.

### Acceptance Criteria:
 - Page Object Pattern must be used for the tests.
 - The tests must run successfully on at least two browsers (e.g. Firefox and Chrome).
 
### General conditions:
 - The programming language for the test project to be created can be freely chosen.
 - Project can be built with Maven or Gradle
 - Any Gradle/Maven dependencies can be added
 - The use of open source projects is allowed

 [Porsche Finder]: https://finder.porsche.com/de/de_DE/ 

 

--- German -----------------------------------------------

### Test Szenario:
Zu testen ist die deutsche Version des [Porsche-Finders]. Mittels des Porsche-Finders lassen sich gebrauchte Porsche nach gewünschten Kriterien finden und zum Händler kann Kontakt aufgenommen werden. Ziel der Coding-Challenge ist es, dass der Bewerber ein solides Wissen im Bereich der Testautomatisierung nachweisen kann.

Auf der Startseite soll zuerst auf einen bestimmten **Standort** eingeschränkt werden und folgende Kriterien genutzt werden:
- Stadt/ Postleitzahl: **10245**
- Distanz: **50 km**
- Porsche Zentrum: **Porsche Zentrum Berlin**
- Modell: **911**

Anschließend sollen die gesetzten Filter über den Button `> x  Porsche verfügbar` bestätigen werden, um eine übersicht über alle verfügbaren Fahrzeuge mit den gesetzten Filtern zu erhalten.


Um die Auswahl noch weiter einzuschränken, soll auf der neu geladenen Seite zusätzlich der Filter **Preis und Zustand:** verwendet und folgende Kriterien eingestellt werden:
- Fahrzeugtyp: **Gebrauchtfahrzeuge**
- Laufleistung: **0 - 200.000**
- Preis: **100.000 - 300.000**
- Erstzulassung: **2000 - 2019**
- Maximale Anzahl Vorbesitzer: **6**

Nachdem der Filter bestätigt wurde, soll über das Dropdown-Menü **Preis: Niedrigster** ausgewählt werden. Anschließend soll das günstigste Fahrzeug über den Button `> Fahrzeugdetails` angezeigt werden.

Für diese gefilterte Übersicht sollen nun sinnvolle Testfälle geschrieben werden, um im Rahmen einer CI gewährleisten zu können, dass bisherige Funktionalitäten nach einer änderung bzw. einer Aktualisierung von Features weiterhin ordnungsgemäß laufen.

**Funktionalitäten:**
- Das Aufrufen der Fahrzeugdetailseite
    - Prüfung des Preises und Fahrzeugmodell
- Gesetzte Filter müssen mit dem Ergebnis der Suchergebnisseite übereinstimmen
- Die Sortierung soll aufsteigend sortieren    

Hinweis: Es kann von einer bereits bestehenden CI-Pipeline ausgegangen werden.

### Akzeptanzkritieren: 
 - Page Object Pattern müssen für die Tests verwendet werden.
 - Die Tests müssen auf mindestens zwei Browsern (bsp. Firefox und Chrome) erfolgreich durchlaufen
 
### Rahmenbedingungen:
 - Die Programmiersprache für das zu erstellende Test-Projekt kann frei gewählt werden
 - Das Projekt kann mit Maven oder Gradle gebaut werden
 - Es können beliebige Gradle/Maven dependencies hinzugefügt werden
 - Die Nutzung von Open-Source Projekten ist gestattet

 [Porsche-Finders]: https://finder.porsche.com/de/de_DE/
