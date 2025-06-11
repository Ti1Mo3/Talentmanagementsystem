package com.example.talentmanagement.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Wissensbaustein {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Level level;

    @ManyToOne
    @JoinColumn(name = "wissensbereich_id")
    private Wissensbereich wissensbereich;

    @ManyToMany(mappedBy = "wissensbausteine")
    private List<Berater> berater;

    @ManyToMany
    @JoinTable(
        name = "wissensbaustein_ausbildungspfad",
        joinColumns = @JoinColumn(name = "wissensbaustein_id"),
        inverseJoinColumns = @JoinColumn(name = "ausbildungspfad_id")
    )
    private List<Ausbildungspfad> ausbildungspfade;

    public enum Level {
        Grundwissen, Beraterwissen, Expertenwissen
    }

    // Getter, Setter, Constructor
}
