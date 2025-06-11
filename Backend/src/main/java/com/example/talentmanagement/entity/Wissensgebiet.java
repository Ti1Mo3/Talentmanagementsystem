package com.example.talentmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Wissensgebiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

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

    @JsonIgnore
    public List<Wissensbereich> getWissensbereiche() {
        return wissensbereiche;
    }

    public void setWissensbereiche(List<Wissensbereich> wissensbereiche) {
        this.wissensbereiche = wissensbereiche;
    }
}
