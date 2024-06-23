# Einleitung
Diese Datei beschreibt im groben, wie diese Anwendung funktioniert, wie sie zustande gekommen ist und welche Probleme es auf dem Weg dahin gab.

Die Idee für dieses Projekt kam mir sehr spontan. In der dieser Theoriephase vorausgegangenen Praxisphase mussten wir eine Praxisarbeit schreiben. Eine der möglichen Themen war dabei die Entwicklung einer kleinen Web Anwendung, die einen manuellen und sehr benutzerunfreundlichen Prozess vereinfachen sollte. Durch das Klicken einige Knöpfe würde so die zuvor recht aufwendige Arbeit von der Anwendung übernommen werden. Dies erinnerte mich an ein Projekt, das ich während ich an der Schule war, programmiert hatte: um es zu bedienen mussten jedes Mal einige Stellen im Code abgeändert werden und das Projekt neu gestartet werden. Dies war auf Dauer sehr umständlich, weshalb das Projekt dann langsam in Vergessenheit geriet.
## Die Simulation
Das zuvor angesprochene Projekt war eine recht schön anzusehende Simulation von Ameisen. Es wurde versucht die Duftspuren von Ameisen und ihr Verhalten darauf zu visualisieren. Jede vorhandene Ameise macht in jedem Zeitschritt der Simulation folgende Schritte.
-	Untersuche die Stärke der Duftspur an drei Stellen: Links vorne, geradeaus und rechts vorne.
-	Drehe dich in Richtung der stärksten Konzentration und gehe nach vorne
-	Hinterlasse deine eigene Duftspur

Damit die Duftspuren zu sehen waren, wurden sie farblich in einem Bild dargestellt, die Ameisen waren dabei einzelne Pixel. Damit nicht nach längerer Zeit das ganze Bild eine einheitliche Farbe hätte, wird von der Simulation in jedem Zeitschritt ein geringer Anteil von allen Durfspuren abgezogen. Dadurch werden weniger bewanderte Wege mit der Zeit verblassen und nur die Hauptwege bleiben. Zudem werden alle Wege geglättet, was für ein schöneres Bild sorgt und die langsame Verwischung der Wege simuliert.
## Zielsetzung / Anforderungen
Als uns dann in der Vorlesung die Aufgabe gestellt wurde ein Web-Projekt zu suchen, fiel mir die Praxisphase und damit auch mein altes Projekt wieder ein. Da unser Projekt zusätzlich noch eine Client-Server Struktur aufweisen sollte, hatte ich mir Folgendes überlegt:
-	Die Simulation sollte zu dem Server umprogrammiert werden und damit für das Berechnen der Simulation zuständig sein.
-	Das Anzeigen der Simulation sowie das zuvor sehr aufwändige Abändern von Simulationsparametern sollte von dem Client übernommen werden. Damit die Bedienung auch benutzerfreundlich ist, sollte dies am besten mit Elementen wie Slidern umgesetzt werden.
Damit war die grobe Struktur des Projekts festgelegt, zu Details wie die Form der Schnittstelle oder wie der Client aufgebaut ist hatte ich mir zu diesem Zeitpunkt auch schon Gedanken gemacht, allerdings kam es während der Programmierung davon zu so vielen kleinen Abänderungen, dass das endgültige Ergebnis nicht mehr viel mit der ursprünglichen Idee zu tun hat.

Abgesehen von der Umstrukturierung der alten Simulation in Client und Server gab es eine weitere zentrale Anforderung: Die Benutzerfreundlichkeit der Anwendung sollte soweit erhöht werden, dass auch eine Person, die den Code nicht auswendig kennt, die Simulationsparameter einstellen und nachvollziehen kann.
# Umsetzung
## Technologieauswahl
Die alte Simulation war in Java geschrieben, ich habe mich entschieden dabei zu bleiben. Zum einen wäre es recht aufwändig den ganzen Code in eine andere Sprache zu übersetzen, zum anderen habe ich bisher mit neuen Frameworks immer sehr lange gebraucht, um mich einzuarbeiten. Da Java für mich recht vertraut ist, war dies eine gute Option. Da der zu implementierende Server sehr simpel sein wird und keine spezielleren Anforderungen erfüllen muss, habe ich mich für den in Java enthaltenen httpServer entschieden. Dieser lässt sich mit wenigen Zeilen Code starten und ist auch im Umgang mit verschiedenen Endpoints sehr flexibel und einfach zu verstehen.

