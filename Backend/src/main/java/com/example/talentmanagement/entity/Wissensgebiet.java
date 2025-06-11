package com.example.talentmanagement.entity;

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

    // Getter, Setter, Constructor
}
