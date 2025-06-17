package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.Customer;

import java.util.List;

public interface CustomerService {

    Customer getCustomerById(int id);

    List<Customer> listCustomers();

    Customer saveCustomer(Customer customer);
}
