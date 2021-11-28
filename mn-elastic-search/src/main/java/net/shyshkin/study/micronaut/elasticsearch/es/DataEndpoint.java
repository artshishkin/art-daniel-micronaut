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
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

}
