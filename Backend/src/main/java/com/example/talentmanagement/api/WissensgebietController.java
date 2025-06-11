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

    @Operation(summary = "Fügt ein neues Wissensgebiet hinzu", description = "Legt ein neues Wissensgebiet mit dem angegebenen Namen an.")
    @PostMapping
    public ResponseEntity<Wissensgebiet> addWissensgebiet(@Valid @RequestBody Wissensgebiet wissensgebiet) {
        Wissensgebiet saved = wissensgebietRepository.save(wissensgebiet);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Liefert alle Wissensgebiete", description = "Gibt eine Liste aller vorhandenen Wissensgebiete zurück.")
    @GetMapping
    public ResponseEntity<List<Wissensgebiet>> getAllWissensgebiete() {
        List<Wissensgebiet> list = wissensgebietRepository.findAll();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Aktualisiert ein Wissensgebiet", description = "Aktualisiert den Namen eines bestehenden Wissensgebiets anhand der ID.")
    @PutMapping("/{id}")
    public ResponseEntity<Wissensgebiet> updateWissensgebiet(@PathVariable Long id, @Valid @RequestBody Wissensgebiet wissensgebiet) {
        return wissensgebietRepository.findById(id)
            .map(existing -> {
                existing.setName(wissensgebiet.getName());
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
