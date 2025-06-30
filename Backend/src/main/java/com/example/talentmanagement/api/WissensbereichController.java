package com.example.talentmanagement.api;

import com.example.talentmanagement.entity.Wissensbereich;
import com.example.talentmanagement.entity.Wissensgebiet;
import com.example.talentmanagement.repository.WissensbereichRepository;
import com.example.talentmanagement.repository.WissensgebietRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/wissensbereich")
public class WissensbereichController {
    private final WissensbereichRepository wissensbereichRepository;
    private final WissensgebietRepository wissensgebietRepository;

    @Autowired
    public WissensbereichController(WissensbereichRepository wissensbereichRepository, WissensgebietRepository wissensgebietRepository) {
        this.wissensbereichRepository = wissensbereichRepository;
        this.wissensgebietRepository = wissensgebietRepository;
    }

    @Operation(summary = "Fügt einen neuen Wissensbereich hinzu", description = "Legt einen neuen Wissensbereich mit dem angegebenen Namen an.")
    @PostMapping
    public ResponseEntity<?> addWissensbereich(@Valid @RequestBody Wissensbereich wissensbereich) {
        // ID aus dem Body entfernen, falls vorhanden
        wissensbereich.setId(null);
        if (wissensbereich.getWissensgebiet() != null && wissensbereich.getWissensgebiet().getId() != null) {
            var optGebiet = wissensgebietRepository.findById(wissensbereich.getWissensgebiet().getId());
            if (optGebiet.isPresent()) {
                Wissensgebiet gebiet = optGebiet.get();
                // Validierung: Einarbeitung darf nur true sein, wenn das Gebiet auch Einarbeitung erlaubt
                if (Boolean.TRUE.equals(wissensbereich.getEinarbeitung()) && !Boolean.TRUE.equals(gebiet.getEinarbeitung())) {
                    return ResponseEntity.badRequest().body("Das zugewiesene Wissensgebiet ist nicht für Einarbeitung vorgesehen.");
                }
                // Prüfe, ob Kombination aus Name und Wissensgebiet bereits existiert
                boolean exists = wissensbereichRepository.existsByNameAndWissensgebiet(wissensbereich.getName(), gebiet);
                if (exists) {
                    return ResponseEntity.badRequest().body("Ein Wissensbereich mit diesem Namen existiert bereits in diesem Wissensgebiet.");
                }
                wissensbereich.setWissensgebiet(gebiet);
            }
        }
        Wissensbereich saved = wissensbereichRepository.save(wissensbereich);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Liefert alle Wissensbereiche", description = "Gibt eine Liste aller vorhandenen Wissensbereiche zurück.")
    @GetMapping
    public ResponseEntity<List<Wissensbereich>> getAllWissensbereiche() {
        List<Wissensbereich> list = wissensbereichRepository.findAllByOrderByIdDesc();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Aktualisiert einen Wissensbereich", description = "Aktualisiert Name, Einarbeitung und weitere Felder eines bestehenden Wissensbereichs anhand der ID.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWissensbereich(@PathVariable Long id, @Valid @RequestBody Wissensbereich wissensbereich) {
        return wissensbereichRepository.findById(id)
            .map(existing -> {
                Wissensgebiet gebiet = null;
                if (wissensbereich.getWissensgebiet() != null && wissensbereich.getWissensgebiet().getId() != null) {
                    gebiet = wissensgebietRepository.findById(wissensbereich.getWissensgebiet().getId()).orElse(null);
                }
                if (Boolean.TRUE.equals(wissensbereich.getEinarbeitung()) && (gebiet == null || !Boolean.TRUE.equals(gebiet.getEinarbeitung()))) {
                    return ResponseEntity.badRequest().body("Das zugewiesene Wissensgebiet ist nicht für Einarbeitung vorgesehen.");
                }
                // Unique-Check: Kombination Name + Wissensgebiet darf nicht doppelt sein (außer beim eigenen Datensatz)
                boolean exists = wissensbereichRepository.existsByNameAndWissensgebietAndIdNot(wissensbereich.getName(), gebiet, id);
                if (exists) {
                    return ResponseEntity.badRequest().body("Ein Wissensbereich mit diesem Namen existiert bereits in diesem Wissensgebiet.");
                }
                // ID aus dem Body ignorieren
                existing.setName(wissensbereich.getName());
                existing.setEinarbeitung(wissensbereich.getEinarbeitung());
                existing.setWissensgebiet(gebiet);
                existing.setWissensbausteine(wissensbereich.getWissensbausteine());
                Wissensbereich updated = wissensbereichRepository.save(existing);
                // Wissensgebiet mit Name für Response setzen
                if (updated.getWissensgebiet() != null && updated.getWissensgebiet().getId() != null) {
                    wissensgebietRepository.findById(updated.getWissensgebiet().getId()).ifPresent(updated::setWissensgebiet);
                }
                return ResponseEntity.ok(updated);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Löscht einen Wissensbereich", description = "Löscht einen Wissensbereich anhand der ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWissensbereich(@PathVariable Long id) {
        if (wissensbereichRepository.existsById(id)) {
            wissensbereichRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Liste der Wissensbereiche zu einem Wissensgebiet", description = "Gibt alle Wissensbereiche für ein bestimmtes Wissensgebiet zurück.")
    @GetMapping("/byWissensgebiet/{wissensgebietId}")
    public ResponseEntity<List<Wissensbereich>> getWissensbereicheByWissensgebiet(@PathVariable Long wissensgebietId) {
        if (!wissensgebietRepository.existsById(wissensgebietId)) {
            return ResponseEntity.notFound().build();
        }
        List<Wissensbereich> bereiche = wissensbereichRepository.findByWissensgebiet_IdOrderByIdDesc(wissensgebietId);
        return ResponseEntity.ok(bereiche);
    }
}
