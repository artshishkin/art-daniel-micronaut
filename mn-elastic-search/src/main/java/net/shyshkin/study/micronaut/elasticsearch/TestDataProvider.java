package net.shyshkin.study.micronaut.elasticsearch;

import com.github.javafaker.Faker;
import io.micronaut.context.annotation.Property;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

@Singleton
public class TestDataProvider {

    public static final Logger log = LoggerFactory.getLogger(TestDataProvider.class);

    private final RestHighLevelClient client;

    @Property(name = "app.elasticsearch.index")
    private String index;

    public TestDataProvider(RestHighLevelClient client) {
        this.client = client;
    }

    @Scheduled(fixedDelay = "10s")
    void insertDocument() {

        var document = Map.of(
                "first_name", Faker.instance().name().firstName(),
                "last_name", Faker.instance().name().lastName()
        );

        log.debug("Trying to insert: {}", document);

        IndexRequest indexRequest = new IndexRequest()
                .index(this.index)
                .id(UUID.randomUUID().toString())
                .source(document, XContentType.JSON);

        client.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                log.debug("Added document {} with id {}", document, indexResponse.getId());
            }

            @Override
            public void onFailure(Exception e) {
                log.error("Failed to insert document: ", e);
            }
        });


    }

}
