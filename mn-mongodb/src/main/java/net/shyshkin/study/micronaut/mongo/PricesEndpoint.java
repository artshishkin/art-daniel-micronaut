package net.shyshkin.study.micronaut.mongo;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller("/api/prices")
public class PricesEndpoint {

    public static final Logger log = LoggerFactory.getLogger(PricesEndpoint.class);

    private final MongoClient client;

    public PricesEndpoint(MongoClient client) {
        this.client = client;
    }

    @Get
    public Flux<Document> getAllPrices() {
        MongoCollection<Document> collection = client.getDatabase("prices").getCollection("example");
        return Flux.from(collection.find());
    }

    @Post
    public Mono<InsertOneResult> insert(@Body ObjectNode json) {

        String jsonText = json.toString();
        log.info("JSON {}", jsonText);
        Document doc = Document.parse(jsonText);
        log.info("Document {}", doc);
        Publisher<InsertOneResult> insertOne = client
                .getDatabase("prices")
                .getCollection("example")
                .insertOne(doc);
        return Mono.from(insertOne);
    }

}
