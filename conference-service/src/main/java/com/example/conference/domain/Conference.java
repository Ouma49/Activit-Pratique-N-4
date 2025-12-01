package com.example.conference.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Enumerated(EnumType.STRING)
    private ConferenceType type;

    private LocalDateTime date;

    private Integer dureeMinutes;
    private Integer nbInscrits;
    private Double score;

    private Long keynoteId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public ConferenceType getType() { return type; }
    public void setType(ConferenceType type) { this.type = type; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public Integer getDureeMinutes() { return dureeMinutes; }
    public void setDureeMinutes(Integer dureeMinutes) { this.dureeMinutes = dureeMinutes; }
    public Integer getNbInscrits() { return nbInscrits; }
    public void setNbInscrits(Integer nbInscrits) { this.nbInscrits = nbInscrits; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public Long getKeynoteId() { return keynoteId; }
    public void setKeynoteId(Long keynoteId) { this.keynoteId = keynoteId; }
    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }
}
