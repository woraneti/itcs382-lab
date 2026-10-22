package bikeshare;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Scanner;

public final class BikeShareCli {
    private final BikeShareService service;
    private final Scanner input;
    private final PrintStream output;

    public BikeShareCli(
            BikeShareService service, Scanner input, PrintStream output
    ) {
        this.service = service;
        this.input = input;
        this.output = output;
    }

    public void run() {
        output.println("rent <memberId> <student|staff> <Library|Canteen|Dorm>");
        output.println("return <memberId> <Library|Canteen|Dorm> <minutes>");
        output.println("quit");
        output.println("Member IDs cannot contain whitespace.");

        while (true) {
            output.print("> ");
            output.flush();

            if (!input.hasNextLine()) {
                return;
            }

            String line = input.nextLine().strip();
            if (line.equalsIgnoreCase("quit")) {
                return;
            }
            if (line.isEmpty()) {
                continue;
            }

            try {
                execute(line.split("\\s+"));
            } catch (IllegalArgumentException e) {
                output.println("Error: " + e.getMessage());
            } catch (IOException e) {
                output.println(
                        "Could not save ride; rental remains active. Retry the return.");
            }
        }
    }

    private void execute(String[] parts) throws IOException {
        if (parts.length != 4) {
            throw new IllegalArgumentException("Use a command shown above.");
        }

        switch (parts[0].toLowerCase(Locale.ROOT)) {
            case "rent" -> {
                MemberType type = parseType(parts[2]);
                Station station = parseStation(parts[3]);
                String bike = service.rent(parts[1], type, station);

                output.println("Rented bike " + bike + " to " + parts[1] + ".");
            }
            case "return" -> {
                int minutes;
                try {
                    minutes = Integer.parseInt(parts[3]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                            "Minutes must be a positive whole number.");
                }

                Ride ride = service.returnBike(
                        parts[1], parseStation(parts[2]), minutes);

                output.printf(
                        "Receipt | Bike: %s | Minutes: %d | Fee: %d baht%n",
                        ride.bikeId(), ride.minutes(), ride.fee());
            }
            default -> throw new IllegalArgumentException(
                    "Unknown command. Use rent, return or quit.");
        }
    }

    private static MemberType parseType(String value) {
        try {
            return MemberType.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Member type must be student or staff.");
        }
    }

    private static Station parseStation(String value) {
        try {
            return Station.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Station must be Library, Canteen or Dorm.");
        }
    }
}
