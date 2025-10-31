package com.example.talentmanagement.api.controller;

import com.example.talentmanagement.api.dto.WissensbereichDto;
import com.example.talentmanagement.entity.Wissensbereich;
import com.example.talentmanagement.repository.WissensbereichRepository;
import com.example.talentmanagement.repository.WissensgebietRepository;
import com.example.talentmanagement.service.WissensbereichService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Liefert alle Wissensbereiche", description = "Gibt eine Liste aller vorhandenen Wissensbereiche zurück. Wissensbausteine werden nicht mitgeliefert.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wissensbereiche erfolgreich abgerufen")
    })
    @GetMapping
    public ResponseEntity<List<WissensbereichDto>> getAllWissensbereiche() {
        return ResponseEntity.ok(wissensbereichService.getAllWissensbereiche());
    }

    @Operation(summary = "Erstellt einen neuen Wissensbereich", description = "Erstellt einen neuen Wissensbereich.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wissensbereich erfolgreich erstellt"),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabe, z.B. wenn der Name bereits existiert oder das Wissensgebiet nicht für Einarbeitung markiert ist")
    })
    @PostMapping
    public ResponseEntity<?> addWissensbereich(@Valid @RequestBody WissensbereichDto wissensbereich) {
        return wissensbereichService.addWissensbereich(wissensbereich);
    }

    @Operation(summary = "Aktualisiert einen Wissensbereich", description = "Aktualisiert einen vorhandenen Wissensbereich anhand seiner ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wissensbereich erfolgreich aktualisiert"),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabe"),
            @ApiResponse(responseCode = "404", description = "Wissensbereich nicht gefunden")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWissensbereich(@PathVariable Long id, @Valid @RequestBody WissensbereichDto wissensbereich) {
        return wissensbereichService.updateWissensbereich(id, wissensbereich);
    }

    @Operation(summary = "Löscht einen Wissensbereich", description = "Löscht einen Wissensbereich anhand seiner ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Wissensbereich erfolgreich gelöscht"),
            @ApiResponse(responseCode = "400", description = "Wissensbereich kann nicht gelöscht werden, da noch Wissensbausteine zugeordnet sind"),
            @ApiResponse(responseCode = "404", description = "Wissensbereich nicht gefunden")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWissensbereich(@PathVariable Long id) {
        return wissensbereichService.deleteWissensbereich(id);
    }
}
