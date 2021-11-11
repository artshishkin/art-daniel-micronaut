package net.shyshkin.study.micronaut.hello;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class Greeting {

    final String myText = "Hello World";
    final BigDecimal id = BigDecimal.ONE;
    final BigDecimal money =new BigDecimal("123.45");
    final Instant timeUTC = Instant.now();

}
