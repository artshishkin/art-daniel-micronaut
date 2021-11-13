package net.shyshkin.study.micronaut.broker.persistence.jpa;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import net.shyshkin.study.micronaut.broker.persistence.model.QuoteEntity;
import net.shyshkin.study.micronaut.broker.persistence.model.SymbolEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuotesRepository extends CrudRepository<QuoteEntity, Long> {

    @Override
    List<QuoteEntity> findAll();

    List<QuoteEntity> findAllBySymbolValue(String symbol);

    Optional<QuoteEntity> findOneBySymbol(SymbolEntity symbol);

}
