package guru.springframework.spring6restmvc.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
public class Customer {

    private static AtomicInteger idGenerator = new AtomicInteger(1);
    private final int id = idGenerator.getAndIncrement();
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Double creditLimit;

}
