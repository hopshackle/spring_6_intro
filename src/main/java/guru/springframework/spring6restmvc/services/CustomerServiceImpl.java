package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Customer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {

    Map<Integer, Customer> customers = new HashMap<>();

    public CustomerServiceImpl() {
        customers.put(1, Customer.builder().name("John Doe")
                .creditLimit(1000.00)
                .createdDate(LocalDateTime.parse("2023-07-12T00:00:00"))
                .build());
        customers.put(2, Customer.builder().name("Jane Smith").creditLimit(1500.00).build());
        customers.put(3, Customer.builder().name("Alice Johnson").creditLimit(2000.00).build());
    }

    @Override
    public Customer getCustomerById(int id) {
        return customers.get(id);
    }

    @Override
    public List<Customer> listCustomers() {
        return customers.values().stream().toList();
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        Customer savedCustomer = Customer.builder()
                .name(customer.getName())
                .creditLimit(customer.getCreditLimit())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
        customers.put(customer.getId(), savedCustomer);
        return customers.get(customer.getId());
    }

    @Override
    public Customer patchCustomer(int id, Customer customer) {
        Customer existingCustomer = customers.get(id);
        if (customer.getName() != null) {
            existingCustomer.setName(customer.getName());
        }
        if (customer.getCreditLimit() != null) {
            existingCustomer.setCreditLimit(customer.getCreditLimit());
        }
        existingCustomer.setModifiedDate(LocalDateTime.now());
        return existingCustomer;
    }
}
