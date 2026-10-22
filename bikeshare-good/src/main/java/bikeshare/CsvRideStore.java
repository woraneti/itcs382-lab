package bikeshare;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public final class CsvRideStore implements RideStore {
    private final Path file;

    public CsvRideStore(Path file) {
        this.file = Objects.requireNonNull(file);
    }

    @Override
    public void append(Ride ride) throws IOException {
        String header = !Files.exists(file) || Files.size(file) == 0
                ? "member_id,member_type,bike_id,origin,destination,minutes,fee_baht\n"
                : "";

        String row = String.join(",",
                quote(ride.memberId()),
                ride.memberType().name(),
                quote(ride.bikeId()),
                ride.origin().name(),
                ride.destination().name(),
                Integer.toString(ride.minutes()),
                Long.toString(ride.fee()));

        Files.writeString(file, header + row + "\n", StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private static String quote(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
