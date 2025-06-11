package com.example.talentmanagement.api;

import com.example.talentmanagement.entity.Wissensbereich;
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
    public ResponseEntity<Wissensbereich> addWissensbereich(@Valid @RequestBody Wissensbereich wissensbereich) {
        // ID aus dem Body entfernen, falls vorhanden
        wissensbereich.setId(null);
        if (wissensbereich.getWissensgebiet() != null && wissensbereich.getWissensgebiet().getId() != null) {
            wissensgebietRepository.findById(wissensbereich.getWissensgebiet().getId()).ifPresent(wissensbereich::setWissensgebiet);
        }
        Wissensbereich saved = wissensbereichRepository.save(wissensbereich);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Liefert alle Wissensbereiche", description = "Gibt eine Liste aller vorhandenen Wissensbereiche zurück.")
    @GetMapping
    public ResponseEntity<List<Wissensbereich>> getAllWissensbereiche() {
        List<Wissensbereich> list = wissensbereichRepository.findAll();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Aktualisiert einen Wissensbereich", description = "Aktualisiert den Namen eines bestehenden Wissensbereichs anhand der ID.")
    @PutMapping("/{id}")
    public ResponseEntity<Wissensbereich> updateWissensbereich(@PathVariable Long id, @Valid @RequestBody Wissensbereich wissensbereich) {
        return wissensbereichRepository.findById(id)
            .map(existing -> {
                // ID aus dem Body ignorieren
                existing.setName(wissensbereich.getName());
                existing.setWissensgebiet(wissensbereich.getWissensgebiet());
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
}
