package com.gridnine.testing.filter;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

import java.util.List;
import java.util.stream.Collectors;

public class DepartureBeforeArrivalFilter implements Filter {

    @Override
    public List<Flight> apply(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> allArrivalsAfterDepartures(flight.getSegments()))
                .collect(Collectors.toList());
    }

    private boolean allArrivalsAfterDepartures(List<Segment> segments) {
        return segments.stream()
                .allMatch(segment -> segment.getArrivalDate().isAfter(segment.getDepartureDate()));
    }
}
