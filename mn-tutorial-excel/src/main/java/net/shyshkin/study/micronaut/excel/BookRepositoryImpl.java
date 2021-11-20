package net.shyshkin.study.micronaut.excel;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class BookRepositoryImpl implements BookRepository {

    @NonNull
    @Override
    public List<Book> findAll() {
        Book buildingMicroservices = new Book("1491950358", "Building Microservices");
        Book releaseIt = new Book("1680502395", "Release It!");
        Book ciDelivery = new Book("0321601912", "Continuous Delivery:");
        return List.of(buildingMicroservices, releaseIt, ciDelivery);
    }
}
