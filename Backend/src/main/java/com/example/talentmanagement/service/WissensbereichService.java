package com.example.talentmanagement.service;

import com.example.talentmanagement.api.dto.WissensbereichDto;
import com.example.talentmanagement.entity.Wissensbereich;
import com.example.talentmanagement.entity.Wissensgebiet;
import com.example.talentmanagement.repository.WissensbereichRepository;
import com.example.talentmanagement.repository.WissensgebietRepository;
import com.example.talentmanagement.repository.WissensbausteinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WissensbereichService {
    private final WissensbereichRepository wissensbereichRepository;
    private final WissensgebietRepository wissensgebietRepository;
    private final WissensbausteinRepository wissensbausteinRepository;

    @Autowired
    public WissensbereichService(WissensbereichRepository wissensbereichRepository, WissensgebietRepository wissensgebietRepository, WissensbausteinRepository wissensbausteinRepository) {
        this.wissensbereichRepository = wissensbereichRepository;
        this.wissensgebietRepository = wissensgebietRepository;
        this.wissensbausteinRepository = wissensbausteinRepository;
    }

    public WissensbereichDto toDto(Wissensbereich entity) {
        WissensbereichDto dto = new WissensbereichDto();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.einarbeitung = entity.getEinarbeitung();
        dto.wissensgebietId = entity.getWissensgebiet() != null ? entity.getWissensgebiet().getId() : null;
        return dto;
    }

    private Wissensbereich fromDto(WissensbereichDto dto) {
        Wissensbereich entity = new Wissensbereich();
        entity.setId(dto.id);
        entity.setName(dto.name);
        entity.setEinarbeitung(dto.einarbeitung);
        if (dto.wissensgebietId != null) {
            Wissensgebiet gebiet = wissensgebietRepository.findById(dto.wissensgebietId).orElse(null);
            entity.setWissensgebiet(gebiet);
        }
        return entity;
    }

    private ResponseEntity<String> badRequest(String message) {
        return ResponseEntity.badRequest().body(message);
    }

    public ResponseEntity<?> addWissensbereich(WissensbereichDto dto) {
        Wissensbereich wissensbereich = fromDto(dto);
        wissensbereich.setId(null);
        Wissensgebiet gebiet = wissensbereich.getWissensgebiet();
        if (gebiet != null && gebiet.getId() != null) {
            var optGebiet = wissensgebietRepository.findById(gebiet.getId());
            if (optGebiet.isPresent()) {
                gebiet = optGebiet.get();
                if (Boolean.TRUE.equals(wissensbereich.getEinarbeitung()) && !Boolean.TRUE.equals(gebiet.getEinarbeitung())) {
                    return badRequest("Einarbeitung kann nur gesetzt werden, wenn das zugehörige Wissensgebiet ebenfalls für die Einarbeitung vorgesehen ist.");
                }
                boolean exists = wissensbereichRepository.existsByNameAndWissensgebiet(wissensbereich.getName(), gebiet);
                if (exists) {
                    return badRequest("Ein Wissensbereich mit diesem Namen und Wissensgebiet existiert bereits.");
                }
                wissensbereich.setWissensgebiet(gebiet);
            } else {
                return badRequest("Das gewählte Wissensgebiet existiert nicht.");
            }
        }
        Wissensbereich saved = wissensbereichRepository.save(wissensbereich);
        return ResponseEntity.ok(toDto(saved));
    }

    public ResponseEntity<?> updateWissensbereich(Long id, WissensbereichDto dto) {
        var existingOpt = wissensbereichRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Der Wissensbereich mit der angegebenen ID wurde nicht gefunden.");
        }
        Wissensbereich existing = existingOpt.get();
        Wissensbereich wissensbereich = fromDto(dto);
        Wissensgebiet gebiet = null;
        Long gebietId = wissensbereich.getWissensgebiet() != null ? wissensbereich.getWissensgebiet().getId() : null;
        if (gebietId != null) {
            gebiet = wissensgebietRepository.findById(gebietId).orElse(null);
        }
        if (Boolean.TRUE.equals(wissensbereich.getEinarbeitung()) && (gebiet == null || !Boolean.TRUE.equals(gebiet.getEinarbeitung()))) {
            return badRequest("Einarbeitung kann nur gesetzt werden, wenn das zugehörige Wissensgebiet ebenfalls für die Einarbeitung vorgesehen ist.");
        }
        boolean exists = wissensbereichRepository.existsByNameAndWissensgebietAndIdNot(wissensbereich.getName(), gebiet, id);
        if (exists) {
            return badRequest("Ein Wissensbereich mit diesem Namen und Wissensgebiet existiert bereits.");
        }
        Boolean einarbeitungAlt = existing.getEinarbeitung() != null ? existing.getEinarbeitung() : false;
        Boolean einarbeitungNeu = wissensbereich.getEinarbeitung() != null ? wissensbereich.getEinarbeitung() : false;
        existing.setName(wissensbereich.getName());
        existing.setEinarbeitung(wissensbereich.getEinarbeitung());
        existing.setWissensgebiet(gebiet);
        Wissensbereich updated = wissensbereichRepository.save(existing);
        // Wenn Einarbeitung von true auf false geändert wurde, alle zugehörigen Wissensbausteine ebenfalls auf false setzen
        if (einarbeitungAlt && !einarbeitungNeu && existing.getWissensbausteine() != null) {
            var bausteine = existing.getWissensbausteine();
            boolean changed = false;
            for (var baustein : bausteine) {
                if (Boolean.TRUE.equals(baustein.getEinarbeitung())) {
                    baustein.setEinarbeitung(false);
                    changed = true;
                }
            }
            if (changed) {
                wissensbausteinRepository.saveAll(bausteine);
            }
        }
        return ResponseEntity.ok(toDto(updated));
    }

    public ResponseEntity<?> deleteWissensbereich(Long id) {
        if (!wissensbereichRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Der Wissensbereich mit der angegebenen ID wurde nicht gefunden.");
        }
        if (wissensbausteinRepository.existsByWissensbereich_Id(id)) {
            return badRequest("Um den Wissensbereich zu löschen, müssen zuerst alle zugehörigen Wissensbausteine gelöscht werden.");
        }
        wissensbereichRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public List<WissensbereichDto> getAllWissensbereiche() {
        return wissensbereichRepository.findAllByOrderByWissensgebiet_NameAscNameAsc()
                .stream().map(this::toDto).toList();
    }

    public List<WissensbereichDto> getWissensbereicheByWissensgebiet(Long wissensgebietId) {
        if (!wissensgebietRepository.existsById(wissensgebietId)) {
            return List.of();
        }
        return wissensbereichRepository.findByWissensgebiet_IdOrderByNameAsc(wissensgebietId)
                .stream().map(this::toDto).toList();
    }
}
