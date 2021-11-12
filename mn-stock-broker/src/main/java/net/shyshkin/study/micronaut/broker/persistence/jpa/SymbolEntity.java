package net.shyshkin.study.micronaut.broker.persistence.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "symbol")
@Table(name = "symbols", schema = "mn")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SymbolEntity {

    @Id
    private String value;

}
