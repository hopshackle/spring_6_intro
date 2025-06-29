package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.services.BeerService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BeerControllerJPA {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerController beerController;

    @Transactional
    @Test
    public void testBeerData1() {
        beerRepository.deleteAll();
        assertEquals(0, beerController.listBeers().size());
    }
    @Test
    public void testBeerData2() {
        assertEquals(3, beerController.listBeers().size());
    }

    @Test
    public void testBeerData3() {
        Beer knownBeer = beerController.listBeers().get(1);
        assertEquals(knownBeer, beerController.getBeerById(knownBeer.getId()));
    }
    @Test
    public void testBeerData4() {
        assertThrows(NoSuchElementException.class, () ->
            beerController.getBeerById(UUID.randomUUID())
        );
    }
}
