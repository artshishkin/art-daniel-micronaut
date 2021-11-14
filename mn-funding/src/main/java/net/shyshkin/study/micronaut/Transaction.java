package net.shyshkin.study.micronaut;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@MappedEntity(value = "transactions")
//@MappedEntity(value = "transactions", namingStrategy = NamingStrategies.Raw.class)
public class Transaction {

    @Id
    @MappedProperty("transactionId")
    private long transactionId;

    private final UUID user;
    private final String symbol;
    private final BigDecimal modification;

}
