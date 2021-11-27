package net.shyshkin.study.micronaut.redis;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;

@Controller("/api/time")
public class RateLimitedTimeEndpoint {

    private static final Logger log = LoggerFactory.getLogger(RateLimitedTimeEndpoint.class);

    private static final int QUOTA_PER_MINUTE = 10;

    private final StatefulRedisConnection<String, String> redis;

    public RateLimitedTimeEndpoint(StatefulRedisConnection<String, String> redis) {
        this.redis = redis;
    }

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    public String getTime() {

        String key = "EXAMPLE::TIME";
        String value = redis.sync().get(key);

        int currentQuota = value == null ? 0 : Integer.parseInt(value);

        if (currentQuota >= QUOTA_PER_MINUTE) {
            String errMessage = String.format("Rate limit reached %s %d/%d", key, currentQuota, QUOTA_PER_MINUTE);
            log.warn("{}", errMessage);
            return errMessage;
        }
        log.info("Current quota {} {}/{}", key, currentQuota, QUOTA_PER_MINUTE);
        increaseCurrentQuota(key);
        return LocalTime.now().toString();
    }

    private void increaseCurrentQuota(String key) {

        RedisCommands<String, String> commands = redis.sync();
        commands.multi();
        commands.incr(key);
        var remainingSeconds = 60 - LocalTime.now().getSecond();
        commands.expire(key, remainingSeconds);
        commands.exec();
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
