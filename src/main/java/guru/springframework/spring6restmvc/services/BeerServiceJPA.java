package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.context.annotation.Primary;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository repository;

    @Override
    public List<Beer> listBeers() {
        return repository.findAll();
    }

    @Override
    public Beer getBeerById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Beer not found"));
    }

    @Override
    public Beer saveNewBeer(Beer beer) {
        return repository.save(beer);
    }

    @Override
    public Beer updateBeer(UUID id, Beer beer) {
        return repository.findById(id)
                .map(existingBeer -> {
                    existingBeer.setBeerName(beer.getBeerName());
                    existingBeer.setBeerStyle(beer.getBeerStyle());
                    existingBeer.setUpc(beer.getUpc());
                    existingBeer.setPrice(beer.getPrice());
                    existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
                    return repository.save(existingBeer);
                })
                .orElseThrow(() -> new NoSuchElementException("Beer not found"));
    }

    @Override
    public void deleteBeer(UUID id) {
        repository.delete(repository.findById(id).orElseThrow(() -> new NoSuchElementException("Beer not found")));
    }
}
