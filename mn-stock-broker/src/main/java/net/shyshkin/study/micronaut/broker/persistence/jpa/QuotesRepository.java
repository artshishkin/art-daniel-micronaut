package net.shyshkin.study.micronaut.broker.persistence.jpa;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import net.shyshkin.study.micronaut.broker.persistence.model.QuoteEntity;

import java.util.List;

@Repository
public interface QuotesRepository extends CrudRepository<QuoteEntity, Long> {

    @Override
    List<QuoteEntity> findAll();

}
