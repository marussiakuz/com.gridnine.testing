package com.gridnine.testing.filter;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.*;

public class DepartureAfterFilterTest {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Test
    public void ifFlightHasDepartureEarlierThanTheSpecifiedDateThenWillNotBeReturned() {
        LocalDateTime dateTime = LocalDateTime.now();

        Segment departureAfter = new Segment(dateTime.plusHours(1), dateTime.plusHours(2));
        Segment departureBefore = new Segment(dateTime.minusHours(1), dateTime.plusHours(2));

        Flight flight = new Flight(List.of(departureAfter, departureBefore));

        List<Flight> returned = new DepartureAfterFilter(dateTime).apply(List.of(flight));

        assertTrue(String.format("flight with departure date earlier than %s wasn't filtered out", dateTime.format(FMT)),
                returned.isEmpty());
    }

    @Test
    public void ifFlightHasDepartureEqualsTheSpecifiedDateThenWillBeReturned() {
        LocalDateTime dateTime = LocalDateTime.now();

        Segment segment = new Segment(dateTime, dateTime.plusHours(5));

        Flight flight = new Flight(List.of(segment));

        List<Flight> returned = new DepartureAfterFilter(dateTime).apply(List.of(flight));

        assertEquals(String.format("flight with departure date equals %s wasn't returned", dateTime.format(FMT)),
                1, returned.size());

        assertSame(flight, returned.get(0));
    }

    @Test
    public void ifFlightHasAllDeparturesLaterThanTheSpecifiedDateThenWillBeReturned() {
        LocalDateTime dateTime = LocalDateTime.now();

        Segment departureAfter = new Segment(dateTime.plusHours(1), dateTime.plusHours(2));
        Segment departureBefore = new Segment(dateTime.plusDays(4), dateTime.plusDays(5));

        Flight flight = new Flight(List.of(departureAfter, departureBefore));

        List<Flight> returned = new DepartureAfterFilter(dateTime).apply(List.of(flight));

        assertEquals(String.format("flight with departure date later than %s wasn't returned", dateTime.format(FMT)),
                1, returned.size());

        assertSame(flight, returned.get(0));
    }
}