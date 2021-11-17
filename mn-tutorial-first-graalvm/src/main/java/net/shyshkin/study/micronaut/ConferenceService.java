package net.shyshkin.study.micronaut;

import jakarta.inject.Singleton;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class ConferenceService {

    private static final List<Conference> CONFERENCES = Stream
            .of(
                    "Greach",
                    "GR8Conf EU",
                    "Micronaut Summit",
                    "Devoxx Belgium",
                    "Oracle Code One",
                    "CommitConf",
                    "Codemotion Madrid"
            )
            .map(Conference::new)
            .collect(Collectors.toList());

    public Conference randomConf() {
        return CONFERENCES.get(ThreadLocalRandom.current().nextInt(CONFERENCES.size()));
    }

}
