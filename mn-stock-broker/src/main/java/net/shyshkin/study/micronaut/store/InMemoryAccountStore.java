package net.shyshkin.study.micronaut.store;

import net.shyshkin.study.micronaut.broker.model.WatchList;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class InMemoryAccountStore implements AccountStore {

    private final Map<UUID, WatchList> watchListMap = new HashMap<>();

    @Override
    public WatchList getWatchList(final UUID accountId) {
        return watchListMap.getOrDefault(accountId, new WatchList());
    }

    @Override
    public WatchList updateWatchList(final UUID accountId, final WatchList watchList) {
        watchListMap.put(accountId, watchList);
        return getWatchList(accountId);
    }

    @Override
    public void deleteWatchList(final UUID accountId) {
        watchListMap.remove(accountId);
    }

}
