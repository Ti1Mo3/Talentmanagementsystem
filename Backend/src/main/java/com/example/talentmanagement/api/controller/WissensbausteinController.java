package com.example.talentmanagement.api;

import com.example.talentmanagement.api.dto.WissensbausteinDto;
import com.example.talentmanagement.entity.Wissensbaustein;
import com.example.talentmanagement.entity.Wissensbereich;
import com.example.talentmanagement.service.WissensbausteinService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/wissensbaustein")
public class WissensbausteinController {
    private final WissensbausteinService wissensbausteinService;

    @Autowired
    public WissensbausteinController(WissensbausteinService wissensbausteinService) {
        this.wissensbausteinService = wissensbausteinService;
    }

    @Operation(summary = "Fügt einen neuen Wissensbaustein hinzu")
    @PostMapping
    public ResponseEntity<?> addWissensbaustein(@Valid @RequestBody WissensbausteinDto dto) {
        Object result = wissensbausteinService.addWissensbaustein(dto);
        if (result instanceof String) {
            return ResponseEntity.badRequest().body(result);
        }
        Wissensbaustein saved = (Wissensbaustein) result;
        return ResponseEntity.ok(toDto(saved));
    }

    @Operation(summary = "Liefert alle Wissensbausteine")
    @GetMapping
    public ResponseEntity<List<WissensbausteinDto>> getAllWissensbausteine() {
        List<Wissensbaustein> list = wissensbausteinService.getAllWissensbausteine();
        return ResponseEntity.ok(list.stream().map(this::toDto).toList());
    }

    @Operation(summary = "Aktualisiert einen Wissensbaustein")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWissensbaustein(@PathVariable Long id, @Valid @RequestBody WissensbausteinDto dto) {
        Object result = wissensbausteinService.updateWissensbaustein(id, dto);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        if (result instanceof String) {
            return ResponseEntity.badRequest().body(result);
        }
        Wissensbaustein updated = (Wissensbaustein) result;
        return ResponseEntity.ok(toDto(updated));
    }

    @Operation(summary = "Löscht einen Wissensbaustein")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWissensbaustein(@PathVariable Long id) {
        boolean deleted = wissensbausteinService.deleteWissensbaustein(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Liste der Wissensbausteine zu einem Wissensbereich")
    @GetMapping("/byWissensbereich/{wissensbereichId}")
    public ResponseEntity<List<Wissensbaustein>> getWissensbausteineByWissensbereich(@PathVariable Long wissensbereichId) {
        List<Wissensbaustein> bausteine = wissensbausteinService.getWissensbausteineByWissensbereich(wissensbereichId);
        if (bausteine == null || bausteine.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
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
        dto.wissensgebietId = (entity.getWissensbereich() != null && entity.getWissensbereich().getWissensgebiet() != null)
            ? entity.getWissensbereich().getWissensgebiet().getId()
            : null;
        return dto;
    }
}
