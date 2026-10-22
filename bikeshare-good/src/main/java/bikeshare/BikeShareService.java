package bikeshare;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class BikeShareService {
    private final Map<Station, Deque<String>> bikes =
            new EnumMap<>(Station.class);
    private final Map<String, Rental> rentals = new HashMap<>();
    private final RideStore store;

    public BikeShareService(RideStore store) {
        this.store = Objects.requireNonNull(store);

        int number = 1;
        for (Station station : Station.values()) {
            Deque<String> available = new ArrayDeque<>();
            available.addLast("B" + number++);
            available.addLast("B" + number++);
            bikes.put(station, available);
        }
    }

    public String rent(String memberId, MemberType type, Station station) {
        requireMemberId(memberId);
        Objects.requireNonNull(type, "Member type is required.");
        Objects.requireNonNull(station, "Station is required.");

        if (rentals.containsKey(memberId)) {
            throw new IllegalArgumentException("Member already has a bike.");
        }

        Deque<String> available = bikes.get(station);
        if (available.isEmpty()) {
            throw new IllegalArgumentException(
                    "No bikes available at " + station + ".");
        }

        String bikeId = available.removeFirst();
        rentals.put(memberId, new Rental(type, bikeId, station));
        return bikeId;
    }

    public Ride returnBike(
            String memberId, Station destination, int minutes
    ) throws IOException {
        requireMemberId(memberId);
        Objects.requireNonNull(destination, "Station is required.");

        Rental rental = rentals.get(memberId);
        if (rental == null) {
            throw new IllegalArgumentException("Member has no active rental.");
        }

        Ride ride = new Ride(
                memberId, rental.type(), rental.bikeId(),
                rental.origin(), destination, minutes);

        // Leave the rental active if saving fails.
        store.append(ride);
        rentals.remove(memberId);
        bikes.get(destination).addLast(rental.bikeId());

        return ride;
    }

    private static void requireMemberId(String memberId) {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("Member ID is required.");
        }
    }

    private record Rental(
            MemberType type, String bikeId, Station origin
    ) {}
}
