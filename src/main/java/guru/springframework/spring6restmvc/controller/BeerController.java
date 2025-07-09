package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Created by jt, Spring Framework Guru.
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final BeerService beerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping("{id}")
    public ResponseEntity<Beer> getBeerById(@PathVariable("id") UUID beerId) {
        try {
            Beer beer = beerService.getBeerById(beerId);
            return ResponseEntity.ok(beer);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping()
    public ResponseEntity handlePost(@RequestBody Beer beer) {
        // Implementation for handling POST request
        Beer savedBeer = beerService.saveNewBeer(beer);
        if (savedBeer == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/beer/" + savedBeer.getId().toString())
                .build();
    }

    @PutMapping("{id}")
    public ResponseEntity handlePut(@PathVariable("id") UUID beerId, @RequestBody Beer beer) {
        try {
            Beer updated = beerService.updateBeer(beerId, beer);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .header("Location", "/api/v1/beer/" + updated.getId().toString())
                    .build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity handleDelete(@PathVariable("id") UUID beerId) {
        if (beerService.getBeerById(beerId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        beerService.deleteBeer(beerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
