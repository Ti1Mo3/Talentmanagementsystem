package com.example.talentmanagement.api.controller;

import com.example.talentmanagement.entity.Wissensgebiet;
import com.example.talentmanagement.service.WissensgebietService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Liefert alle Wissensgebiete", description = "Gibt eine Liste aller vorhandenen Wissensgebiete inklusive Einarbeitung zurück.")
    @GetMapping
    public ResponseEntity<List<Wissensgebiet>> getAllWissensgebiete() {
        List<Wissensgebiet> list = wissensgebietService.getAllWissensgebiete();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Löscht ein Wissensgebiet", description = "Löscht ein Wissensgebiet anhand seiner ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWissensgebiet(@PathVariable Long id) {
        return wissensgebietService.deleteWissensgebiet(id);
    }
}
