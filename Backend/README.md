# Talentmanagement Backend REST API


## Voraussetzungen
* Java: 21
* PostgreSQL (Version 13 oder höher empfohlen, z.B. 13.x, 14.x, 15.x, 16.x, 17.x)

**Wichtig:** Die Datenbank "Talentmanagement" muss in PostgreSQL existieren, bevor die Anwendung gestartet wird.
Du kannst sie z.B. mit folgendem Befehl anlegen:

```sql
CREATE DATABASE "Talentmanagement";
```

**Zusätzlich:** Das Schema `talentmanagement` muss in der Datenbank existieren. Falls es nicht vorhanden ist, kann es mit folgendem Befehl angelegt werden:

```sql
CREATE SCHEMA talentmanagement;
```

Alternativ kannst du den Datenbanknamen in der `src/main/resources/application.properties` anpassen.

Dieses Projekt ist die Backend REST API für das Talentmanagementsystem.

## Starten

Mit folgendem Befehl im Projektverzeichnis starten:

```
./mvnw spring-boot:run
```

## Hinweise
- Die REST API ist unter `/api` erreichbar.
- Weitere Konfigurationen in `src/main/resources/application.properties`.

## API Dokumentation (Swagger)

Nach dem Starten der Anwendung ist die interaktive API-Dokumentation über Swagger UI erreichbar:

- [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Mit Swagger kannst du alle Endpunkte testen und die API-Struktur einsehen.
