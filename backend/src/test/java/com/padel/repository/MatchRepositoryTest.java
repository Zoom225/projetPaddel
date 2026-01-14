package com.padel.repository;

import com.padel.model.Match;
import com.padel.model.TypeMatch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class MatchRepositoryTest {

    @Autowired
    private MatchRepository matchRepository;

    @Test
    void testFindByTypeMatch() {
        // Ce test nécessiterait des données de test
        List<Match> matches = matchRepository.findByTypeMatch(TypeMatch.PUBLIC);
        assertNotNull(matches);
    }

    @Test
    void testFindByDate() {
        LocalDate date = LocalDate.now();
        List<Match> matches = matchRepository.findByDate(date);
        assertNotNull(matches);
    }
}

