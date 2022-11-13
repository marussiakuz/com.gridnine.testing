package com.gridnine.testing;

import com.gridnine.testing.filter.DepartureAfterFilter;
import com.gridnine.testing.filter.DepartureBeforeArrivalFilter;
import com.gridnine.testing.filter.Filter;
import com.gridnine.testing.filter.TotalTimeOnEarthNotExceedFilter;
import com.gridnine.testing.model.Flight;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Main {

    private static final List<Filter> FILTERS;

    static {
        FILTERS = new ArrayList<>();
        FILTERS.add(new DepartureAfterFilter(LocalDateTime.now()));
        FILTERS.add(new DepartureBeforeArrivalFilter());
        FILTERS.add(new TotalTimeOnEarthNotExceedFilter(Duration.of(2, ChronoUnit.HOURS)));
    }

    public static void main( String[] args ) {
        List<Flight> flights = FlightBuilder.createFlights();

        printFlights(Collections.singletonMap("Total flights: ", flights));

        FILTERS.forEach(filter -> {
            List<Flight> filtered = filter.apply(flights);
            printResult(filter, flights, filtered);
        });
    }

    private static void printResult(Filter filter, List<Flight> total, List<Flight> filtered) {
        StringBuilder builder = new StringBuilder();
        Map<String, List<Flight>> infoToPrint = new LinkedHashMap<>();

        switch (filter.getClass().getSimpleName()) {
            case "DepartureAfterFilter":
                builder.append("CASE 1. Exclude flights with departure before the current time");
                break;
            case "DepartureBeforeArrivalFilter":
                builder.append("CASE 2. Exclude flights that have segments with an arrival date earlier than the " +
                        "departure date");
                break;
            case "TotalTimeOnEarthNotExceedFilter":
                builder.append("CASE 3. Exclude flights with a total time on the ground of more than 2 hours");
                break;
            default:
                builder.append("Unknown filter");
        }

        ArrayList<Flight> excluded = new ArrayList<>(total);
        excluded.removeAll(filtered);

        infoToPrint.put(builder.append("\nfiltered: ").toString(), filtered);
        infoToPrint.put("excluded: ", excluded);
        printFlights(infoToPrint);
    }

    private static void printFlights(Map<String, List<Flight>> info) {
        info.forEach((title, flights) -> {
            System.out.println(title);
            flights.forEach(System.out::println);
        });
        System.out.println("======================================================================");
    }
}
