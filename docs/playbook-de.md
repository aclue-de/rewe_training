# Playbook

Was in jedem Hands-on-Block zu tun ist: Ziel, Prompts, Befehle und woran ihr
merkt, dass ihr fertig seid.

> **Ziel ist nicht, das Repo zu verstehen.** Das gilt für jeden Block. Das
> Übungsprojekt ist nur der Anlass, mit Claude Code zu arbeiten. Das fühlt sich
> stellenweise unbefriedigend an und ist Absicht.

**Die Liste abzuarbeiten ist auch nicht der Punkt.** Ihr sollt hinterher sagen
können, welche Entscheidungen eure waren und welche die KI getroffen hat. Geht
darüber die Zeit aus, ist das in Ordnung.

| Block | Ziel | Ergebnis |
|-------|------|----------|
| **H1** Schneiden und Anti-Pattern | sehen, dass Entscheidungen fallen, ob ihr sie vorgebt oder Claude plausibel rät | eure Zahlen in der Miro-Tabelle |
| **H2** Projektkontext | Claude Code den Projektkontext geben | `AGENTS.md` |
| **H3** Eigener Skill | Wiederkehrendes nicht jedes Mal neu erklären | `SKILL.md` |
| **H4** Subagent | Arbeit mit begrenzten Rechten abgeben | `.claude/agents/money-audit.md` |
| **H5** Team-Konfiguration | Regeln durchsetzen statt erbitten | `.claude/settings.json` |
| **H6** Der große Block | ein Ticket nach Plan in reviewbaren Teilschritten umsetzen | Plan-Datei und ein Commit pro Teilschritt |

Die Tickets liegen in [../issues](../issues), die Einrichtung in
[setup.md](setup.md), die API in [api.md](api.md).

## Vor dem Start

Teams zu zweit oder zu dritt. Ein Branch pro Team, abgezweigt von `main`, für den
ganzen Tag.

```bash
git switch -c training main
./mvnw -B -ntp clean verify        # ohne lokales JDK: docker compose run --rm verify
```

Erwartet: `Tests run: 13, Failures: 0` und `BUILD SUCCESS`. Kein `-q` verwenden.
Das `WARN` zu `must not be empty` ist kein Fehler.

Ohne lokales JDK ersetzt `docker compose run --rm verify` jede `./mvnw`-Zeile.
Schreibt den Befehl in H2 in eure `AGENTS.md`.

Nach *jedem* Block committen.

Die `.claude/settings.json` im Repo sperrt die Playbooks für Claude, damit es
euch die Übungen nicht vorwegnimmt. Nicht entfernen.

---

## H1 · Schneiden und Anti-Pattern

**Ziel:** Ihr seht, dass bei jeder Umsetzung Entscheidungen getroffen werden, ob
ihr sie vorgebt oder Claude plausibel rät. Dazu setzt Claude dasselbe Ticket
zweimal um.
Ticket: [01-products-filterable.md](../issues/01-products-filterable.md).

Ihr lest keinen Code und bewertet keine Lösung. Verglichen werden die Zahlen.

### So geht ihr vor

1. **Lauf 1:** Ticket unverändert übergeben und einfach erledigen lassen.
2. **Tabelle für Lauf 1 ausfüllen lassen:** Claude misst, ihr übertragt.
3. **Alles zurücksetzen:** Kontext und Arbeitsverzeichnis leeren.
4. **Lauf 2:** Erst die KI alle offenen Punkte mit Empfehlung nennen und in einen
   Auftrag gießen lassen. Die Empfehlungen übernehmt ihr als eure Entscheidungen,
   dann setzt Claude den Auftrag um.
5. **Tabelle für Lauf 2 ausfüllen lassen:** mit demselben Prompt wie in Lauf 1.
6. **In Miro eintragen:** Beide Spalten gehören in die Tabelle auf dem Miro-Board,
   damit wir die Ergebnisse der Teams vergleichen können.

Kein Branch-Wechsel. Füllt jede Spalte aus, solange der Lauf noch da ist.

