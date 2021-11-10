package net.shyshkin.study.micronaut.store;

import net.shyshkin.study.micronaut.broker.model.WatchList;

import java.util.UUID;

public interface AccountStore {

    WatchList getWatchList(final UUID accountId);

    WatchList updateWatchList(final UUID accountId, final WatchList watchList);

    void deleteWatchList(final UUID accountId);

}
