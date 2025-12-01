package com.example.conference.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConferenceDTO {
    private Long id;
    private String titre;
    private String type;
    private LocalDateTime date;
    private Integer dureeMinutes;
    private Integer nbInscrits;
    private Double score;
    private Long keynoteId;
    private List<ReviewDTO> reviews = new ArrayList<>();

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
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
    public List<ReviewDTO> getReviews() { return reviews; }
    public void setReviews(List<ReviewDTO> reviews) { this.reviews = reviews; }
}