Für den Client war die Wahl einer Technologie deutlich schwieriger. Ich hatte zuvor nur einen kurzen Kontakt mit Web-Entwicklung in Angular. Da Angular recht groß ist, war ich erst gewillt eine andere Technologie zu wählen. Allerdings hatte ich durch meinen vorherigen Kontakt damit schon einige Vorkenntnisse von der Struktur und Funktionsweise, die ich dann doch beschloss, im Zuge dieses Projekts etwas auszuweiten.
## Schnittstelle
Die Anforderungen an die Schnittstelle sind im Großen und Ganzen recht gering. Es gibt nur zwei Informationen, die über die Schnittstelle ausgetauscht werden müssen, der Rest läuft ausschließlich im Client oder im Server:
-	Informationen über den aktuellen Stand der Simulation (= Bild)
-	Updates über neue Parameteränderungen

Bei den Parameteränderungen kam mir sofort JSON in den Sinn, da die relevanten Informationen dafür der Parametername und der neue Wert sind und Zahlen und Strings sich sehr leicht in JSON speichern lassen. Die Daten der Simulation (= das Bild) wurden in der alten Simulation ein einem 3d-Array gespeichert und anschließend zum Anzeigen in ein BufferedImage umgewandelt. Es wäre möglich für das Bild nur das Array zu senden, um dann im Frontend daraus das Bild herzustellen. Da aber das Frontend primär zum Anzeigen der Daten zuständig ist und das Bild auf dem Server bereits berechnet wurde, habe ich mich dazu entscheiden, das fertige Bild zu schicken. Eine kurze Recherche hat ergeben, dass sich mit Hilfe der Base64 Kodierung Bilder im ASCII Format darstellen lassen. Dies kann dann über die Schnittstelle verschickt werden, von Angular gelesen und als Bild angezeigt werden.
Da die Base64 Repräsentation der Bilder Text sind und JSON dies unterstützt, habe ich meine ganze Schnittstelle mit JSON implementiert. Die Schnittstelle sieht damit wie folgt aus (Implementierung im Client):

```typescript
export interface ImageInterface{
  image: string;
}
```

```typescript
export interface ParameterInterface{
  parameter: string;
  value: number;
}
```
## Implementierung des Servers
Die Implementierung des Servers lässt sich in zwei Schritte einteilen:
### Umschreiben der Simulation
Zuvor war die Simulation ein alleine stehendes Projekt, das sowohl die Simulation berechnete als auch die Ergebnisse anzeigte. Da das Anzeigen der Simulation nun im Client passierte, musste dieser Teil des Codes herausgenommen werden. Diese Funktionalität war zum allergrößten Teil in der Simulations-Klasse implementiert und hatte wenige Verknüpfungen zu anderen Teilen des Projekts, er konnte also ohne weitere Probleme entfernt werden.

Um neue Parameterwerte zu erhalten und diese auch richtig in die laufende Simulation einfließen zu lassen, musste ich die Struktur des alten Projekts etwas umstellen. Die alte Struktur sah vor, dass zu Beginn einer Simulation alle Parameter bereit lagen und diese dann im Zuge der sich entwickelnden Simulation weiter benutzt werden. Um neue Parameterwerte im Zuge einer  laufenden Simulation miteinbeziehen zu können, wurden die zuvor konstanten Variablen als globale Variablen angelegt, die durch die Server-Klasse verändert werden können.
### Implementieren der Serverfunktionalität
Das Aufsetzten des Servers ging, vorallem durch die vorhandene Dokumentation, recht schnell, zudem war der Code dazu auch recht kompakt. Mit wenigen Zeilen konnte ich schon einen Server starten. Diesen legte ich in einem neuen Paket namens server an.

Der kompliziertere Teil war das aufsetzen der Handler für die beiden Endpoints /image zu Bereitstellung der Bilder und /parameter zum entgegennehmen und weiterreichen der veränderten Parameter. Hier musste für beide jeweils ein HttpHandler geschrieben werden, der eigentlich recht wenig zur Verfügung stellte. Sowohl das Auslesen als auch das Interpretieren der einkommenden Anfragen musste ich selbst schreiben, da Java auch standardmäßig nicht JSON unterstützt, musste ich das auf eine recht umständliche Weise lösen.
## Implementieren des Clients
Da ich den Client von Grund auf geschrieben habe, musste ich hier keinen alten Code umschreiben und konnte meine gewünschte Funktionalität direkt umsetzen. Der Client besteht primär aus drei verschiedenen Komponenten:

