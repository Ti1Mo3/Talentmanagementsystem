package com.example.talentmanagement.api.controller;

import com.example.talentmanagement.entity.Wissensgebiet;
import com.example.talentmanagement.service.WissensgebietService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/wissensgebiet")
public class WissensgebietController {
    private final WissensgebietService wissensgebietService;

    @Autowired
    public WissensgebietController(WissensgebietService wissensgebietService) {
        this.wissensgebietService = wissensgebietService;
    }
    @Operation(summary = "Erstellt ein neues Wissensgebiet", description = "Erstellt ein neues Wissensgebiet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wissensgebiet erfolgreich erstellt"),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabe, z.B. wenn der Name bereits existiert")
    })
    @PostMapping
    public ResponseEntity<?> addWissensgebiet(@Valid @RequestBody Wissensgebiet wissensgebiet) {
        return wissensgebietService.addWissensgebiet(wissensgebiet);
    }


    @Operation(summary = "Aktualisiert ein Wissensgebiet", description = "Aktualisiert ein vorhandenes Wissensgebiet anhand seiner ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wissensgebiet erfolgreich aktualisiert"),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabe, z.B. wenn der Name bereits existiert"),
            @ApiResponse(responseCode = "404", description = "Wissensgebiet nicht gefunden")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWissensgebiet(@PathVariable Long id, @Valid @RequestBody Wissensgebiet wissensgebiet) {
        return wissensgebietService.updateWissensgebiet(id, wissensgebiet);
    }


    @Operation(summary = "Löscht ein Wissensgebiet", description = "Löscht ein Wissensgebiet anhand seiner ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Wissensgebiet erfolgreich gelöscht"),
            @ApiResponse(responseCode = "400", description = "Wissensgebiet kann nicht gelöscht werden, da noch Wissensbereiche zugeordnet sind"),
            @ApiResponse(responseCode = "404", description = "Wissensgebiet nicht gefunden")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWissensgebiet(@PathVariable Long id) {
        return wissensgebietService.deleteWissensgebiet(id);
    }
}
