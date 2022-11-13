package com.gridnine.testing.filter;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.Assert.*;

public class TotalTimeOnEarthNotExceedFilterTest {

    @Test
    public void ifTotalTimeOnTheGroundMoreThanMaxThenWillNotBeReturned() {
        Duration max = Duration.of(6, ChronoUnit.HOURS);

        Segment first = new Segment(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        Segment second = new Segment(LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(4));
        Segment third = new Segment(LocalDateTime.now().plusHours(10), LocalDateTime.now().plusHours(11));

        Flight flight = new Flight(List.of(first, second, third));

        List<Flight> returned = new TotalTimeOnEarthNotExceedFilter(max).apply(List.of(flight));

        assertTrue(String.format("flight with total time on the ground = %s hours at max time on the ground = %s " +
                "hours wasn't filtered out", Duration.between(first.getArrivalDate(), second.getDepartureDate())
                .plus(Duration.between(second.getArrivalDate(), third.getDepartureDate())).toHours(), max.toHours()),
                returned.isEmpty());
    }

    @Test
    public void ifTotalTimeOnTheGroundEqualsMaxThenWillBeReturned() {
        Duration max = Duration.of(1, ChronoUnit.HOURS);

        LocalDateTime dateTime = LocalDateTime.now();

        Segment first = new Segment(dateTime, dateTime.plusHours(2));
        Segment second = new Segment(dateTime.plusHours(3), dateTime.plusHours(4));

        Flight flight = new Flight(List.of(first, second));

        List<Flight> returned = new TotalTimeOnEarthNotExceedFilter(max).apply(List.of(flight));

        assertEquals("the flight with total time on the ground equals max time wasn't returned",
                1, returned.size());

        assertSame(flight, returned.get(0));
    }

    @Test
    public void ifFlightHasOnlyOneSegmentThenWillBeReturned() {
        Duration max = Duration.of(5, ChronoUnit.MINUTES);

        Segment segment = new Segment(LocalDateTime.now(), LocalDateTime.now().plusHours(5));

        Flight flight = new Flight(List.of(segment));

        List<Flight> returned = new TotalTimeOnEarthNotExceedFilter(max).apply(List.of(flight));

        assertEquals("the flight with only one segment wasn't returned", 1, returned.size());

        assertSame(flight, returned.get(0));
    }

    @Test
    public void ifTotalTimeOnTheGroundLessThanMaxThenWillBeReturned() {
        Duration max = Duration.of(1, ChronoUnit.HOURS);

        LocalDateTime dateTime = LocalDateTime.now();

        Segment first = new Segment(dateTime, dateTime.plusHours(2));
        Segment second = new Segment(dateTime.plusHours(2).minusMinutes(59), dateTime.plusHours(4));

        Flight flight = new Flight(List.of(first, second));

        List<Flight> returned = new TotalTimeOnEarthNotExceedFilter(max).apply(List.of(flight));

        assertEquals("the flight with total time on the ground less than max wasn't returned",
                1, returned.size());

        assertSame(flight, returned.get(0));
    }
}