package com.example.talentmanagement.api.controller;

import com.example.talentmanagement.api.dto.WissensbausteinDto;
import com.example.talentmanagement.entity.Wissensbaustein;
import com.example.talentmanagement.service.WissensbausteinService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/wissensbaustein")
public class WissensbausteinController {
    private final WissensbausteinService wissensbausteinService;

    @Autowired
    public WissensbausteinController(WissensbausteinService wissensbausteinService) {
        this.wissensbausteinService = wissensbausteinService;
    }

    @Operation(summary = "Liefert alle Wissensbausteine", description = "Gibt eine Liste aller vorhandenen Wissensbausteine zurück.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wissensbausteine erfolgreich abgerufen")
    })
    @GetMapping
    public ResponseEntity<List<WissensbausteinDto>> getAllWissensbausteine() {
        List<Wissensbaustein> list = wissensbausteinService.getAllWissensbausteine();
        return ResponseEntity.ok(list.stream().map(this::toDto).toList());
    }

    @Operation(summary = "Erstellt einen neuen Wissensbaustein", description = "Erstellt einen neuen Wissensbaustein.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wissensbaustein erfolgreich erstellt"),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabe")
    })
    @PostMapping
    public ResponseEntity<?> addWissensbaustein(@Valid @RequestBody WissensbausteinDto wissensbausteinDto) {
        return wissensbausteinService.addWissensbaustein(wissensbausteinDto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWissensbaustein(@PathVariable Long id, @Valid @RequestBody WissensbausteinDto wissensbausteinDto) {
        return wissensbausteinService.updateWissensbaustein(id, wissensbausteinDto);
    }

    @Operation(summary = "Löscht einen Wissensbaustein", description = "Löscht einen Wissensbaustein anhand seiner ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Wissensbaustein erfolgreich gelöscht"),
            @ApiResponse(responseCode = "404", description = "Wissensbaustein nicht gefunden")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWissensbaustein(@PathVariable Long id) {
        return wissensbausteinService.deleteWissensbaustein(id);
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
