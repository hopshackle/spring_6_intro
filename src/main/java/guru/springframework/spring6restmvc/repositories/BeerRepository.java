package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.model.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<Beer, Integer> {
}
