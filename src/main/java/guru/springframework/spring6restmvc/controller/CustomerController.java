package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;


@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private CustomerService customerService;

    @RequestMapping(method= RequestMethod.GET)
    public List<Customer> customers() {
        return customerService.listCustomers();
    }

    @RequestMapping(value="{id}", method= RequestMethod.GET)
    public Customer findCustomerById(@PathVariable("id") int id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public ResponseEntity handlePost(@RequestBody Customer customer) {
        Customer savedCustomer =  customerService.saveCustomer(customer);
        ResponseEntity response = ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/customer/" + savedCustomer.getId())
                .build();
        return response;
    }

    @PutMapping("{id}")
    public ResponseEntity handleUpdate(@PathVariable("id") int id, @RequestBody Customer customer) {

        Customer existingCustomer = customerService.getCustomerById(id);
        if (customer.getName() != null) {
            existingCustomer.setName(customer.getName());
        }
        if (customer.getCreditLimit() != null) {
            existingCustomer.setCreditLimit(customer.getCreditLimit());
        }
        existingCustomer.setModifiedDate(LocalDateTime.now());
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
