package com.gridnine.testing.filter;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class DepartureBeforeArrivalFilterTest {

    @Test
    public void ifFlightHasDepartureLaterThanArrivalThenWillNotBeReturned() {
        Segment departureBeforeArrival = new Segment(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        Segment departureAfterArrival = new Segment(LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(2));

        Flight flight = new Flight(List.of(departureBeforeArrival, departureAfterArrival));

        List<Flight> returned = new DepartureBeforeArrivalFilter().apply(List.of(flight));

        assertTrue("flight with departure after arrival wasn't filtered out", returned.isEmpty());
    }

    @Test
    public void ifFlightHasDepartureEqualsArrivalThenWillNotBeReturned() {
        LocalDateTime dateTime = LocalDateTime.now();

        Segment departureEqualsArrival = new Segment(dateTime, dateTime);

        Flight flight = new Flight(List.of(departureEqualsArrival));

        List<Flight> returned = new DepartureBeforeArrivalFilter().apply(List.of(flight));

        assertTrue("flight with departure equals arrival wasn't filtered out", returned.isEmpty());
    }

    @Test
    public void ifFlightHasAllDeparturesBeforeThanArrivalsThenWillBeReturned() {
        Segment first = new Segment(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        Segment second = new Segment(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3));

        Flight flight = new Flight(List.of(first, second));

        List<Flight> returned = new DepartureBeforeArrivalFilter().apply(List.of(flight));

        assertEquals("flight with departures before arrivals wasn't returned", 1, returned.size());

        assertSame(flight, returned.get(0));
    }
}