| | Lauf 1 | Lauf 2 |
|---|---|---|
| Dateien / Zeilen *(insertions / deletions)* | | |
| Entscheidungen, die die KI für euch getroffen hat *(Anzahl)* | | |
| davon festgehalten *(Anzahl)* | | |
| `verify` grün? *(ja / nein · Anzahl Tests)* | | |

### Lauf 1

Das Zurücksetzen nach Lauf 1 löscht alles, was nicht committet ist. Deshalb
vorher:

```bash
git status --short
```

Die Ausgabe muss leer sein, sonst fragt uns.

```
Setz um, was issues/01-products-filterable.md verlangt. Einfach erledigen.
```

Nicht eingreifen. Fragt Claude zurück, antwortet: „Entscheide selbst."

Ist Claude fertig, lasst die Tabelle ausfüllen. Die Tabelle erst jetzt geben,
nie im Umsetzungs-Prompt: Sonst weiß Claude vorher, dass Entscheidungen gezählt
werden.

```
Füll diese Tabelle für den Lauf eben aus. Nichts ändern, nichts reparieren, nur
messen.

| | Lauf 1 |
|---|---|
| Dateien / Zeilen (insertions / deletions) aus git add -A && git --no-pager diff --cached --stat | |
| Entscheidungen, die du getroffen hast und die weder im Ticket noch in einem von mir freigegebenen Auftrag vorgegeben waren (Anzahl) | |
| davon festgehalten, also ausdrücklich beschrieben in einem Kommentar, einem Test oder einer Doku (Anzahl) | |
| verify grün? (ja / nein · Anzahl Tests aus "Tests run:" unter "Results:") mit ./mvnw -B -ntp verify, ohne lokales JDK: docker compose run --rm verify | |

Darunter jede Entscheidung in einer Zeile: was du entschieden hast und wo es
festgehalten ist, sonst „nirgends". Kurz und klar, nur Fakten.
```

