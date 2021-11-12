package net.shyshkin.study.micronaut.broker.persistence.jpa;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface SymbolsRepository extends CrudRepository<SymbolEntity, String> {

    @Override
    List<SymbolEntity> findAll();
}
