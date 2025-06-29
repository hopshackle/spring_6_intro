package guru.springframework.spring6restmvc.bootstrap;


import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {

        // We now set up some initial data for the Beer and Customer repositories
        if (beerRepository.count() == 0) {
            // Add some beers
            beerRepository.save(Beer.builder()
                    .beerName("Beer 1")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("123456789012")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(100)
                    .build());

            beerRepository.save(Beer.builder()
                    .beerName("Beer 2")
                    .beerStyle(BeerStyle.IPA)
                    .upc("123456789013")
                    .price(new BigDecimal("14.99"))
                    .quantityOnHand(150)
                    .build());

            beerRepository.save(Beer.builder()
                    .beerName("Beer 3")
                    .beerStyle(BeerStyle.STOUT)
                    .upc("123456789014")
                    .price(new BigDecimal("15.99"))
                    .quantityOnHand(200)
                    .build());

            // and then three sample customers, Minnie Mouse, Daffy Duck and Pluto
            customerRepository.save(Customer.builder()
                    .name("Minnie Mouse")
                    .creditLimit(450.00)
                    .build());
            customerRepository.save(Customer.builder()
                    .name("Daffy Duck")
                    .creditLimit(550.00)
                    .build());
            customerRepository.save(Customer.builder()
                    .name("Pluto")
                    .creditLimit(650.00)
                    .build());
        }
    }
}
