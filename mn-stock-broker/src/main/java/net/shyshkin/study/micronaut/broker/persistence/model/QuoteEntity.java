package net.shyshkin.study.micronaut.broker.persistence.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "quote")
@Table(name = "quotes", schema = "mn")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class QuoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private BigDecimal bid;
    private BigDecimal ask;
    @Column(name = "last_price")
    private BigDecimal lastPrice;
    private BigDecimal volume;

    @ManyToOne(targetEntity = SymbolEntity.class)
    @JoinColumn(name = "symbol", referencedColumnName = "value")
    private SymbolEntity symbol;
}
