package com.example.talentmanagement.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Wissensbereich {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Name des Wissensbereichs", example = "SAP Entwicklung", required = true)
    @Column(nullable = false)
    private String name;

    @Schema(description = "Zugehöriges Wissensgebiet (nur ID angeben)", implementation = WissensgebietIdOnly.class)
    @ManyToOne
    @JoinColumn(name = "wissensgebiet_id")
    private Wissensgebiet wissensgebiet;

    @Schema(description = "Liste der zugeordneten Wissensbausteine (optional)", required = false)
    @OneToMany(mappedBy = "wissensbereich")
    private List<Wissensbaustein> wissensbausteine;

    public Wissensbereich() {}

    public Wissensbereich(String name) {
        this.name = name;
    }

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Wissensgebiet getWissensgebiet() {
        return wissensgebiet;
    }

    public void setWissensgebiet(Wissensgebiet wissensgebiet) {
        this.wissensgebiet = wissensgebiet;
    }

    public List<Wissensbaustein> getWissensbausteine() {
        return wissensbausteine;
    }

    public void setWissensbausteine(List<Wissensbaustein> wissensbausteine) {
        this.wissensbausteine = wissensbausteine;
    }

    // Hilfsklasse für OpenAPI, damit nur die ID im Request erwartet wird
    public static class WissensgebietIdOnly {
        @Schema(description = "ID des Wissensgebiets", example = "1", required = true)
        public Long id;
    }
}
