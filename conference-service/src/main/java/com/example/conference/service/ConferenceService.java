package com.example.conference.service;

import com.example.conference.domain.Conference;
import com.example.conference.domain.Review;
import com.example.conference.repository.ConferenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConferenceService {

    private final ConferenceRepository repository;

    public ConferenceService(ConferenceRepository repository) { this.repository = repository; }

    public Conference save(Conference c) { return repository.save(c); }
    public Optional<Conference> findById(Long id) { return repository.findById(id); }
    public List<Conference> findAll() { return repository.findAll(); }
    public Review addReview(Long conferenceId, Review review) {
        Conference c = repository.findById(conferenceId).orElseThrow();
        c.getReviews().add(review);
        repository.save(c);
        return review;
    }
}
