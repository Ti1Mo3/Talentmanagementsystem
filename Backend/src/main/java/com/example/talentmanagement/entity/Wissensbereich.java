package com.example.talentmanagement.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Wissensbereich {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "wissensgebiet_id")
    private Wissensgebiet wissensgebiet;

    @OneToMany(mappedBy = "wissensbereich")
    private List<Wissensbaustein> wissensbausteine;

    // Getter, Setter, Constructor
}
