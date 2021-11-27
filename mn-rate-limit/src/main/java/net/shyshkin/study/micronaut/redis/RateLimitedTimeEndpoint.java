package net.shyshkin.study.micronaut.redis;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import java.time.LocalTime;

@Controller("/api/time")
public class RateLimitedTimeEndpoint {

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    public String getTime() {
        return LocalTime.now().toString();
    }

    @Get("/json")
    @Produces(MediaType.APPLICATION_JSON)
    public TimeJson getTimeAsJson() {
        return new TimeJson();
    }

    static class TimeJson {

        @JsonFormat(pattern = "HH-mm-ss.SSS")
        private LocalTime time = LocalTime.now();

        public LocalTime getTime() {
            return time;
        }
    }

}
