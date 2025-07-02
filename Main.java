import java.time.Duration;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        double tableArea = (1.2 * 0.8);
        int robotAverageTime = 10;

        RestrictedSpots[] restrictedSpots = new RestrictedSpots[5];
        restrictedSpots[0] = new RestrictedSpots("S01", "Dining Foyer", 3.0, 2.5, robotAverageTime, tableArea);
        restrictedSpots[1] = new RestrictedSpots("S02", "Main Dining Hall", 10.0, 8.0, robotAverageTime, tableArea);
        restrictedSpots[2] = new RestrictedSpots("S03", "Dining Room 1", 5.0, 4.0, robotAverageTime, tableArea);
        restrictedSpots[3] = new RestrictedSpots("S04", "Dining Room 2", 4.5, 3.5, robotAverageTime, tableArea);
        restrictedSpots[4] = new RestrictedSpots("S05", "Family Dining Room", 6.0, 5.0, robotAverageTime, tableArea);

        Drinks[] drinks = new Drinks[5];
        drinks[0] = new Drinks("Cola", "Classic fizzy drink", 2.50, 3);
        drinks[1] = new Drinks("Lemonade", "Refreshing lemon soda", 2.20, 3);
        drinks[2] = new Drinks("Iced Tea", "Cold tea with lemon", 2.80, 3);
        drinks[3] = new Drinks("Orange Juice", "Freshly squeezed juice", 3.00, 3);
        drinks[4] = new Drinks("Mineral Water", "Still natural spring water", 1.50, 3);

        int sel;

        // Restricted Spots Selection
        while (true) {
            System.out.printf("%-4s %-20s %-15s\n", "No.", "Name", "Size (W × H)");
            for (int i = 0; i < restrictedSpots.length; i++) {
                RestrictedSpots spot = restrictedSpots[i];
                System.out.printf("%-4d %-20s %.1fm × %.1fm\n", i + 1, spot.getSpotName(), spot.getSpotWidth(), spot.getSpotHeight());
            }
            System.out.println("Please choose a spot: ");
            try {
                sel = Integer.parseInt(scanner.nextLine()) - 1;
            } catch (Exception e) {
                sel = 6; // Invalid selection case
            }

            if (sel >= 5 || sel < 0) {
                System.out.println("Invalid selection. Please try again");
                continue;
            } else if (restrictedSpots[sel].getCurrentOccupancy() > restrictedSpots[sel].getMaximumCapacity()) {
                int waitLine = restrictedSpots[sel].getCurrentOccupancy() - restrictedSpots[sel].getMaximumCapacity() + 1;
                int waitTime = waitLine * restrictedSpots[sel].getPermittedAverageTimePerRobot();
                boolean yes = waitChoice(waitTime, waitLine);
                if (yes) {
                    waitingTime(waitTime);
                } if(!yes)
                    continue;
            }
            // Check Customer's Distance
            System.out.printf("Entering %s\n", restrictedSpots[sel].getSpotName());
            if (!checkCustomerDistance())
                displayCasualContactInfo(restrictedSpots[sel]);

            if(checkRobotDistance(restrictedSpots[sel]))
                break;
        }


        // Drink Selection
        System.out.printf("\n%-4s %-20s %-30s %-10s %-10s\n", "No.", "Name", "Description", "Price", "Wait Time");
        for (int i = 0; i < drinks.length; i++) {
            Drinks drink = drinks[i];
            System.out.printf("%-4d %-20s %-30s $%-9.2f %-14s\n",
                    i + 1,
                    drink.getName(),
                    drink.getDescription(),
                    drink.getPrice(),
                    drink.getWaitTime()
            );
        }

        System.out.println("Please choose a drink: ");
        int quantity;
        while (true) {
            try {
                sel = Integer.parseInt(scanner.nextLine()) - 1;
                System.out.println("How many would you like: ");
                quantity = Integer.parseInt(scanner.nextLine());
                if (sel >= 0 && sel < 5) {
                    break; // Valid selection
                } else {
                    System.out.println("Invalid selection. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        System.out.printf("You have selected: %d %s\n", quantity, drinks[sel].getName());
        waitingTime(drinks[sel].getWaitTime()*quantity);
        System.out.printf("Your %d %s has been served.\n", quantity, drinks[sel].getName());
        System.out.printf("Your total comes to %.2f.\n", drinks[sel].getPrice()*quantity);
    }

    // Wait Choice and Time
    public static boolean waitChoice(int waitTime, int waitLine) {
        while (true) {
            long hours = TimeUnit.MINUTES.toHours(waitTime);
            long minutes = waitTime - TimeUnit.HOURS.toMinutes(hours);
            System.out.println("\nThe room is currently full");
            System.out.printf("You are currently the %d person in line", waitLine);
            System.out.printf("Your spot will be available in %d hour(s) %d min\n", hours, minutes);
            System.out.println("Would you like to wait? (y/n)");
            String yes = scanner.nextLine();
            if (yes.equalsIgnoreCase("y")) {
                return true;
            } else if (yes.equalsIgnoreCase("n")) {
                return false;
            }
        }
    }

    // Waiting time countdown
    public static void waitingTime(int waitTime) {
        LocalTime currentTime = LocalTime.now();
        LocalTime endTime = currentTime.plusSeconds(waitTime);
        do {
            currentTime = LocalTime.now();
            Duration remaining = Duration.between(currentTime, endTime);

            long hours = remaining.toMinutes();
            long minutes = remaining.toSeconds() % 60;

            System.out.printf("Time left: %02d:%02d\r", hours, minutes);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } while (LocalTime.now().isBefore(endTime));
    }

    // Check Customer Distance
    private static boolean checkCustomerDistance() {
        double left, right, front, back;
        while (true) {
            try {
                System.out.print("Enter distance from the LEFT side of the robot: ");
                left = Double.parseDouble(scanner.nextLine()) - 1;

                System.out.print("Enter distance from the RIGHT side of the robot: ");
                right = Double.parseDouble(scanner.nextLine()) - 1;

                System.out.print("Enter distance from the FRONT of the robot: ");
                front = Double.parseDouble(scanner.nextLine()) - 1;

                System.out.print("Enter distance from the BACK of the robot: ");
                back = Double.parseDouble(scanner.nextLine()) - 1;

                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter valid decimal numbers for all distances.");
            }
        }

        if (left < 1) {
            System.out.printf("Please move %.1fm away from the LEFT side of the robot.\n", 1 - left);
            return false;
        }
        if (right < 1) {
            System.out.printf("Please move %.1fm away from the RIGHT side of the robot.\n", 1 - right);
            return false;
        }
        if (front < 1) {
            System.out.printf("Please move %.1fm away from the FRONT of the robot.\n", 1 - front);
            return false;
        }
        if (back < 1) {
            System.out.printf("Please move %.1fm away from the BACK of the robot.\n", 1 - back);
            return false;
        }

        System.out.println("You are safe in dynamic distancing!");
        return true;
    }

    // Display Casual Contact Info
    private static void displayCasualContactInfo(RestrictedSpots spot) {
        String robotID = "20619586";
        String robotName = "Tan Wei Sin";

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        DateTimeFormatter firstFormatTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = time.format(firstFormatTime);

        System.out.println("\n***** Casual Contact Record *****");
        System.out.println("Robot ID: " + robotID);
        System.out.println("Robot Full Name: " + robotName);
        System.out.println("Selected Restricted Spot ID: " + spot.getSpotID());
        System.out.println("Spot Name: " + spot.getSpotName());
        System.out.println("Date: " + date);
        System.out.println("Time: " + formattedTime);
        System.out.println("Contact Status: Casual Contact");
    }

    public static boolean checkRobotDistance(RestrictedSpots spots) {
        int personCount;
        double[][] peoplePositions;
        // Step 1: Generate people
        Random rand = new Random();
        // Randomly decide how many people to generate (e.g., 3 to 7 people)
        personCount = rand.nextInt(5) + 3;
        peoplePositions = new double[personCount][2];

        double currentRoomWidth = spots.getSpotWidth();
        double currentRoomLength = spots.getSpotHeight();

        for (int i = 0; i < personCount; i++) {
            // Ensure people are at least 1.0m from the walls
            double x = 1.0 + (currentRoomWidth - 2.0) * rand.nextDouble();
            double y = 1.0 + (currentRoomLength - 2.0) * rand.nextDouble();
            peoplePositions[i][0] = x;
            peoplePositions[i][1] = y;
            System.out.printf("Generated Person %d at (%.2f, %.2f)\n", i + 1, x, y);
        }

        // Step 2: Get robot position
        System.out.println("\n***** Enter Robot Position *****");
        System.out.printf("(Room Width: %.1f m, Length: %.1f m)\n", currentRoomWidth, currentRoomLength);

        System.out.print("Enter robot's X position (width): ");
        double robotX = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter robot's Y position (length): ");
        double robotY = Double.parseDouble(scanner.nextLine());

        // Step 3: Check if robot is valid
        if (isRobotPositionValid(robotX, robotY, spots, personCount, peoplePositions)) {
            System.out.println("\nRobot position is valid.");
            return true;
        } else {
            System.out.println("\nRobot position is invalid. Please relocate.");
            displayCasualContactInfo(spots);
            return false;
        }

    }

    private static boolean isRobotPositionValid(double robotX, double robotY, RestrictedSpots spots, int personCount,
    double[][] peoplePositions) {
        // Check wall boundaries (1m min spacing)
        if (robotX < 1.0 || robotX > spots.getSpotWidth() - 1.0 ||
                robotY < 1.0 || robotY > spots.getSpotWidth() - 1.0) {
            System.out.println("Robot is too close to the wall or outside the room bounds.");
            return false;
        }

        // Check distance from people
        for (int i = 0; i < personCount; i++) {
            double dx = robotX - peoplePositions[i][0];
            double dy = robotY - peoplePositions[i][1];
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance < 1.0) {
                System.out.printf("Too close to Person %d: Distance = %.2f m\n", i + 1, distance);
                return true;
            }
        }
        return true;
    }
}