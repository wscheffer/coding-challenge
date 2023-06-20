# QA Coding Challenge: UI mobile automation
### Test scenario:
The house-booking application has to be tested. The application can be used to book various houses for short stays. The goal of the coding challenge is to ensure that the applicant has a strong knowledge of Android test automation.

On the start page you should first look for all houses including the letter **"b"**.
Afterwards the house **House BlackWood of Raventree Hall** should be selected.
On the detail page you can book the selected house by clicking the `Book` button.

**Functionalities:**
- Calling up the house detail page
- Check that only houses that have a `Lord` can be booked
    - Check that the `Book` button after a click will generate a text under the button
    - Check that a toast is created after each click on the `Book` button
- The sorting `Sort houses without lords` should only show detached houses

### Acceptance Criteria:
- Page Object Pattern must be used for the tests.
- The tests must run successfully on an Emulator:
    - Display size: 5.0"
    - Resolution: 1080x1920 px
    - Release Name: Oreo
    - API Level: 27
    - Android: 8.1

### General conditions:
- The programming language for the test project to be created can be chosen freely.
- The Project can be built with Maven or Gradle
- Any Gradle/Maven dependencies can be added
- The use of open source projects is allowed

--- German -----------------------------------------------

### Test Szenario:
Zu testen ist die house-booking Applikation. Mittels der Applikation lassen sich unterschiedliche Häuser für einen kurzen Aufenhalt buchen. Ziel der Coding-Challenge ist es, dass der Bewerber ein solides Wissen im Bereich der Android-Testautomatisierung nachweisen kann.

Auf der Startseite soll zuerst nach allen Häusern gesucht werden die den Buchstaben **"b"** beinhalten.
Anschließend soll das Haus **House BlackWood of Raventree Hall** in der Suche ausgewählt werden.
Auf der Detailseite soll dann das ausgewählte Haus über den `Book` Button gebucht werden.

**Funktionalitäten:**
- Das Aufrufen der Haus-Detailseite
- Es sollen nur Häuser gebucht werden die einen `Lord` haben
    - Nachdem auf dem Button `Book` geklickt wird, erscheint unter diesem ein Text
    - Nach jedem Klick auf dem `Book` Button wird ein toast erzeugt
- Die Sortierung `Sort houses without lords` soll nur alleinstehende Häuser anzeigen

### Akzeptanzkritieren: 
- Page Object Pattern müssen für die Tests verwendet werden.
- Die Test müssen erfolgreich auf einen Emulator durchlaufen:
    - Displaygröße: 5.0"
    - Displayauflösung: 1080x1920 px
    - Release Namen: Oreo
    - API Level: 27
    - Android: 8.1
    
### Rahmenbedingungen:
- Die Programmiersprache für das zu erstellende Test-Projekt kann frei gewählt werden
- Das Projekt kann mit Maven oder Gradle gebaut werden
- Es können beliebige Gradle/Maven dependencies hinzugefügt werden
- Die Nutzung von Open-Source Projekten ist gestattet
