package ru.practicum.shareit.strategy;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.State;

import java.util.HashMap;
import java.util.Map;

@Component
public class BookingStateStrategy {
    public final Map<State, BookingStateFetchStrategy> handlers;

    public BookingStateStrategy(BookingAllFetchStrategy allStrategy,
                                BookingCurrentFetchStrategy currentStrategy,
                                BookingFutureFetchStrategy futureStrategy,
                                BookingPastFetchStrategy pastStrategy,
                                BookingRejectedFetchStrategy rejectedStrategy,
                                BookingWaitingFetchStrategy waitingStrategy) {
        handlers = new HashMap<>();
        handlers.put(State.ALL, allStrategy);
        handlers.put(State.CURRENT, currentStrategy);
        handlers.put(State.FUTURE, futureStrategy);
        handlers.put(State.PAST, pastStrategy);
        handlers.put(State.REJECTED, rejectedStrategy);
        handlers.put(State.WAITING, waitingStrategy);
    }
}
