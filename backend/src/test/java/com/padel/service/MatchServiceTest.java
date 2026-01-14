package com.padel.service;

import com.padel.model.*;
import com.padel.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MembreRepository membreRepository;

    @Mock
    private TerrainRepository terrainRepository;

    @Mock
    private PaiementRepository paiementRepository;

    @Mock
    private JourFermetureRepository jourFermetureRepository;

    @Mock
    private MembreService membreService;

    @InjectMocks
    private MatchService matchService;

    private Site site;
    private Terrain terrain;
    private Membre membreGlobal;

    @BeforeEach
    void setUp() {
        site = new Site();
        site.setId(1L);
        site.setNom("Site Test");
        site.setHeureDebut(LocalTime.of(8, 0));
        site.setHeureFin(LocalTime.of(22, 0));

        terrain = new Terrain();
        terrain.setId(1L);
        terrain.setNom("Terrain 1");
        terrain.setSite(site);

        membreGlobal = new Membre();
        membreGlobal.setId(1L);
        membreGlobal.setMatricule("G1234");
        membreGlobal.setNom("Dupont");
        membreGlobal.setPrenom("Jean");
        membreGlobal.setTypeMembre(TypeMembre.GLOBAL);
        membreGlobal.setSoldeDu(0.0);
    }

    @Test
    void testCreateMatch_Success() {
        LocalDate dateMatch = LocalDate.now().plusDays(21); // 3 semaines pour membre global
        LocalTime heureDebut = LocalTime.of(10, 0);

        when(membreRepository.findByMatricule("G1234")).thenReturn(Optional.of(membreGlobal));
        when(terrainRepository.findById(1L)).thenReturn(Optional.of(terrain));
        when(matchRepository.findByDate(dateMatch)).thenReturn(java.util.Collections.emptyList());
        when(jourFermetureRepository.findBySiteIsNull()).thenReturn(java.util.Collections.emptyList());
        when(jourFermetureRepository.findBySiteId(1L)).thenReturn(java.util.Collections.emptyList());
        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Match match = matchService.createMatch("G1234", 1L, dateMatch, heureDebut, TypeMatch.PRIVE);

        assertNotNull(match);
        assertEquals(TypeMatch.PRIVE, match.getTypeMatch());
        assertEquals(membreGlobal, match.getOrganisateur());
        verify(matchRepository, times(1)).save(any(Match.class));
    }

    @Test
    void testCreateMatch_SoldeDu_ThrowsException() {
        membreGlobal.setSoldeDu(15.0);
        LocalDate dateMatch = LocalDate.now().plusDays(21);

        when(membreRepository.findByMatricule("G1234")).thenReturn(Optional.of(membreGlobal));

        assertThrows(RuntimeException.class, () -> {
            matchService.createMatch("G1234", 1L, dateMatch, LocalTime.of(10, 0), TypeMatch.PRIVE);
        });
    }

    @Test
    void testCreateMatch_DelaiInsuffisant_ThrowsException() {
        LocalDate dateMatch = LocalDate.now().plusDays(10); // Moins de 3 semaines

        when(membreRepository.findByMatricule("G1234")).thenReturn(Optional.of(membreGlobal));
        when(terrainRepository.findById(1L)).thenReturn(Optional.of(terrain));

        assertThrows(RuntimeException.class, () -> {
            matchService.createMatch("G1234", 1L, dateMatch, LocalTime.of(10, 0), TypeMatch.PRIVE);
        });
    }
}