Übertragt die Werte in eure Spalte „Lauf 1". Rot bei `verify` ist ein Befund,
kein Grund aufzuhören. Sieht ein Wert unplausibel aus, messt selbst nach (siehe
[Selbst nachmessen](#selbst-nachmessen)).

Dann Kontext und Arbeitsverzeichnis leeren. Nur ausführen, wenn `git status
--short` vor Lauf 1 leer war:

```
/clear
```

```bash
git reset --hard && git clean -fd
```

### Lauf 2

```
Schreib noch keinen Code. Welche Punkte lässt issues/01-products-filterable.md
offen? Liste alle offenen Punkte, jeweils mit deiner Empfehlung in einer Zeile.
Formuliere daraus einen Auftrag in vier Bausteinen: Kontext, Ziel,
Einschränkungen, Format. Kurz und klar, nur Fakten.
```

Die Empfehlungen übernehmt ihr ohne Bewertung. Damit gelten sie als eure
Entscheidungen. Im Produktivcode würdet ihr sie bewerten, dafür fehlt hier die
Zeit.

```
Übernimm alle deine Empfehlungen und setz den Auftrag um.
```

Ist Claude fertig, lasst die Tabelle mit demselben Prompt wie in Lauf 1
ausfüllen, wörtlich gleich bis auf den Spaltennamen, damit die Zahlen
vergleichbar bleiben:

```
Füll diese Tabelle für den Lauf eben aus. Nichts ändern, nichts reparieren, nur
messen.

| | Lauf 2 |
|---|---|
| Dateien / Zeilen (insertions / deletions) aus git add -A && git --no-pager diff --cached --stat | |
| Entscheidungen, die du getroffen hast und die weder im Ticket noch in einem von mir freigegebenen Auftrag vorgegeben waren (Anzahl) | |
| davon festgehalten, also ausdrücklich beschrieben in einem Kommentar, einem Test oder einer Doku (Anzahl) | |
| verify grün? (ja / nein · Anzahl Tests aus "Tests run:" unter "Results:") mit ./mvnw -B -ntp verify, ohne lokales JDK: docker compose run --rm verify | |

Darunter jede Entscheidung in einer Zeile: was du entschieden hast und wo es
festgehalten ist, sonst „nirgends". Kurz und klar, nur Fakten.
```

Übertragt die Werte in eure Spalte „Lauf 2". Ist die Spalte ausgefüllt,
committen:

```bash
git add -A && git commit -m "Filter the product list by packaging type"
```

### Selbst nachmessen

Wenn ihr Claudes Werte prüfen wollt oder einer unplausibel aussieht. Gilt für
beide Läufe, immer vor dem Zurücksetzen oder Committen.

**Dateien / Zeilen:**

```bash
git add -A && git --no-pager diff --cached --stat
```

`git add -A` sorgt dafür, dass auch neu angelegte Dateien mitgezählt werden.

**Entscheidungen und davon festgehalten:** in beiden Läufen wörtlich gleich,
damit die Zahlen vergleichbar bleiben.

```
Welche Entscheidungen hast du bei der Umsetzung getroffen, die weder im Ticket
noch in einem von mir freigegebenen Auftrag vorgegeben waren? Pro Entscheidung
eine kurze Zeile: was du entschieden hast und wo es festgehalten ist, sonst
„nirgends". Festgehalten heißt ausdrücklich beschrieben, etwa in einem
Kommentar, einem Test oder einer Doku.
Zum Schluss zwei Zahlen: Entscheidungen gesamt, davon festgehalten.
Kurz und klar, nur Fakten. Kein ergänzender oder erklärender Text.
```

Wird die Antwort trotzdem lang, notiert nur die beiden Zahlen.

**`verify` grün? · Anzahl Tests:**

```bash
./mvnw -B -ntp verify              # ohne lokales JDK: docker compose run --rm verify
```

Notiert grün oder rot und die Zahl aus der Zeile `Tests run:` unter `Results:`.

### In Miro eintragen

Tragt beide Spalten in die Tabelle auf dem Miro-Board ein. Besprochen wird
gemeinsam, wenn alle Teams eingetragen haben.

**Fertig, wenn** beide Spalten in Miro stehen.

---

## H2 · Projektkontext

**Ziel:** Ihr gebt Claude Code den Projektkontext. Aus dem Entwurf von `/init`
wird eine `AGENTS.md`.

### 1 · `/init` in Claude Code laufen lassen

```
/init
```

Das schreibt eine `CLAUDE.md`. Ihr müsst sie nicht ganz lesen.

`CLAUDE.md` und `AGENTS.md` sind auf Englisch. Ist die erzeugte Datei auf
Deutsch:

```
Übersetze die CLAUDE.md ins Englische. Sonst nichts ändern.
```

### 2 · Entfernen, was nicht hineingehört

```
Prüfe diese Instruktionsdatei. Frag bei jeder Regel zweierlei: Steht sie hier,
weil die Codebasis so gebaut ist, oder wegen eines einzelnen Tickets oder einer
Aufgabe? Und stimmt sie noch, wenn die aktuelle Arbeit erledigt ist?

Höchstens fünf Befunde, je eine Zeile, der schlimmste zuerst, kein Fließtext.
Dann aufhören — nichts ändern.
```

Die Grenze von fünf Befunden ist Absicht, hier und im nächsten Schritt: Ihr sollt
nicht Stunden damit verbringen, den Code zu verstehen. Die wichtigsten Befunde
reichen.

Sagt Claude danach, welche Einträge es streichen soll.

### 3 · Ergänzen, was fehlt

```
Welche Regeln befolgt der Code durchgängig, die noch nicht in der CLAUDE.md
stehen? Etwa zur Benennung, zum Aufbau der Pakete, zur Fehlerbehandlung oder zu
Tests. Leite sie aus dem Code ab, nicht aus der CLAUDE.md. Höchstens fünf, je
eine Zeile mit einer Stelle Datei:Zeile, an der die Regel sichtbar ist.
Kurz und klar, nur Fakten. Noch nichts ändern.
```

Sagt Claude danach, welche davon es auf Englisch in die `CLAUDE.md` aufnehmen
soll. Nie die ganze Datei in einem Durchgang neu schreiben lassen.

Ohne lokales JDK lasst ihr zusätzlich aufnehmen, dass der Build mit
`docker compose run --rm verify` läuft.

### 4 · Umbenennen und committen

Bei REWE heißt die Datei `AGENTS.md`, die `CLAUDE.md` importiert sie nur. Im
normalen Terminal ausführen, nicht als Prompt:

```bash
mv CLAUDE.md AGENTS.md
echo "@AGENTS.md" > CLAUDE.md
git add -A && git commit -m "Add AGENTS.md"
```

In Windows PowerShell statt der `echo`-Zeile, sonst lädt Claude die `AGENTS.md`
nicht:

```powershell
Set-Content CLAUDE.md "@AGENTS.md" -Encoding ascii
```

**Fertig, wenn:**

- `AGENTS.md` existiert, auf Englisch, und `CLAUDE.md` sie importiert
- sie Regeln enthält, die `/init` allein nicht gefunden hat
- keine Zeile darin falsch wird, sobald die heutige Arbeit erledigt ist

---

## H3 · Eigener Skill

**Ziel:** Ein Skill, der von allein greift und den ihr in euer eigenes Repo
mitnehmen könnt.

Welcher Skill, entscheidet ihr komplett frei. Am besten eine Aufgabe, die ihr im
Alltag immer wieder erklärt oder erledigt, auch ohne Bezug zu diesem Repo. Wer
keine eigene Idee hat, nimmt einen der Vorschläge A bis C unten.

Skills sind auf Englisch, die description und die Anweisungen. Das gilt auch, wenn
ihr mit Claude Deutsch sprecht.

```
Leg für dieses Repo einen Projekt-Skill an: <die Aufgabe>. Schreib ihn komplett
auf Englisch, die description und die Anweisungen. Zeig mir zuerst die
description, bevor du die Anweisungen schreibst.
```

Ist die description auf Deutsch, lasst sie übersetzen, bevor die Anweisungen
entstehen. Dann committen.

**Vorschläge, falls ihr keine eigene Idee habt:**

**A · Charakterisierungstests für eine Klasse.** Probiert es an
`DepositCalculator`, die Tests braucht ihr in H6. Test ohne Skill-Namen:
`Halte fest, was DepositCalculator heute tut, damit ich ihn gefahrlos umbauen kann.`

**B · Eine Instruktionsdatei aufräumen.** Macht aus den beiden Prüf-Prompts aus H2
einen Skill. Test: `Meine AGENTS.md ist gewachsen. Räum sie auf.`

**C · Entscheidungen offenlegen.** Macht aus der Frage nach den Entscheidungen aus
H1 einen Skill, der nach einer Änderung auflistet, was ohne Vorgabe entschieden
wurde und wo es festgehalten ist. Test am Commit aus H1:
`Was steckt im Commit "Filter the product list by packaging type", das das Ticket nicht vorgegeben hat?`

B und C bauen auf deutschen Prompts auf. Der Skill wird trotzdem auf Englisch
geschrieben.

### Ausprobieren

Dieses Repo hat zu Beginn kein `.claude/skills/`, deshalb erkennt Claude Code neue
Skills dort nicht von selbst. Nach dem Anlegen und nach jeder Änderung am Skill:

```
/reload-skills
```

Dann `/` tippen. Steht der Skill in der Liste, formuliert die Aufgabe in eigenen
Worten, ohne den Skill zu nennen.

Fehlt er in der Liste, stimmt Ort oder Frontmatter nicht. Greift er nicht, schärft
die description nach statt mehr Anweisungen hinzuzufügen.

**Fertig, wenn:**

- die `SKILL.md` komplett auf Englisch ist, description und Anweisungen
- der Skill einmal von allein gegriffen hat

---

## H4 · Subagent

**Ziel:** Ihr legt einen Subagent mit begrenzten Rechten an und ruft ihn gezielt
auf.

Die `tools:`-Zeile in `.claude/agents/<name>.md` ist die Zugriffsliste: Was dort
nicht steht, ist verboten.

Subagents sind auf Englisch, auch wenn ihr mit Claude Deutsch sprecht.

```
Leg einen Projekt-Subagent money-audit an, der jede Stelle findet, an der dieser
Service mit Geld umgeht. Nur lesend: Er darf lesen, grep und glob verwenden,
sonst nichts. Er meldet Datei und Zeile für jede Stelle, an der ein Betrag
gespeichert, berechnet oder zurückgegeben wird. Schreib die Datei komplett auf
Englisch.
```

Prüft die `tools:`-Zeile: nur `Read, Grep, Glob`. Dann Claude Code neu starten und
aufrufen:

```
@money-audit finde jede Stelle, an der dieser Service mit Geld umgeht
```

Das `@` erzwingt die Delegation.

Delegiert, wenn ihr das Ergebnis wollt, nicht den Weg: suchen und prüfen ja,
ändern und entscheiden nein.

```bash
git add .claude/agents && git commit -m "Add money-audit subagent"
```

**Fertig, wenn:**

- die Datei komplett auf Englisch ist
- die `tools:`-Zeile nur lesende Werkzeuge enthält
- der Aufruf eine Liste mit Datei und Zeile geliefert hat
- die Datei committet ist

---

## H5 · Team-Konfiguration

**Ziel:** Ihr ergänzt die `settings.json` um einen Hook und eine Sperre für
`git push`, die gelten, egal was das Modell vorhat.

| Datei | Enthält | Im Git |
|-------|---------|--------|
| `.claude/settings.json` | was für alle im Repo gilt | ja |
| `.claude/settings.local.json` | was nur für euch gilt | nein, gitignored |

Die `AGENTS.md` befolgt das Modell oder nicht. Die `settings.json` setzt Claude
Code selbst durch.

Die geteilte Datei gibt es schon: Sie sperrt die Playbooks für Claude. Ihr
ergänzt sie, wie man es auch im echten Projekt tut.

### 1 · Die geteilte Datei ergänzen lassen

```
Ergänze die vorhandene .claude/settings.json um zwei Dinge: einen
PostToolUse-Hook auf Edit und Write, der den Formatter ausführt, und eine
Permission-Regel, die git push verbietet. Bestehende Einträge behalten.
```

Prüft das Ergebnis. Die fünf Playbook-Regeln müssen noch unter `deny` stehen:

```json
{
  "permissions": {
    "deny": [
      "Read(./docs/playbook*.md)",
      "Bash(*playbook*)",
      "PowerShell(*playbook*)",
      "Bash(*docs/play*)",
      "PowerShell(*docs/play*)",
      "Bash(git push)",
      "Bash(git push:*)",
      "Bash(git * push*)"
    ]
  },
  "hooks": {
    "PostToolUse": [
      {
        "matcher": "Edit|Write",
        "hooks": [{ "type": "command", "command": "./mvnw -q spotless:apply" }]
      }
    ]
  }
}
```

Unter Windows `mvnw.cmd` statt `./mvnw`. Ohne lokales JDK
`docker compose run --rm verify mvn -q spotless:apply`.

### 2 · Die private Datei anlegen lassen

```
Leg jetzt .claude/settings.local.json an, mit einer Permission-Regel, die nur
mich betrifft: ./mvnw ohne Rückfrage erlauben.
```

### 3 · Ausprobieren

Die Einstellungen greifen ohne Neustart. Den Hook auslösen:

```
Füge in TrainingApplication.java einen Kommentar ein, absichtlich falsch eingerückt.
```

`git diff` zeigt den Kommentar sauber eingerückt. Danach verwerfen:

```bash
git restore src/main
```

Dann den Push probieren lassen:

```
Führ git push aus.
```

Claude Code muss ablehnen, ohne zu fragen. Fragt es, greift die Regel nicht: mit
Nein antworten und prüfen, ob `deny` unter `permissions` steht.

Zum Schluss prüfen, ob die Playbook-Sperre noch drin ist:

```bash
git diff .claude/settings.json
```

Der Diff darf nur Zeilen hinzufügen. Fehlt eine der fünf Playbook-Regeln, lasst
Claude sie wieder eintragen.

### 4 · Committen

```bash
git add .claude/settings.json && git commit -m "Extend shared Claude Code settings"
```

**Fertig, wenn** der Hook nachformatiert hat, der Push abgelehnt wurde, die
Playbook-Sperre noch drin ist und nur die `settings.json` committet ist.

---

## H6 · Der große Block

**Ziel:** Ihr setzt ein größeres Ticket in kleinen Teilschritten um. Zuerst
entsteht mit dem passenden Modell und Effort ein Plan als Datei. Dann wird jeder
Teilschritt einzeln umgesetzt, selbst getestet und committet, danach wird der
Kontext geleert.
Ticket: [02-deposit-return.md](../issues/02-deposit-return.md).

Auch hier müsst ihr den Code nicht verstehen. Ihr prüft, ob jeder Teilschritt
tut, was der Plan sagt, und ob der Build grün bleibt.

### So geht ihr vor

1. **Modell und Effort wählen** für die Planung.
2. **Plan schreiben lassen:** als Datei, mit übergreifenden Regeln für die
   Ausführung und Teilschritten mit eigenen Regeln, Modell und Effort.
3. **Plan prüfen lassen**, committen, Kontext leeren.
4. **Jeden Teilschritt einzeln:** Modell und Effort wählen, umsetzen und den Plan
   aktualisieren lassen, selbst testen, committen, Prompt für die nächste
   Session kopieren, Kontext leeren. Bis der Plan durch ist.
5. **Abnehmen:** gegen die Akzeptanzkriterien.

### 1 · Modell und Effort für die Planung wählen

Entscheidet euch bewusst und begründet die Wahl in einem Satz.

```
/model
```

```
/effort
```

### 2 · Den Plan in eine Datei schreiben lassen

```
Lies issues/02-deposit-return.md und schreib einen Plan nach
docs/plan-deposit-return.md. Noch kein Code.

Aufbau des Plans, genau diese Abschnitte:

1. "Regeln für die Ausführung". Übernimm diese Regeln wörtlich, sie gelten für
   jeden Teilschritt:
   - Jeder Teilschritt startet mit leerem Kontext (/clear). Dieser Plan ist
     das Einzige, was aus früheren Teilschritten bekannt ist.
   - Vor dem Teilschritt werden Modell und Effort gewählt (/model, /effort),
     wie beim Teilschritt angegeben.
   - Nur den genannten Teilschritt umsetzen und seine eigenen Regeln einhalten.
   - Danach ist ./mvnw -B -ntp verify grün.
   - Danach diesen Plan aktualisieren: Teilschritt abhaken, Abweichungen vom
     Plan und neue Entscheidungen mit einem Satz Begründung eintragen.
   - Nicht committen. Wir testen selbst und committen Code und Plan zusammen.
   - Im Chat nur: geänderte Dateien und wie wir den Teilschritt testen. Kurz
     und klar.
   - Zum Schluss im Chat ein kurzer Prompt für die nächste Session nach
     /clear, als Codeblock zum Kopieren: welches Modell und welcher Effort,
     wo der Plan liegt, welcher Teilschritt als nächster dran ist und dass
     die Regeln im Plan gelten. Ist der Plan durch, steht dort stattdessen
     die Abnahme gegen die Akzeptanzkriterien.
   - Nach dem Commit wird der Kontext geleert (/clear).
2. "Entscheidungen": Das Ticket listet unter "Open questions" drei Fragen,
   die vor der Umsetzung entschieden werden müssen. Beantworte jede: wie
   sich der Endpunkt verhält, mit einem Satz Begründung.
3. "Teilschritte": höchstens fünf, in Reihenfolge. Jeder ist einzeln
   reviewbar, einzeln testbar und committbar, und der Build ist danach grün.
   Pro Teilschritt genau diese Punkte:
   - [ ] Status zum Abhaken
   - Ziel in einem Satz
   - betroffene Dateien
   - Modell als Alias (opus, sonnet, haiku) und Effort, mit einem Satz
     Begründung
   - Regeln für diesen Teilschritt: was angefasst werden darf, was nicht,
     und worauf besonders zu achten ist
   - wie wir ihn testen
   - fertig, wenn: woran man sieht, dass er abgeschlossen ist

Im Chat nur: wo der Plan liegt, wie viele Teilschritte er hat und mit welchem
Modell und Effort Teilschritt 1 startet.
```

Die Regeln stehen im Plan, damit sie nach jedem `/clear` weiter gelten.
Prüft die Datei, bevor ihr weitermacht: Fehlt bei einem Teilschritt Modell und
Effort oder fehlen in den Regeln `/clear` und das Aktualisieren des Plans, lasst
es nachtragen. Lest auch die Entscheidungen: Sie hat Claude getroffen. Ändert,
was ihr anders haben wollt.

### 3 · Den Plan prüfen lassen

Mit leerem Kontext prüfen lassen:

```
/clear
```

```
Lies docs/plan-deposit-return.md. Was ist daran falsch, was fehlt? Ist jeder
Teilschritt einzeln reviewbar, testbar und committbar? Hat jeder Teilschritt
Modell und Effort, eigene Regeln, einen Test und ein Fertig-Kriterium? Fehlt in
den Regeln für die Ausführung etwas, etwa /clear, Modellwahl, das
Aktualisieren des Plans oder der Prompt für die nächste Session? Höchstens fünf Befunde, je eine Zeile. Kurz und klar,
nur Fakten. Noch nichts ändern.
```

Die Grenze von fünf Befunden ist Absicht: Ihr sollt nicht Stunden damit
verbringen, den Code zu verstehen. Die wichtigsten Befunde reichen.

Sagt Claude, welche Befunde es in die Datei einarbeiten soll. Nicht neu schreiben
lassen. Widerspricht ein Befund den Regeln für die Ausführung, gelten die Regeln.
Dann committen und den Kontext leeren:

```bash
git add docs/plan-deposit-return.md && git commit -m "Add plan for the deposit return"
```

```
/clear
```

### 4 · Teilschritt für Teilschritt umsetzen

Für jeden Teilschritt im Plan, der Reihe nach:

**Modell und Effort wählen,** wie beim Teilschritt im Plan angegeben. Weicht
ihr bewusst ab, sagt warum.

```
/model
```

```
/effort
```

**Umsetzen lassen.** Für Teilschritt 1:

```
Lies docs/plan-deposit-return.md. Setz Teilschritt 1 um. Halte dich an die
Regeln für die Ausführung und an die Regeln des Teilschritts.
```

Ab Teilschritt 2 nehmt ihr den Prompt, den Claude am Ende des vorigen
Teilschritts für die nächste Session ausgegeben hat.

**Selbst testen,** so wie der Plan es für den Teilschritt vorsieht, und immer:

```bash
./mvnw -B -ntp verify              # ohne lokales JDK: docker compose run --rm verify
```

Ist `verify` rot, lasst Claude nachbessern, bevor ihr committet.

Prüft auch, ob der Plan aktualisiert ist: Teilschritt abgehakt, Abweichungen und
neue Entscheidungen eingetragen. Sonst Claude daran erinnern, bevor ihr
committet, denn nach `/clear` ist nur noch der Plan da.

**Committen,** Code und aktualisierten Plan zusammen:

```bash
git add -A && git commit -m "<was der Teilschritt tut>"
```

**Prompt für die nächste Session kopieren.** Claude hat ihn am Ende des
Teilschritts als Codeblock ausgegeben. Fehlt er, fordert ihn an:

```
Gib mir den kurzen Prompt für die nächste Session nach /clear, wie in den
Regeln im Plan beschrieben.
```

**Kontext leeren:**

```
/clear
```

Dann Modell und Effort wählen, wie im kopierten Prompt angegeben, und den
Prompt einfügen. So weiter, bis der Plan durch ist.

### 5 · Abnehmen

```
Prüfe die Umsetzung gegen die Akzeptanzkriterien in issues/02-deposit-return.md.
Pro Kriterium eine Zeile: erfüllt oder nicht, mit einem Beleg als Datei:Zeile
oder Testname. Kurz und klar, nur Fakten.
```

**Fertig, wenn** alle Teilschritte im Plan abgehakt und committet sind,

```bash
./mvnw -B -ntp verify              # ohne lokales JDK: docker compose run --rm verify
```

grün ist und der gestartete Service

```bash
./mvnw spring-boot:run             # ohne lokales JDK: docker compose up --build
```

in einem zweiten Terminal auf

```bash
curl -X POST http://localhost:8080/api/returns -H "Content-Type: application/json" -d "{\"items\":[{\"productId\":\"P-1001\",\"quantity\":6}]}"
```

mit `"totalDepositCents": 150` antwortet.
