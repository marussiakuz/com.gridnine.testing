package com.gridnine.testing.filter;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DepartureAfterFilter implements Filter {

    private final LocalDateTime dateTime;

    public DepartureAfterFilter(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public List<Flight> apply(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> !hasDepartureDateBefore(flight.getSegments()))
                .collect(Collectors.toList());
    }

    private boolean hasDepartureDateBefore(List<Segment> segments) {
        return segments.stream()
                .anyMatch(segment -> segment.getDepartureDate().isBefore(dateTime));
    }
}
