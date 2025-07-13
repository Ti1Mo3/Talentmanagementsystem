package com.example.talentmanagement.api;

import com.example.talentmanagement.api.dto.WissensbausteinDto;
import com.example.talentmanagement.entity.Wissensbaustein;
import com.example.talentmanagement.entity.Wissensbereich;
import com.example.talentmanagement.repository.WissensbausteinRepository;
import com.example.talentmanagement.repository.WissensbereichRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/wissensbaustein")
public class WissensbausteinController {
    private final WissensbausteinRepository wissensbausteinRepository;
    private final WissensbereichRepository wissensbereichRepository;

    @Autowired
    public WissensbausteinController(WissensbausteinRepository wissensbausteinRepository, WissensbereichRepository wissensbereichRepository) {
        this.wissensbausteinRepository = wissensbausteinRepository;
        this.wissensbereichRepository = wissensbereichRepository;
    }

    @Operation(summary = "Fügt einen neuen Wissensbaustein hinzu")
    @PostMapping
    public ResponseEntity<?> addWissensbaustein(@Valid @RequestBody WissensbausteinDto dto) {
        Wissensbaustein wissensbaustein = new Wissensbaustein();
        wissensbaustein.setName(dto.name);
        wissensbaustein.setLevel(Wissensbaustein.Level.valueOf(dto.level));
        wissensbaustein.setEinarbeitung(dto.einarbeitung);
        if (dto.reihenfolge < 1 || dto.reihenfolge > 10) {
            return ResponseEntity.badRequest().body("Reihenfolge muss zwischen 1 und 10 liegen.");
        }
        wissensbaustein.setReihenfolge(Wissensbaustein.Reihenfolge.values()[dto.reihenfolge - 1]);
        if (dto.wissensbereichId == null) {
            return ResponseEntity.badRequest().body("Wissensbereich muss angegeben werden.");
        }
        Wissensbereich bereich = wissensbereichRepository.findById(dto.wissensbereichId).orElse(null);
        if (bereich == null) {
            return ResponseEntity.badRequest().body("Wissensbereich nicht gefunden.");
        }
        boolean exists = wissensbausteinRepository.existsByNameAndWissensbereich(dto.name, bereich);
        if (exists) {
            return ResponseEntity.badRequest().body("Ein Wissensbaustein mit diesem Namen existiert bereits in diesem Wissensbereich.");
        }
        wissensbaustein.setWissensbereich(bereich);
        Wissensbaustein saved = wissensbausteinRepository.save(wissensbaustein);
        return ResponseEntity.ok(toDto(saved));
    }

    @Operation(summary = "Liefert alle Wissensbausteine")
    @GetMapping
    public ResponseEntity<List<WissensbausteinDto>> getAllWissensbausteine() {
        List<Wissensbaustein> list = wissensbausteinRepository.findAll();
        return ResponseEntity.ok(list.stream().map(this::toDto).toList());
    }

    @Operation(summary = "Aktualisiert einen Wissensbaustein")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWissensbaustein(@PathVariable Long id, @Valid @RequestBody WissensbausteinDto dto) {
        return wissensbausteinRepository.findById(id)
            .map(existing -> {
                Wissensbereich bereich = null;
                if (dto.wissensbereichId != null) {
                    bereich = wissensbereichRepository.findById(dto.wissensbereichId).orElse(null);
                }
                if (bereich == null) {
                    return ResponseEntity.badRequest().body("Wissensbereich nicht gefunden.");
                }
                boolean exists = wissensbausteinRepository.existsByNameAndWissensbereich(dto.name, bereich);
                if (exists && !existing.getId().equals(id)) {
                    return ResponseEntity.badRequest().body("Ein Wissensbaustein mit diesem Namen existiert bereits in diesem Wissensbereich.");
                }
                existing.setName(dto.name);
                existing.setLevel(Wissensbaustein.Level.valueOf(dto.level));
                existing.setEinarbeitung(dto.einarbeitung);
                if (dto.reihenfolge < 1 || dto.reihenfolge > 10) {
                    return ResponseEntity.badRequest().body("Reihenfolge muss zwischen 1 und 10 liegen.");
                }
                existing.setReihenfolge(Wissensbaustein.Reihenfolge.values()[dto.reihenfolge - 1]);
                existing.setWissensbereich(bereich);
                Wissensbaustein updated = wissensbausteinRepository.save(existing);
                return ResponseEntity.ok(toDto(updated));
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Löscht einen Wissensbaustein")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWissensbaustein(@PathVariable Long id) {
        if (wissensbausteinRepository.existsById(id)) {
            wissensbausteinRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Liste der Wissensbausteine zu einem Wissensbereich")
    @GetMapping("/byWissensbereich/{wissensbereichId}")
    public ResponseEntity<List<Wissensbaustein>> getWissensbausteineByWissensbereich(@PathVariable Long wissensbereichId) {
        if (!wissensbereichRepository.existsById(wissensbereichId)) {
            return ResponseEntity.notFound().build();
        }
        List<Wissensbaustein> bausteine = wissensbausteinRepository.findByWissensbereich_IdOrderByReihenfolgeAsc(wissensbereichId);
        return ResponseEntity.ok(bausteine);
    }

    private WissensbausteinDto toDto(Wissensbaustein entity) {
        WissensbausteinDto dto = new WissensbausteinDto();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.level = entity.getLevel().name();
        dto.einarbeitung = entity.getEinarbeitung();
        dto.reihenfolge = entity.getReihenfolge().getValue();
        dto.wissensbereichId = entity.getWissensbereich() != null ? entity.getWissensbereich().getId() : null;
        return dto;
    }
}
