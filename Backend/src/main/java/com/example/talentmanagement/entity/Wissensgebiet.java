package com.example.talentmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Wissensgebiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Name des Wissensgebiets", example = "ABAP", required = true)
    @Column(nullable = false, unique = true)
    private String name;

    @Schema(description = "Gibt an, ob das Wissensgebiet eingearbeitet werden muss", example = "true", required = true)
    @Column(nullable = false)
    private Boolean einarbeitung = false;

    @OneToMany(mappedBy = "wissensgebiet")
    private List<Wissensbereich> wissensbereiche;

    public Wissensgebiet() {}

    public Wissensgebiet(String name) {
        this.name = name;
    }

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

    public Boolean getEinarbeitung() {
        return einarbeitung;
    }

    public void setEinarbeitung(Boolean einarbeitung) {
        this.einarbeitung = einarbeitung;
    }

    @JsonIgnore
    public List<Wissensbereich> getWissensbereiche() {
        return wissensbereiche;
    }

    public void setWissensbereiche(List<Wissensbereich> wissensbereiche) {
        this.wissensbereiche = wissensbereiche;
    }
}
