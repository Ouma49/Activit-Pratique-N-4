package com.example.conference;

import com.example.conference.dto.ConferenceDTO;
import com.example.conference.dto.ReviewDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConferenceIntegrationTest {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void createConferenceAndAddReview() {
        ConferenceDTO c = new ConferenceDTO();
        c.setTitre("Test Conf");
        ResponseEntity<ConferenceDTO> post = rest.postForEntity("/api/conferences", c, ConferenceDTO.class);
        assertThat(post.getStatusCode().is2xxSuccessful()).isTrue();
        ConferenceDTO created = post.getBody();
        assertThat(created).isNotNull();

        ReviewDTO r = new ReviewDTO();
        r.setTexte("Great");
        r.setNote(5);
        ResponseEntity<ReviewDTO> add = rest.postForEntity("/api/conferences/" + created.getId() + "/reviews", r, ReviewDTO.class);
        assertThat(add.getStatusCode().is2xxSuccessful()).isTrue();
    }
}
