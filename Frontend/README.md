# Vue 3 + TypeScript + Vite

## Projekt starten

### Voraussetzungen

- **Node.js**: Aktuelle LTS-Version (z.B. Node 18 oder Node 20) ist erforderlich, da die verwendeten Pakete (Vite 6, Vue 3, TypeScript 5.8) eine moderne Node-Version voraussetzen.

1. **Abhängigkeiten installieren**  
   Öffne ein Terminal im Projektordner und führe aus:
   ```
   npm install
   ```

2. **Entwicklungsserver starten**  
   Starte das Projekt mit:
   ```
   npm run dev
   ```
   Der lokale Server läuft standardmäßig auf [http://localhost:5173](http://localhost:5173).

3. **Build für Produktion**  
   Erzeuge ein Produktions-Build mit:
   ```
   npm run build
   ```

4. **Preview des Produktions-Builds**  
   Vorschau des Builds:
   ```
   npm run preview
   ```

## Projektstruktur

- `components/` – Wiederverwendbare Vue-Komponenten
- `views/` – Seiten/Views
- `services/` – API- und andere Service-Dateien
- `store/` – Pinia Store für State-Management
- `router/` – Vue Router-Konfiguration

## Weitere Hinweise

 REST-API-Kommunikation erfolgt zentral über `services/apiService.ts` mit Axios.
- State-Management mit Pinia.
