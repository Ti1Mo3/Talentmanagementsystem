package com.example.talentmanagement.service;

import com.example.talentmanagement.api.dto.WissensbausteinDto;
import com.example.talentmanagement.entity.Wissensbaustein;
import com.example.talentmanagement.entity.Wissensbereich;
import com.example.talentmanagement.repository.WissensbausteinRepository;
import com.example.talentmanagement.repository.WissensbereichRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Object addWissensbaustein(WissensbausteinDto dto) {
        Wissensbaustein wissensbaustein = new Wissensbaustein();
        wissensbaustein.setName(dto.name);
        wissensbaustein.setLevel(Wissensbaustein.Level.valueOf(dto.level));
        wissensbaustein.setEinarbeitung(dto.einarbeitung);
        if (dto.reihenfolge < 1 || dto.reihenfolge > 10) {
            return "Reihenfolge muss zwischen 1 und 10 liegen.";
        }
        wissensbaustein.setReihenfolge(Wissensbaustein.Reihenfolge.values()[dto.reihenfolge - 1]);
        if (dto.wissensbereichId == null) {
            return "Wissensbereich muss angegeben werden.";
        }
        Wissensbereich bereich = wissensbereichRepository.findById(dto.wissensbereichId).orElse(null);
        if (bereich == null) {
            return "Wissensbereich nicht gefunden.";
        }
        boolean exists = wissensbausteinRepository.existsByNameAndWissensbereich(dto.name, bereich);
        if (exists) {
            return "Ein Wissensbaustein mit diesem Namen existiert bereits in diesem Wissensbereich.";
        }
        wissensbaustein.setWissensbereich(bereich);
        Wissensbaustein saved = wissensbausteinRepository.save(wissensbaustein);
        return saved;
    }

    public List<Wissensbaustein> getAllWissensbausteine() {
        return wissensbausteinRepository.findAll();
    }

    public Object updateWissensbaustein(Long id, WissensbausteinDto dto) {
        Optional<Wissensbaustein> opt = wissensbausteinRepository.findById(id);
        if (opt.isEmpty()) return null;
        Wissensbaustein existing = opt.get();
        Wissensbereich bereich = null;
        if (dto.wissensbereichId != null) {
            bereich = wissensbereichRepository.findById(dto.wissensbereichId).orElse(null);
        }
        if (bereich == null) {
            return "Wissensbereich nicht gefunden.";
        }
        boolean exists = wissensbausteinRepository.existsByNameAndWissensbereich(dto.name, bereich);
        if (exists && !existing.getId().equals(id)) {
            return "Ein Wissensbaustein mit diesem Namen existiert bereits in diesem Wissensbereich.";
        }
        existing.setName(dto.name);
        existing.setLevel(Wissensbaustein.Level.valueOf(dto.level));
        existing.setEinarbeitung(dto.einarbeitung);
        if (dto.reihenfolge < 1 || dto.reihenfolge > 10) {
            return "Reihenfolge muss zwischen 1 und 10 liegen.";
        }
        existing.setReihenfolge(Wissensbaustein.Reihenfolge.values()[dto.reihenfolge - 1]);
        existing.setWissensbereich(bereich);
        Wissensbaustein updated = wissensbausteinRepository.save(existing);
        return updated;
    }

    public boolean deleteWissensbaustein(Long id) {
        if (wissensbausteinRepository.existsById(id)) {
            wissensbausteinRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<Wissensbaustein> getWissensbausteineByWissensbereich(Long wissensbereichId) {
        return wissensbausteinRepository.findByWissensbereich_IdOrderByReihenfolgeAsc(wissensbereichId);
    }
}
