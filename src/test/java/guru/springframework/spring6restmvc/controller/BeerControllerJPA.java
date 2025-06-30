package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootTest
public class BeerControllerJPA {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerController beerController;

    @Transactional
    @Test
    public void testEmptyList() {
        beerRepository.deleteAll();
        assertEquals(0, beerController.listBeers().size());
    }
    @Test
    public void testNonEmptyList() {
        assertEquals(3, beerController.listBeers().size());
    }

    @Test
    public void testGetBeerById() {
        Beer knownBeer = beerController.listBeers().get(1);
        assertEquals(knownBeer, beerController.getBeerById(knownBeer.getId()));
    }
    @Test
    public void testGetBeerByUnknownId() {
        assertThrows(NoSuchElementException.class, () ->
            beerController.getBeerById(UUID.randomUUID())
        );
    }

    @Transactional
    @Test
    public void testSaveBeer() {
        Beer newBeer = Beer.builder()
                .beerName("Imperial Stout")
                .beerStyle(BeerStyle.STOUT)
                .upc("389302-3793-2938")
                .price(BigDecimal.valueOf(12.99))
                .build();

        assertNull(newBeer.getId(), "New beer should not have an ID before saving");

        ResponseEntity<Beer> responseEntity = beerController.handlePost(newBeer);
        assertEquals(CREATED, responseEntity.getStatusCode());
        String location = Objects.requireNonNull(responseEntity.getHeaders().getLocation()).toString();
        assertTrue(location.contains("/api/v1/beer/"), "Location header should contain the beer ID");
        UUID beerId = UUID.fromString(location.substring(location.lastIndexOf("/") + 1));
        assertNotNull(beerId, "Beer ID should not be null after saving");

        Beer beerFromRepository = beerController.getBeerById(beerId);
        assertEquals("Imperial Stout", beerFromRepository.getBeerName(), "Beer name should match");
    }
}
