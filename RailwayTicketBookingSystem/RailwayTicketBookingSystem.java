import java.util.*;

abstract class Coach {
    private String coachType; 
    private int totalSeats;     
    private int availableSeats; 
    public Coach(String coachType, int totalSeats) {
        this.coachType = coachType;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats; 
    }

    // Getter methods
    public String getCoachType() {
        return coachType;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public abstract double calculateFare(double distance, int numPassengers);

    public List<Integer> allocateSeats(int numSeats) {
        List<Integer> allocatedSeats = new ArrayList<>();
        int seatNumber = getTotalSeats() - getAvailableSeats() + 1;
        for (int i = 0; i < numSeats; i++) {
            if (seatNumber <= getTotalSeats()) {
                allocatedSeats.add(seatNumber);
                seatNumber++;
            } else {
                return null;
            }
        }
        setAvailableSeats(getAvailableSeats() - numSeats); 
        return allocatedSeats;
    }
}

// Sleeper Coach
class SleeperCoach extends Coach {

    private static final double FARE_PER_KM = 1.0;  

    public SleeperCoach(int totalSeats) {
        super("Sleeper", totalSeats); 
    }

    @Override
    public double calculateFare(double distance, int numPassengers) {
        return distance * FARE_PER_KM * numPassengers;
    }
}

// AC Coach
class AC3TierCoach extends Coach {
    private static final double FARE_PER_KM = 2.0;
    public AC3TierCoach(int totalSeats) {
        super("3A", totalSeats);
    }

    @Override
    public double calculateFare(double distance, int numPassengers) {
        return distance * FARE_PER_KM * numPassengers;
    }
}

// 2 Tier AC Coach
class AC2TierCoach extends Coach {
    private static final double FARE_PER_KM = 3.0;

    public AC2TierCoach(int totalSeats) {
        super("2A", totalSeats);
    }

    @Override
    public double calculateFare(double distance, int numPassengers) {
        return distance * FARE_PER_KM * numPassengers;
    }
}

// 1st Class AC
class AC1TierCoach extends Coach {
    private static final double FARE_PER_KM = 4.0;

    public AC1TierCoach(int totalSeats) {
        super("1A", totalSeats);
    }

    @Override
    public double calculateFare(double distance, int numPassengers) {
        return distance * FARE_PER_KM * numPassengers;
    }
}

// Represents a train
class Train {
    private int trainNumber;
    private String source;
    private String destination;
    private double distance; 
    private List<Coach> coaches;   

    public Train(int trainNumber, String source, String destination, double distance) {
        this.trainNumber = trainNumber;
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.coaches = new ArrayList<>();
    }

    public void addCoach(Coach coach) {
        this.coaches.add(coach);
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public double getDistance() {
        return distance;
    }

    public List<Coach> getCoaches() {
        return coaches;
    }

    public void displayTrainInfo() {
        System.out.println("Train Number: " + trainNumber);
        System.out.println("Source: " + source);
        System.out.println("Destination: " + destination);
        System.out.println("Distance: " + distance + " KM");

        for (Coach coach : coaches) {
            System.out.println("  " + coach.getCoachType() + ": " + coach.getAvailableSeats() + " seats available");
        }
    }

}

// Represents a passenger
class Passenger {
    private String name;
    private int age;

