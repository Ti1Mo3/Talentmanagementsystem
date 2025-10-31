package com.example.talentmanagement.service;

import com.example.talentmanagement.api.dto.WissensbausteinDto;
import com.example.talentmanagement.entity.Wissensbaustein;
import com.example.talentmanagement.entity.Wissensbereich;
import com.example.talentmanagement.repository.WissensbausteinRepository;
import com.example.talentmanagement.repository.WissensbereichRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WissensbausteinService {
    private final WissensbausteinRepository wissensbausteinRepository;
    private final WissensbereichRepository wissensbereichRepository;

    @Autowired
    public WissensbausteinService(WissensbausteinRepository wissensbausteinRepository, WissensbereichRepository wissensbereichRepository) {
        this.wissensbausteinRepository = wissensbausteinRepository;
        this.wissensbereichRepository = wissensbereichRepository;
    }

    public WissensbausteinDto toDto(Wissensbaustein entity) {
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

    private ResponseEntity<String> badRequest(String message) {
        return ResponseEntity.badRequest().body(message);
    }

    public ResponseEntity<?> addWissensbaustein(WissensbausteinDto dto) {
        Wissensbaustein wissensbaustein = new Wissensbaustein();
        wissensbaustein.setName(dto.name);
        wissensbaustein.setLevel(Wissensbaustein.Level.valueOf(dto.level));
        if (dto.reihenfolge < 1 || dto.reihenfolge > 10) {
            return badRequest("Reihenfolge muss zwischen 1 und 10 liegen.");
        }
        wissensbaustein.setReihenfolge(Wissensbaustein.Reihenfolge.values()[dto.reihenfolge - 1]);
        if (dto.wissensbereichId == null || dto.wissensgebietId == null) {
            return badRequest("Wissensbereich und Wissensgebiet müssen angegeben werden.");
        }
        Wissensbereich bereich = wissensbereichRepository.findById(dto.wissensbereichId).orElse(null);
        if (bereich == null) {
            return badRequest("Wissensbereich nicht gefunden.");
        }
        if (bereich.getWissensgebiet() == null || !bereich.getWissensgebiet().getId().equals(dto.wissensgebietId)) {
            return badRequest("Der Wissensbereich gehört nicht zum angegebenen Wissensgebiet.");
        }
        // Einarbeitung nur erlauben, wenn der Bereich für Einarbeitung vorgesehen ist
        if (dto.einarbeitung && (bereich.getEinarbeitung() == null || !bereich.getEinarbeitung())) {
            return badRequest("Einarbeitung kann nur gesetzt werden, wenn der zugehörige Wissensbereich für Einarbeitung vorgesehen ist.");
        }
        wissensbaustein.setEinarbeitung(dto.einarbeitung);
        // Prüfe, ob die Kombination aus Name, Wissensbereich und Wissensgebiet eindeutig ist
        boolean exists = wissensbausteinRepository.existsByNameAndWissensbereich_NameAndWissensbereich_Wissensgebiet_Id(
            dto.name, bereich.getName(), dto.wissensgebietId
        );
        if (exists) {
            return badRequest("Ein Wissensbaustein mit diesem Namen existiert bereits in dieser Kombination aus Wissensgebiet und Wissensbereich.");
        }
        wissensbaustein.setWissensbereich(bereich);
        Wissensbaustein saved = wissensbausteinRepository.save(wissensbaustein);
        return ResponseEntity.ok(toDto(saved));
    }

    public List<Wissensbaustein> getAllWissensbausteine() {
        List<Wissensbaustein> bausteine = wissensbausteinRepository.findAll();
        return bausteine;
    }

    public ResponseEntity<?> updateWissensbaustein(Long id, WissensbausteinDto dto) {
        Optional<Wissensbaustein> opt = wissensbausteinRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Wissensbaustein existing = opt.get();
        Wissensbereich bereich = null;
        if (dto.wissensbereichId != null && dto.wissensgebietId != null) {
            bereich = wissensbereichRepository.findById(dto.wissensbereichId).orElse(null);
            if (bereich == null) {
                return badRequest("Wissensbereich nicht gefunden.");
            }
            if (bereich.getWissensgebiet() == null || !bereich.getWissensgebiet().getId().equals(dto.wissensgebietId)) {
                return badRequest("Der Wissensbereich gehört nicht zum angegebenen Wissensgebiet.");
            }
        } else {
            return badRequest("Wissensbereich und Wissensgebiet müssen angegeben werden.");
        }
        // Einarbeitung nur erlauben, wenn der Bereich für Einarbeitung vorgesehen ist
        if (dto.einarbeitung && (bereich.getEinarbeitung() == null || !bereich.getEinarbeitung())) {
            return badRequest("Einarbeitung kann nur gesetzt werden, wenn der zugehörige Wissensbereich für Einarbeitung vorgesehen ist.");
        }
        boolean exists = wissensbausteinRepository.existsByNameAndWissensbereich_NameAndWissensbereich_Wissensgebiet_Id(
            dto.name, bereich.getName(), dto.wissensgebietId
        );
        if (exists && !existing.getId().equals(id)) {
            return badRequest("Ein Wissensbaustein mit diesem Namen existiert bereits in dieser Kombination aus Wissensgebiet und Wissensbereich.");
        }
        existing.setName(dto.name);
        existing.setLevel(Wissensbaustein.Level.valueOf(dto.level));
        if (dto.reihenfolge < 1 || dto.reihenfolge > 10) {
            return badRequest("Reihenfolge muss zwischen 1 und 10 liegen.");
        }
        existing.setReihenfolge(Wissensbaustein.Reihenfolge.values()[dto.reihenfolge - 1]);
        existing.setEinarbeitung(dto.einarbeitung);
        existing.setWissensbereich(bereich);
        Wissensbaustein updated = wissensbausteinRepository.save(existing);
        return ResponseEntity.ok(toDto(updated));
    }

    public ResponseEntity<?> deleteWissensbaustein(Long id) {
        if (!wissensbausteinRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        wissensbausteinRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public List<Wissensbaustein> getWissensbausteineByWissensbereich(Long wissensbereichId) {
        return wissensbausteinRepository.findByWissensbereich_IdOrderByReihenfolgeAsc(wissensbereichId);
    }
}
