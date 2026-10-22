import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class BikeShare {
    public static String[] names = {"Library", "Canteen", "Dorm"};
    public static int[] stations = {0, 0, 1, 1, 2, 2};
    public static String[] members = new String[6];
    public static int[] types = new int[6]; // 1 = student, 2 = staff
    public static Scanner input;

    public static void main(String[] args) {
        try (Scanner keyboard = new Scanner(System.in)) {
            input = keyboard;
            while (true) {
                String command = read("Command (rent / return / quit): ");
                if (command == null || command.equalsIgnoreCase("quit")) break;
                switch (command.toLowerCase(java.util.Locale.ROOT)) {
                    case "rent" -> ride(true);
                    case "return" -> ride(false);
                    default -> System.out.println("Unknown command.");
                }
            }
            if (input.ioException() != null) {
                System.err.println("Keyboard error: " + input.ioException().getMessage());
            }
        }
    }

    public static void ride(boolean renting) {
        if (renting) {
            String member = read("Member ID: ");
            if (member == null) return;
            if (member.isEmpty()) {
                System.out.println("Member ID cannot be empty.");
                return;
            }
            int type = number("Type (1 student, 2 staff): ", 1, 2);
            if (type == -1) return;
            int station = number("Station (1 Library, 2 Canteen, 3 Dorm): ", 1, 3);
            if (station == -1) return;
            for (int bike = 0; bike < members.length; bike++) {
                if (members[bike] == null && stations[bike] == station - 1) {
                    members[bike] = member;
                    types[bike] = type;
                    System.out.println("Rented bike " + (bike + 1) + " at " + names[station - 1]);
                    return;
                }
            }
            System.out.println("No bikes available at that station.");
        } else {
            int bike = number("Bike number (1-6): ", 1, 6);
            if (bike == -1) return;
            bike--;
            if (members[bike] == null) {
                System.out.println("That bike is not rented.");
                return;
            }
            int station = number("Return station (1 Library, 2 Canteen, 3 Dorm): ", 1, 3);
            if (station == -1) return;
            int minutes = number("Ride minutes: ", 0, Integer.MAX_VALUE);
            if (minutes == -1) return;
            long fee = (long) minutes * (types[bike] == 1 ? 2 : 3);
            try (BufferedWriter file = Files.newBufferedWriter(Path.of("rides.csv"),
                    StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                // CSV columns: bike, member ID, type, start station, end station, minutes, fee.
                file.write((bike + 1) + ",\"" + members[bike].replace("\"", "\"\"") + "\","
                        + types[bike] + "," + names[stations[bike]] + "," + names[station - 1]
                        + "," + minutes + "," + fee);
                file.newLine();
            } catch (IOException | SecurityException e) {
                System.err.println("Could not save ride: " + e.getMessage()
                        + ". Return not completed; please retry.");
                return;
            }
            stations[bike] = station - 1;
            members[bike] = null;
            types[bike] = 0;
            System.out.println("Receipt: bike " + (bike + 1) + ", " + minutes
                    + " minutes, fee " + fee + " baht.");
        }
    }

    public static String read(String prompt) {
        System.out.print(prompt);
        return input.hasNextLine() ? input.nextLine().trim() : null;
    }

    public static int number(String prompt, int min, int max) {
        String text = read(prompt);
        if (text == null) return -1;
        try {
            int value = Integer.parseInt(text);
            if (value >= min && value <= max) return value;
        } catch (NumberFormatException e) {
            System.out.println("Enter a whole number from " + min + " to " + max + ".");
            return -1;
        }
        System.out.println("Enter a whole number from " + min + " to " + max + ".");
        return -1;
    }
}