- image-component: diese Komponente ist rein dazu da, das vom Server abgefragte Bild anzuzeigen.
- slider-bar-component: Der Slider-bar-component zeigt zum einen die verschiedenen Slider an, an denen man die Parameter der Simulation abändern kann, zum anderen ist der dafür zuständig bei dem Update eines Sliders den neuen Wert eines Parameters an den Server zu schicken.
- image-service: Dieser Service ist nur dazu da, periodisch (alle 33ms = 30fps) den Server nach einem neuen Bild anzufragen, dies zu erhalten und an den image-component weiterzuleiten.
## Probleme
Wie bei jedem Projekt, gab es auch bei diese einige Probleme und Fehler, von denen sich aber die meisten schnell lösen ließen. Allerdings gab es auch ein größeres Problem, an dem ich lange saß: Die Komplexität der Schnittstelle
Es ist sehr schwierig die Abfrage des Clients so zu timen, dass sie ankommt, sobald der Server das letzte Bild berechnet hat. Deshalb war ich sehr lange der Meinung, dass ich jedes Bild, das im Server berechnet wird auch anzeigen sollte. Sollte also mal der Server schneller neue Bilder berechnen, als der Client sie anzeigen kann, dann würde dieser warten, bis ein neues Bild benötigt wird, um nicht unnötig Rechenleistung zu verschwenden.

Die erste Idee, die ich hatte, war ein Bild zu berechnen, sobald eine Anfrage nach einem neuen Bild angekommen war. Die Anfrage würde das zuletzt berechnete Bild erhalten und in der Zeit, bis die nächste anfrage ankommt könnte der Server ein neues Bild berechnen. Dies funktionierte aber nicht, da – obwohl die Anfragen periodisch abgesendet wurden – dazu tendierten, sehr gehäuft bei dem Server anzukommen. Oft kamen fünf bis zehn Anfragen auf einen Haufen, gefolgt von einer längeren Pause. Der Server hatte so keine Zeit zwischen den Anfragen ein neues Bild zu berechnen, sodass meist nur wenige verschiedene Bilder im Client ankamen, was zu einem sehr stotternden Bild führte.

Die zweite Idee, die ich dazu hatte, war einen Puffer von Bildern im Server zu führen. Der Server würde immer weiter neue Bilder berechnen, solange bis der Puffer voll ist. Kommt nun eine Anfrage nach einem Bild von dem Client, so würde das älteste Bild aus dem Puffer zurückgegeben werden und der Server könnte ein neues Bild berechnen. Nach diesem Prinzip konnten auch bei einem Schwall von Anfragen alle beantwortet werden, vorausgesetzt der Puffer ist groß genug. Das war die Theorie, in der Praxis sah das aber leide ganz anders aus. Ich vermute, dass dies daran liegt, dass sehr viele Daten im Speicher hin- und hergeschoben werden müssen (Einfügen in die Queue, Löschen aus der Queue, Einfügen in die Anfrage, Auslesen der Anfrage im Client). Ein Bild ist rund 800 * 300 (Bildauflösung) * 3 (Farbkanäle RGB) * 4 (Bytes für einen int) = 720 kB groß. Bei 60 Bildern die Sekunde sind das etwa 43mB für jeden Schritt.

Die letzte und schlussendlich auch zielführende Idee war, die Bildfrequenz im Client auf 30hz zu setzen und den Server immer dann nach dem neuesten Bild zu fragen. Durch die geringere Frequenz der Bildanfrage kommt es nicht mehr zu solch vielen anfragen und falls mal ein oder zwei Bilder gleich sind, fällt das nicht auf. 
Letztendlich war dann doch die leichteste Variante die, die funktioniert hat. So habe ich aus erster Hand erfahren können, wie das Programmierparadigma „Keep it simple, stupid!“ – von dem ich zuvor nicht so viel hielt – voll zugeschlagen hat.
# Fazit / Lessons Learned
Im Zuge dieses Projektes habe ich einiges gelernt und viel altes Wissen wieder aufgefrischt. Vor allem im Schnittstellenbereich habe ich vielmitgenommen: was ist CORS? oder auch wie eine Anfrage im konkreten aufgebaut ist. Des Weiteren konnte ich mein Wissen in Angular auffrischen und im Bereich von Services und Datenverteilung über Komponenten hinweg vertiefen. Aber am wichtigsten – vorallem, weil es für jede Sprache und jedes Framework Anwendung findet – war dann doch die Erkenntnis, dass eine leicht unperformantere Lösung deutlich besser als eine performante und sehr komplizierte Lösung ist.

Zusammenfassend lässt sich sagen, dass dieses Projekt sehr erfolgreich war. Zusätzlich zu den umgesetzten Anforderungen habe ich viel über die Web-Entwicklung gelernt – ein Bereich mit dem ich zuvor noch nicht viel zu tun hatte- und darüber hinaus auch noch viel Spaß dabei gehabt der Anwendung beim Wachsen zuzusehen. Die Anwendung ist jetzt in einem fertigen Zustand, mit dem ich sehr zufrieden bin, sollte sich die Gelegenheit bieten, so können noch weitere Features der Simulation hinzugefügt werden.
