package com.example.talentmanagement.api.controller;

import com.example.talentmanagement.api.dto.WissensbereichDto;
import com.example.talentmanagement.entity.Wissensbereich;
import com.example.talentmanagement.repository.WissensbereichRepository;
import com.example.talentmanagement.repository.WissensgebietRepository;
import com.example.talentmanagement.service.WissensbereichService;
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
    private final WissensbereichService wissensbereichService;
    private final com.example.talentmanagement.repository.WissensbausteinRepository wissensbausteinRepository;

    @Autowired
    public WissensbereichController(WissensbereichRepository wissensbereichRepository, WissensgebietRepository wissensgebietRepository, WissensbereichService wissensbereichService, com.example.talentmanagement.repository.WissensbausteinRepository wissensbausteinRepository) {
        this.wissensbereichRepository = wissensbereichRepository;
        this.wissensgebietRepository = wissensgebietRepository;
        this.wissensbereichService = wissensbereichService;
        this.wissensbausteinRepository = wissensbausteinRepository;
    }

    @Operation(summary = "Fügt einen neuen Wissensbereich hinzu", description = "Legt einen neuen Wissensbereich mit dem angegebenen Namen an. Wissensbausteine werden nicht berücksichtigt.")
    @PostMapping
    public ResponseEntity<?> addWissensbereich(@Valid @RequestBody WissensbereichDto wissensbereichDto) {
        var response = wissensbereichService.addWissensbereich(wissensbereichDto);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
        return response;
    }

    @Operation(summary = "Liefert alle Wissensbereiche", description = "Gibt eine Liste aller vorhandenen Wissensbereiche zurück. Wissensbausteine werden nicht mitgeliefert.")
    @GetMapping
    public ResponseEntity<List<WissensbereichDto>> getAllWissensbereiche() {
        return ResponseEntity.ok(wissensbereichService.getAllWissensbereiche());
    }

    @Operation(summary = "Aktualisiert einen Wissensbereich", description = "Aktualisiert Name, Einarbeitung und weitere Felder eines bestehenden Wissensbereichs anhand der ID. Wissensbausteine werden nicht verändert.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWissensbereich(@PathVariable Long id, @Valid @RequestBody WissensbereichDto wissensbereichDto) {
        var response = wissensbereichService.updateWissensbereich(id, wissensbereichDto);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
        return response;
    }

    @Operation(summary = "Löscht einen Wissensbereich", description = "Löscht einen Wissensbereich anhand der ID. Der Wissensbereich darf nicht gelöscht werden, wenn ihm mindestens ein Wissensbaustein zugewiesen ist.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWissensbereich(@PathVariable Long id) {
        if (!wissensbereichRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Der Wissensbereich mit der angegebenen ID wurde nicht gefunden.");
        }
        if (wissensbausteinRepository.existsByWissensbereich_Id(id)) {
            return ResponseEntity.badRequest().body("Um den Wissensbereich zu löschen, müssen zuerst alle zugehörigen Wissensbausteine gelöscht werden.");
        }
        wissensbereichRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Liste der Wissensbereiche zu einem Wissensgebiet", description = "Gibt alle Wissensbereiche für ein bestimmtes Wissensgebiet zurück. Wissensbausteine werden nicht mitgeliefert.")
    @GetMapping("/byWissensgebiet/{wissensgebietId}")
    public ResponseEntity<List<WissensbereichDto>> getWissensbereicheByWissensgebiet(@PathVariable Long wissensgebietId) {
        List<WissensbereichDto> result = wissensbereichService.getWissensbereicheByWissensgebiet(wissensgebietId);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    private WissensbereichDto toDto(Wissensbereich entity) {
        WissensbereichDto dto = new WissensbereichDto();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.einarbeitung = entity.getEinarbeitung();
        dto.wissensgebietId = entity.getWissensgebiet() != null ? entity.getWissensgebiet().getId() : null;
        return dto;
    }
}
