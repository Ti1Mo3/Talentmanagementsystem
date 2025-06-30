package com.example.talentmanagement.api;

import com.example.talentmanagement.entity.Wissensgebiet;
import com.example.talentmanagement.repository.WissensgebietRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/wissensgebiet")
public class WissensgebietController {
    private final WissensgebietRepository wissensgebietRepository;

    @Autowired
    public WissensgebietController(WissensgebietRepository wissensgebietRepository) {
        this.wissensgebietRepository = wissensgebietRepository;
    }

    @Operation(summary = "Fügt ein neues Wissensgebiet hinzu", description = "Legt ein neues Wissensgebiet mit dem angegebenen Namen und Einarbeitung an.")
    @PostMapping
    public ResponseEntity<?> addWissensgebiet(@Valid @RequestBody Wissensgebiet wissensgebiet) {
        if (wissensgebietRepository.findAll().stream().anyMatch(wg -> wg.getName().equalsIgnoreCase(wissensgebiet.getName()))) {
            return ResponseEntity.badRequest().body("Ein Wissensgebiet mit diesem Namen existiert bereits.");
        }
        Wissensgebiet saved = wissensgebietRepository.save(wissensgebiet);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Liefert alle Wissensgebiete", description = "Gibt eine Liste aller vorhandenen Wissensgebiete inklusive Einarbeitung zurück.")
    @GetMapping
    public ResponseEntity<List<Wissensgebiet>> getAllWissensgebiete() {
        List<Wissensgebiet> list = wissensgebietRepository.findAllByOrderByIdDesc();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Aktualisiert ein Wissensgebiet", description = "Aktualisiert den Namen und das Einarbeitung-Flag eines bestehenden Wissensgebiets anhand der ID.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWissensgebiet(@PathVariable Long id, @Valid @RequestBody Wissensgebiet wissensgebiet) {
        // Eindeutigkeitsprüfung für Name (außer beim eigenen Datensatz)
        if (wissensgebietRepository.existsByNameAndIdNot(wissensgebiet.getName(), id)) {
            return ResponseEntity.badRequest().body("Ein Wissensgebiet mit diesem Namen existiert bereits.");
        }
        return wissensgebietRepository.findById(id)
            .map(existing -> {
                existing.setName(wissensgebiet.getName());
                existing.setEinarbeitung(wissensgebiet.getEinarbeitung());
                Wissensgebiet updated = wissensgebietRepository.save(existing);
                return ResponseEntity.ok(updated);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Löscht ein Wissensgebiet", description = "Löscht ein Wissensgebiet anhand der ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWissensgebiet(@PathVariable Long id) {
        if (wissensgebietRepository.existsById(id)) {
            wissensgebietRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
