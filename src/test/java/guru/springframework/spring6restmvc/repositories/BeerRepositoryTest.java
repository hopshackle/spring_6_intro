package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    public void testSaveBeer() {
        Beer beer = Beer.builder()
                .beerName("My Beer")
                .beerStyle(BeerStyle.IPA).build();
        assertNull(beer.getId());

        Beer savedBeer = beerRepository.save(beer);

        assertNotNull(savedBeer);
        assertEquals("My Beer", savedBeer.getBeerName());
        assertNotNull(savedBeer.getId());
    }
}
