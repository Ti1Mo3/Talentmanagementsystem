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

    @Column(nullable = false)
    private Boolean einarbeitung;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Reihenfolge reihenfolge;

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
        GRUNDWISSEN, BERATERWISSEN, EXPERTENWISSEN
    }

    public enum Reihenfolge {
        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10);
        private final int value;
        Reihenfolge(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    public Wissensbaustein() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Level getLevel() { return level; }
    public void setLevel(Level level) { this.level = level; }

    public Boolean getEinarbeitung() { return einarbeitung; }
    public void setEinarbeitung(Boolean einarbeitung) { this.einarbeitung = einarbeitung; }

    public Reihenfolge getReihenfolge() { return reihenfolge; }
    public void setReihenfolge(Reihenfolge reihenfolge) { this.reihenfolge = reihenfolge; }

    public Wissensbereich getWissensbereich() { return wissensbereich; }
    public void setWissensbereich(Wissensbereich wissensbereich) { this.wissensbereich = wissensbereich; }

    public List<Berater> getBerater() { return berater; }
    public void setBerater(List<Berater> berater) { this.berater = berater; }

    public List<Ausbildungspfad> getAusbildungspfade() { return ausbildungspfade; }
    public void setAusbildungspfade(List<Ausbildungspfad> ausbildungspfade) { this.ausbildungspfade = ausbildungspfade; }
}
