package com.example.conference.mapper;

import com.example.conference.domain.Conference;
import com.example.conference.domain.Review;
import com.example.conference.dto.ConferenceDTO;
import com.example.conference.dto.ReviewDTO;

import java.util.stream.Collectors;

public class ConferenceMapper {
    public static ConferenceDTO toDto(Conference c) {
        if (c == null) return null;
        ConferenceDTO dto = new ConferenceDTO();
        dto.setId(c.getId());
        dto.setTitre(c.getTitre());
        dto.setType(c.getType() == null ? null : c.getType().name());
        dto.setDate(c.getDate());
        dto.setDureeMinutes(c.getDureeMinutes());
        dto.setNbInscrits(c.getNbInscrits());
        dto.setScore(c.getScore());
        dto.setKeynoteId(c.getKeynoteId());
        dto.setReviews(c.getReviews().stream().map(ConferenceMapper::reviewToDto).collect(Collectors.toList()));
        return dto;
    }

    public static ReviewDTO reviewToDto(Review r) {
        if (r == null) return null;
        ReviewDTO dto = new ReviewDTO();
        dto.setId(r.getId());
        dto.setDate(r.getDate());
        dto.setTexte(r.getTexte());
        dto.setNote(r.getNote());
        return dto;
    }

    public static Review dtoToReview(ReviewDTO dto) {
        if (dto == null) return null;
        Review r = new Review();
        r.setId(dto.getId());
        r.setDate(dto.getDate());
        r.setTexte(dto.getTexte());
        r.setNote(dto.getNote());
        return r;
    }
}
