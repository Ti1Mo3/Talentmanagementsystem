package com.example.talentmanagement.api.controller;

import com.example.talentmanagement.api.dto.WissensbausteinDto;
import com.example.talentmanagement.entity.Wissensbaustein;
import com.example.talentmanagement.service.WissensbausteinService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/wissensbaustein")
public class WissensbausteinController {
    private final WissensbausteinService wissensbausteinService;

    @Autowired
    public WissensbausteinController(WissensbausteinService wissensbausteinService) {
        this.wissensbausteinService = wissensbausteinService;
    }

    @Operation(summary = "Liefert alle Wissensbausteine")
    @GetMapping
    public ResponseEntity<List<WissensbausteinDto>> getAllWissensbausteine() {
        List<Wissensbaustein> list = wissensbausteinService.getAllWissensbausteine();
        return ResponseEntity.ok(list.stream().map(this::toDto).toList());
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
