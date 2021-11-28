package net.shyshkin.study.micronaut.elasticsearch.es;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller("/data")
public class DataEndpoint {

    private static final Logger log = LoggerFactory.getLogger(DataEndpoint.class);

    private final RestHighLevelClient client;

    @Property(name = "app.elasticsearch.index")
    private String index;

    public DataEndpoint(RestHighLevelClient client) {
        this.client = client;
    }

    @ExecuteOn(TaskExecutors.IO)
    @Get("/document/{id}")
    public String byId(@PathVariable("id") UUID documentId) throws IOException {
        GetResponse response = client.get(new GetRequest(this.index, documentId.toString()), RequestOptions.DEFAULT);
        Map<String, Object> source = response.getSource();
        log.debug("Requested Document by id {}: {}", documentId, response.getSourceAsString());
        return response.getSourceAsString();
    }

    @Get("/document/async/{id}")
    public CompletableFuture<String> byIdAsync(@PathVariable("id") UUID documentId) {
        CompletableFuture<String> whenDone = new CompletableFuture<>();
        client.getAsync(new GetRequest(this.index, documentId.toString()), RequestOptions.DEFAULT, new ActionListener<GetResponse>() {
            @Override
            public void onResponse(GetResponse response) {
                var source = response.getSourceAsString();
                log.debug("Requested Document by id {}: {}", documentId, source);
                whenDone.complete(source);
            }

            @Override
            public void onFailure(Exception e) {
                log.error("Error while requesting document by id {}: {}", documentId, e.getMessage());
                whenDone.completeExceptionally(e);
            }
        });
        return whenDone;
    }

    @Get("/document/async/search/{firstname}")
    public CompletableFuture<String> searchByFirstNameAsync(@PathVariable("firstname") String search) {
        CompletableFuture<String> whenDone = new CompletableFuture<>();

        String[] indices = {this.index};
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder
                .searchSource()
                .query(QueryBuilders.matchQuery("first_name", search));
        SearchRequest searchRequest = new SearchRequest(indices, searchSourceBuilder);
        client.searchAsync(searchRequest, RequestOptions.DEFAULT, new ActionListener<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse searchResponse) {
                var hits = searchResponse.getHits().getHits();
                String source = Stream.of(hits)
                        .map(SearchHit::getSourceAsString)
                        .collect(Collectors.joining(","));
                source = "[" + source + "]";
                log.debug("Searching Document by firstname {}: {}", search, source);
                whenDone.complete(source);
            }

            @Override
            public void onFailure(Exception e) {
                log.error("Error while requesting document by firstname {}: {}", search, e.getMessage());
                whenDone.completeExceptionally(e);
            }
        });

        return whenDone;
    }


}
