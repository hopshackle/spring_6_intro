package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// without specifying the specific class, the framework tries to load a BeerService too (and no Mock available?)
@WebMvcTest()
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;
    // This provides the spring framework functionality needed to test the controller

    @MockitoBean
    BeerService beerService;  // not used, but

    @MockitoBean
    CustomerService customerService;
    // this will be a mock object

    @Test
    public void testGetCustomerById() throws Exception {
        Customer customer = Customer.builder()
                .name("John Doe")
                .creditLimit(1000.0)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        // set the Mocked service to return John Doe (regardless of the id passed in)
        // i.e. for a Mocked object, we define outside the object what it should return
        when(customerService.getCustomerById(anyInt())).thenReturn(customer);

        mockMvc.perform(get("/api/v1/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.creditLimit").value(1000.0))
                .andExpect(jsonPath("$.createdDate").exists())
                .andExpect(jsonPath("$.modifiedDate").exists());

    }
}
