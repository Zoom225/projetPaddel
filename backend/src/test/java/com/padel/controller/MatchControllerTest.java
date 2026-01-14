package com.padel.controller;

import com.padel.model.*;
import com.padel.service.MatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchControllerTest {

    @Mock
    private MatchService matchService;

    @InjectMocks
    private MatchController matchController;

    @Test
    void testGetAllMatches() {
        List<Match> matches = Arrays.asList(new Match(), new Match());
        when(matchService.getAllMatches()).thenReturn(matches);

        ResponseEntity<List<Match>> response = matchController.getAllMatches();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetMatchById() {
        Match match = new Match();
        match.setId(1L);
        when(matchService.getMatchById(1L)).thenReturn(Optional.of(match));

        ResponseEntity<Match> response = matchController.getMatchById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetMatchById_NotFound() {
        when(matchService.getMatchById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Match> response = matchController.getMatchById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateMatch() {
        Match match = new Match();
        match.setId(1L);
        when(matchService.createMatch(anyString(), anyLong(), any(LocalDate.class), 
                any(LocalTime.class), any(TypeMatch.class))).thenReturn(match);

        ResponseEntity<Match> response = matchController.createMatch(
                "G1234", 1L, LocalDate.now().plusDays(21), LocalTime.of(10, 0), TypeMatch.PRIVE);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}

