import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BusSeatReservation {
    private static final int ROWS = 12; // Number of rows
    private static final int SEATS_PER_ROW = 6; // Number of seats including spaces
    private static String[][] seats = new String[ROWS][SEATS_PER_ROW];
    private static Map<String, String[]> reservationDetails = new HashMap<>(); // To store seat reservations with user details

    public static void main(String[] args) {
        initializeSeats();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nBus Seat Reservation System");
            System.out.println("1. View Seats");
            System.out.println("2. Reserve Seats");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. Search Seat by Number");
            System.out.println("5. View Reserved Seats Summary");
            System.out.println("6. Display Seat Availability Summary"); // New feature added here
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> viewSeats();
                case 2 -> reserveSeats(scanner);
                case 3 -> cancelReservation(scanner);
                case 4 -> searchSeat(scanner);
                case 5 -> viewReservedSeatsSummary();
                case 6 -> displaySeatAvailabilitySummary(); // Calling the new feature
                case 7 -> {
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    

    private static void initializeSeats() {
        int seatNumber = 1;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                if (j == 3) {
                    seats[i][j] = "    "; // Space of 4 characters
                } else {
                    seats[i][j] = String.format("%02d", seatNumber++);
                }
            }
        }
    }

    private static void viewSeats() {
        System.out.println("\nCurrent Seat Layout:");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                System.out.print(seats[i][j]);
                if (j == 2) {
                    System.out.print("   "); // Add space after every 3 seats
                } else if (j != SEATS_PER_ROW - 1) {
                    System.out.print(" "); // Single space between seat numbers
                }
            }
            System.out.println();
        }
    }

    private static void reserveSeats(Scanner scanner) {
        viewSeats(); // Show seats before reserving
        System.out.print("Enter the seat numbers separated by spaces (e.g., 01 02 03): ");
        scanner.nextLine(); // Consume newline
        String[] seatNumbers = scanner.nextLine().split(" ");
    
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
    
        String phoneNumber;
        while (true) {
            System.out.print("Enter your phone number (10 digits): ");
            phoneNumber = scanner.nextLine();
            if (phoneNumber.matches("\\d{10}")) {
                break;
            } else {
                System.out.println("Invalid phone number. Please enter exactly 10 digits.");
            }
        }
    
        for (String seatNumber : seatNumbers) {
            boolean reserved = false;
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < SEATS_PER_ROW; j++) {
                    if (seats[i][j].trim().equals(seatNumber)) {
                        if (seats[i][j].equals("XX")) {
                            System.out.println("Seat " + seatNumber + " is already reserved.");
                            reserved = true;
                            break;
                        }
                        seats[i][j] = "XX"; // Mark as reserved
                        reserved = true;
                        System.out.println("Seat " + seatNumber + " reserved successfully for " + name + " (" + phoneNumber + ").");
                        break;
                    }
                }
                if (reserved) break;
            }
            if (!reserved) {
                System.out.println("Seat " + seatNumber + " is invalid.");
            }
        }
    
        viewSeats(); // Show updated seats
    }
    
    private static void cancelReservation(Scanner scanner) {
        viewSeats(); // Show seats before canceling
        System.out.print("Enter the seat numbers separated by spaces (e.g., 01 02 03): ");
        scanner.nextLine(); // Consume newline
        String[] seatNumbers = scanner.nextLine().split(" ");
    
        for (String seatNumber : seatNumbers) {
            boolean seatFound = false;
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < SEATS_PER_ROW; j++) {
                    if (seats[i][j].trim().equals(seatNumber)) {
                        seatFound = true;
                        if (seats[i][j].equals("XX")) {
                            // Replace "XX" with the original seat number
                            seats[i][j] = String.format("%02d", Integer.parseInt(seatNumber));
                            reservationDetails.remove(seatNumber); // Remove reservation details
                            System.out.println("Seat " + seatNumber + " has been canceled.");
                        } else {
                            // Seat is not reserved
                            System.out.println("Seat " + seatNumber + " is not reserved.");
                        }
                        break;
                    }
                }
                if (seatFound) break;
            }
            if (!seatFound) {
                System.out.println("Seat " + seatNumber + " is invalid.");
            }
        }
    
        viewSeats(); // Show updated seats
    }

    private static void searchSeat(Scanner scanner) {
        System.out.print("Enter the seat number to search (e.g., 01): ");
        String seatNumber = scanner.next();
        int seatToFind = Integer.parseInt(seatNumber);
        boolean found = false;
    
        int currentSeatNumber = 1; // Initialize seat number counter
    
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                if (j == 3) continue; // Skip the space between seats
    
                if (currentSeatNumber == seatToFind) {
                    found = true;
                    if (seats[i][j].equals("XX")) {
                        System.out.println("Seat " + seatNumber + " is reserved.");
                    } else {
                        System.out.println("Seat " + seatNumber + " is available.");
                    }
                    break;
                }
                currentSeatNumber++; // Increment the seat number as we move to the next seat
            }
            if (found) break;
        }
    
        if (!found) {
            System.out.println("Seat " + seatNumber + " is invalid.");
        }
    }

    private static void viewReservedSeatsSummary() {
        System.out.println("\nSummary of Reserved Seats:");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                if (seats[i][j].equals("XX")) {
                    String seatNumber = String.format("%02d", i * SEATS_PER_ROW + j + 1);
                    String[] details = reservationDetails.get(seatNumber);
                    System.out.println("Seat " + seatNumber + " reserved by " + details[0] + " (" + details[1] + ")");
                }
            }
        }
    }


    private static void displaySeatAvailabilitySummary() {
        int totalSeats = ROWS * (SEATS_PER_ROW - 1); // Exclude the space between seats
        int reservedSeats = 0;
        
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < SEATS_PER_ROW; j++) {
                if (j == 3) continue; // Skip the space between seats
                if (seats[i][j].equals("XX")) {
                    reservedSeats++;
                }
            }
        }
        
        int availableSeats = totalSeats - reservedSeats;
        
        System.out.println("\nSeat Availability Summary:");
        System.out.println("Total Seats: " + totalSeats);
        System.out.println("Reserved Seats: " + reservedSeats);
        System.out.println("Available Seats: " + availableSeats);
    }
    
}
