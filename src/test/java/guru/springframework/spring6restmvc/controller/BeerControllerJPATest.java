package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.bootstrap.BootstrapData;
import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class BeerControllerJPATest {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    private BootstrapData bootstrapData;

    @Autowired
    BeerController beerController;

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeEach
    public void setup() throws Exception {
        // Ensure the repository is empty before each test
        beerRepository.deleteAll();
        bootstrapData.run(); // Load initial data
        // Not clear why we cannot make this @Transactional, but if we try that then RestTemplate picks up stale data
    }

    @Test
    public void testEmptyList() throws Exception {
        beerRepository.deleteAll();
        ResponseEntity<List> response = restTemplate.getForEntity("/api/v1/beer", List.class);
        assertNotNull(response.getBody());
        assertTrue(((List<?>) response.getBody()).isEmpty(), "Beer list should be empty");
    }

    @Test
    public void testNonEmptyList() {
        ResponseEntity<List> response = restTemplate.getForEntity("/api/v1/beer", List.class);
        assertNotNull(response.getBody());
        assertEquals(3, ((List<?>) response.getBody()).size(), "Beer list should be empty");
    }

    @Test
    public void testGetBeerById() {
        Beer knownBeer = beerController.listBeers().get(1);
        UUID beerId = knownBeer.getId();

        // Perform a mock web request
        ResponseEntity<Beer> response = restTemplate.getForEntity("/api/v1/beer/" + beerId, Beer.class);

        // Validate the response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(knownBeer.getId(), response.getBody().getId(), "Beer ID should match");
        assertEquals(knownBeer.getBeerName(), response.getBody().getBeerName(), "Beer name should match");
    }

    @Test
    public void testGetBeerByUnknownId() {
        ResponseEntity<Beer> response = restTemplate.getForEntity("/api/v1/beer/" + UUID.randomUUID(), Beer.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Response status should be NOT_FOUND");
    }

    @Test
    public void testSaveBeer() {
        Beer newBeer = Beer.builder()
                .beerName("Imperial Stout")
                .beerStyle(BeerStyle.STOUT)
                .upc("389302-3793-2938")
                .price(BigDecimal.valueOf(12.99))
                .build();

        assertNull(newBeer.getId(), "New beer should not have an ID before saving");

        ResponseEntity<Beer> responseEntity = restTemplate.postForEntity("/api/v1/beer", newBeer, Beer.class);
        assertEquals(CREATED, responseEntity.getStatusCode());
        String location = Objects.requireNonNull(responseEntity.getHeaders().getLocation()).toString();
        assertTrue(location.contains("/api/v1/beer/"), "Location header should contain the beer ID");
        UUID beerId = UUID.fromString(location.substring(location.lastIndexOf("/") + 1));
        assertNotNull(beerId, "Beer ID should not be null after saving");

        responseEntity = restTemplate.getForEntity("/api/v1/beer/" + beerId, Beer.class);
        Beer beerFromRepository = responseEntity.getBody();
        assertEquals("Imperial Stout", beerFromRepository.getBeerName(), "Beer name should match");
    }

    @Test
    public void deleteBeerById() throws Exception {
        Beer beerToDelete = beerController.listBeers().get(0);
        UUID beerId = beerToDelete.getId();

        ResponseEntity response = restTemplate.exchange("/api/v1/beer/" + beerId, HttpMethod.DELETE, null, Void.class);
        assertEquals(NO_CONTENT, response.getStatusCode(), "Response status should be NO_CONTENT");
        // Verify the beer is deleted
        assertEquals(Optional.empty(), beerRepository.findById(beerId), "Beer should be deleted from repository");
    }

    @Test
    public void testUpdateBeer() {
        Beer existingBeer = beerController.listBeers().get(2);
        UUID beerId = existingBeer.getId();

        Beer updatedBeer = Beer.builder()
                .beerName("Updated Beer Name")
                .beerStyle(BeerStyle.IPA)
                .upc("123456789999")
                .price(BigDecimal.valueOf(15.99))
                .build();

        ResponseEntity responseEntity = restTemplate.exchange("/api/v1/beer/" + beerId, HttpMethod.PUT,
                new HttpEntity<>(updatedBeer), Void.class);
        assertEquals(NO_CONTENT, responseEntity.getStatusCode());

        Beer beerFromRepository = beerRepository.findById(beerId).get();
        assertEquals("Updated Beer Name", beerFromRepository.getBeerName(), "Updated beer name should match");
        assertEquals(BeerStyle.IPA, beerFromRepository.getBeerStyle(), "Beer style should be updated to IPA");
        assertEquals("123456789999", beerFromRepository.getUpc(), "UPC should be updated");
        assertEquals(BigDecimal.valueOf(15.99), beerFromRepository.getPrice(), "Price should be updated");
    }

    @Test
    public void testUpdateBeerWithUnknownId() {
        Beer updatedBeer = Beer.builder()
                .beerName("Updated Beer Name")
                .beerStyle(BeerStyle.IPA)
                .upc("123456789999")
                .price(BigDecimal.valueOf(15.99))
                .build();
        ResponseEntity response = restTemplate.exchange("/api/v1/beer/" + UUID.randomUUID(), HttpMethod.PUT,
                new HttpEntity<>(updatedBeer), Void.class);
        assertEquals(NOT_FOUND, response.getStatusCode(), "Response status should be NOT_FOUND");
    }
}
