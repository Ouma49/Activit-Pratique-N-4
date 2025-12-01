package com.example.conference.dto;

import java.time.LocalDateTime;

public class ReviewDTO {
    private Long id;
    private LocalDateTime date;
    private String texte;
    private Integer note;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }
    public Integer getNote() { return note; }
    public void setNote(Integer note) { this.note = note; }
}
