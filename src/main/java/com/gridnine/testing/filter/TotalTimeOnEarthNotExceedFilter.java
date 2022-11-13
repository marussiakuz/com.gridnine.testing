package com.gridnine.testing.filter;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TotalTimeOnEarthNotExceedFilter implements Filter {

    private final Duration maxDuration;

    public TotalTimeOnEarthNotExceedFilter(Duration maxDuration) {
        this.maxDuration = maxDuration;
    }

    @Override
    public List<Flight> apply(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> totalTimeOnEarthNotExceedingMax(flight.getSegments()))
                .collect(Collectors.toList());
    }

    private boolean totalTimeOnEarthNotExceedingMax(List<Segment> segments) {
        if (segments.size() == 1) return true;

        AtomicLong totalTimeOnEarth = new AtomicLong();

        IntStream.range(0, segments.size() - 1)
                .forEach(index -> totalTimeOnEarth.getAndAdd(Duration.between(segments.get(index).getArrivalDate(),
                        segments.get(index + 1).getDepartureDate()).toSeconds()));

        return totalTimeOnEarth.get() <= maxDuration.toSeconds();
    }
}
