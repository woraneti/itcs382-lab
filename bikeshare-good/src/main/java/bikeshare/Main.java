package bikeshare;

import java.nio.file.Path;
import java.util.Scanner;

public final class Main {
    private Main() {}

    public static void main(String[] args) {
        RideStore store = new CsvRideStore(Path.of("rides.csv"));

        try (Scanner input = new Scanner(System.in)) {
            new BikeShareCli(
                    new BikeShareService(store), input, System.out
            ).run();
        }
    }
}
