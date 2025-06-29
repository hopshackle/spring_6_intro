package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public List<Beer> listBeers(){
        return beerService.listBeers();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable("id") UUID beerId){
        return beerService.getBeerById(beerId);
    }

    @PostMapping
    public ResponseEntity<Beer> handlePost(@RequestBody Beer beer) {
        // Implementation for handling POST request
        Beer savedBeer = beerService.saveNewBeer(beer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBeer);
    }

}