    public Passenger(String name, String age) {
        this.name = name;
        this.age = Integer.parseInt(age);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

//Represents a Ticket
class Ticket {
    private static int pnrCounter = 100000000; 
    private int pnr;
    private Train train;
    private Map<Coach, List<Integer>> coachSeats; 
    private List<Passenger> passengers;
    private double totalFare;

    public Ticket(Train train, Map<Coach, List<Integer>> coachSeats, List<Passenger> passengers, double totalFare) {
        this.pnr = generatePNR();
        this.train = train;
        this.coachSeats = coachSeats;
        this.passengers = passengers;
        this.totalFare = totalFare;
    }

    private static synchronized int generatePNR() {
        return pnrCounter++;
    }

    public int getPnr() {
        return pnr;
    }

    public Train getTrain() {
        return train;
    }

    public Map<Coach, List<Integer>> getCoachSeats() {
        return coachSeats;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public void displayTicket() {
        System.out.println("PNR: " + getPnr());
        System.out.println("Train Number: " + train.getTrainNumber());
        System.out.println("From: " + train.getSource() + " To: " + train.getDestination());

        System.out.println("Seat Allocation:");
        for (Map.Entry<Coach, List<Integer>> entry : coachSeats.entrySet()) {
            Coach coach = entry.getKey();
            List<Integer> seats = entry.getValue();
            System.out.println("  " + coach.getCoachType() + ": " + seats); 
        }

        System.out.println("Passengers:");
        for (Passenger passenger : passengers) {
            System.out.println("  - Name: " + passenger.getName() + ", Age: " + passenger.getAge());
        }
        System.out.println("Total Fare: " + getTotalFare());
    }

    public String displaySeatAllocation() {
        StringBuilder sb = new StringBuilder();
        sb.append("Seat Allcated: ").append(train.getTrainNumber()).append(" ")
                .append(train.getSource()).append(" ").append(train.getDestination()).append(" ");

        for (Map.Entry<Coach, List<Integer>> entry : coachSeats.entrySet()) {
            Coach coach = entry.getKey();
            List<Integer> seats = entry.getValue();
            sb.append(coach.getCoachType()).append(":");
            for (int i = 0; i < seats.size(); i++) {
                sb.append(seats.get(i));
                if (i < seats.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append(" ");
        }

        return sb.toString().trim(); 
    }
}

// Manages the booking process
class BookingSystem {

    private List<Train> trains; 
    private Scanner scanner;

    public BookingSystem() {
        this.trains = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void addTrain(Train train) {
        this.trains.add(train);
    }

    public void displayAvailableTrains() {
        System.out.println("Available Trains:");
        for (Train train : trains) {
            System.out.println("Train Number: " + train.getTrainNumber() +
                               ", Source: " + train.getSource() +
                               ", Destination: " + train.getDestination());
        }
        System.out.println(); 
    }

    public void displayAllTrainDetails() {
        System.out.println("Available Trains with Details:");
        for (Train train : trains) {
            train.displayTrainInfo();
            System.out.println();
        }
    }

    public Train findTrain(int trainNumber) {
        for (Train train : trains) {
            if (train.getTrainNumber() == trainNumber) {
                return train;
            }
        }
        return null;
    }


    public Ticket bookTicket(int trainNumber, String coachType, List<Passenger> passengers) {
        Train train = findTrain(trainNumber);

        if (train == null) {
            System.out.println("Train not found.");
            return null;
        }

        Coach selectedCoach = null; 
        List<Coach> availableCoaches = new ArrayList<>();
        int totalPassengers = passengers.size();

        for (Coach coach : train.getCoaches()) {
            if (coach.getCoachType().equalsIgnoreCase(coachType)) {
                selectedCoach = coach;
                break; 
            }
        }

        if (selectedCoach == null || selectedCoach.getAvailableSeats() < totalPassengers) {
             System.out.println("No seats available in the selected coach.");
             return null;
         }


        Map<Coach, List<Integer>> coachSeats = new HashMap<>();
        List<Integer> allocatedSeats = selectedCoach.allocateSeats(totalPassengers);
        if(allocatedSeats == null){
            System.out.println("Not enough seats in selected coach");
            return null;
        }
        coachSeats.put(selectedCoach, allocatedSeats); 

        double totalFare = selectedCoach.calculateFare(train.getDistance(), totalPassengers);

        Ticket ticket = new Ticket(train, coachSeats, passengers, totalFare);

        System.out.println("Ticket booked successfully!");
        return ticket;
    }

    public void startBooking() {
        displayAvailableTrains(); 

        System.out.print("Enter Train number to book Ticket: ");
        int trainNumber = scanner.nextInt();
        scanner.nextLine(); 

        Train selectedTrain = findTrain(trainNumber);
        if (selectedTrain == null) {
            System.out.println("Invalid train number.");
            return;
        }

        System.out.println("Available coach types: S - Sleeper, B - 3 Tier AC, A - 2 Tier AC, H - 1st class AC");
        System.out.print("Enter class: ");
        String coachTypeInput = scanner.nextLine();
        String coachType = "";

        switch (coachTypeInput.toUpperCase()) {
            case "S":
                coachType = "Sleeper";
                break;
            case "B":
                coachType = "3A";
                break;
            case "A":
                coachType = "2A";
                break;
            case "H":
                coachType = "1A";
                break;
            default:
                System.out.println("Invalid class.");
                return;
        }

        System.out.print("Enter number of passengers: ");
        int numPassengers = scanner.nextInt();
        scanner.nextLine();

        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < numPassengers; i++) {
            System.out.print("Enter passenger name: ");
            String name = scanner.nextLine();
            System.out.print("Enter passenger age: ");
            String age = scanner.nextLine();
            passengers.add(new Passenger(name, age));
        }

        Ticket bookedTicket = bookTicket(trainNumber, coachType, passengers);

        if (bookedTicket != null) {
            System.out.println("\n--- Ticket Details ---");
            bookedTicket.displayTicket();
            System.out.println(bookedTicket.displaySeatAllocation()); 
            System.out.println("Booking Confirm: Yes");
            System.out.println("Total Fare: " + bookedTicket.getTotalFare());
        } else {
            System.out.println("Booking failed.");
        }
    }

}

public class RailwayTicketBookingSystem {

    public static void main(String[] args) {
        BookingSystem bookingSystem = new BookingSystem();

        Train train1 = new Train(17726, "Rajkot", "Mumbai", 750);
        train1.addCoach(new SleeperCoach(72));
        train1.addCoach(new AC3TierCoach(48));
        train1.addCoach(new AC2TierCoach(24));
        train1.addCoach(new AC1TierCoach(24));

        Train train2 = new Train(17728, "Delhi", "Bangalore", 2150);
        train2.addCoach(new SleeperCoach(60));
        train2.addCoach(new AC3TierCoach(30));
        train2.addCoach(new AC2TierCoach(24));
        train2.addCoach(new AC1TierCoach(24));

        bookingSystem.addTrain(train1);
        bookingSystem.addTrain(train2);

        bookingSystem.startBooking(); 
    }
}