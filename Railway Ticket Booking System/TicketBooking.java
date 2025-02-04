import java.util.*;

class Seat {
    int seatNumber;
    boolean isBooked;

    Seat(int seatNumber) {
        this.seatNumber = seatNumber;
        this.isBooked = false;
    }
}

class Coach {
    String coachName;
    Seat[] seats;

    Coach(String coachName, int totalSeats) {
        this.coachName = coachName;
        this.seats = new Seat[totalSeats];
        for (int i = 0; i < totalSeats; i++) {
            seats[i] = new Seat(i + 1);
        }
    }

    int availableSeats() {
        int count = 0;
        for (Seat seat : seats) {
            if (!seat.isBooked) count++;
        }
        return count;
    }

    boolean bookSeats(Passenger[] passengers, StringBuilder allocatedSeats) {
        int count = 0;
        for (Seat seat : seats) {
            if (!seat.isBooked) {
                seat.isBooked = true;
                passengers[count].seatNumber = seat.seatNumber;
                allocatedSeats.append(coachName).append(":").append(seat.seatNumber).append(", ");
                count++;
                if (count == passengers.length) return true;
            }
        }
        return false;
    }
}

class Train {
    String number, source, destination;
    Coach[] coaches;
    int distance;

    Train(String number, String source, String destination, int distance, Coach[] coaches) {
        this.number = number;
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.coaches = coaches;
    }

    void displayTrainInfo() {
        System.out.print(number + " " + source + " " + destination);
        for (Coach coach : coaches) {
            System.out.print(" " + coach.coachName + "-" + coach.availableSeats());
        }
        System.out.println();
    }
}

class Passenger {
    String name;
    int age;
    int seatNumber;

    Passenger(String name, int age) {
        this.name = name;
        this.age = age;
        this.seatNumber = -1;
    }
}

class TicketBooking {
    static int pnrCounter = 100000001;
    static int[] fareRates = {1, 2, 3, 4};

    static Train findTrain(Train[] trains, String trainNumber) {
        for (Train train : trains) {
            if (train.number.equals(trainNumber)) {
                return train;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Coach[] coaches1 = {
            new Coach("S1", 72), new Coach("S2", 72), new Coach("B1", 72), new Coach("A1", 48), new Coach("H1", 24)
        };
        Coach[] coaches2 = {
            new Coach("S1", 15), new Coach("S2", 20), new Coach("S3", 50), new Coach("B1", 36), new Coach("B2", 48)
        };

        Train[] trains = {
            new Train("17726", "Rajkot", "Mumbai", 750, coaches1),
            new Train("17728", "Rajkot", "Mumbai", 750, coaches2)
        };

        System.out.print("Enter Starting Point: ");
        String start = scanner.next();
        System.out.print("Enter Destination: ");
        String destination = scanner.next();
        System.out.print("Enter Class (S/B/A/H): ");
        char coachType = scanner.next().charAt(0);
        System.out.print("Enter No. of Passengers: ");
        int passengersCount = scanner.nextInt();

        Passenger[] passengers = new Passenger[passengersCount];
        for (int i = 0; i < passengersCount; i++) {
            System.out.print("Enter Name for Passenger " + (i + 1) + ": ");
            String name = scanner.next();
            System.out.print("Enter Age for Passenger " + (i + 1) + ": ");
            int age = scanner.nextInt();
            passengers[i] = new Passenger(name, age);
        }

        System.out.println("Total Trains=" + trains.length);
        for (Train train : trains) {
            train.displayTrainInfo();
        }

        System.out.print("Enter Train number to book Ticket: ");
        String selectedTrainNo = scanner.next();
        Train selectedTrain = findTrain(trains, selectedTrainNo);

        if (selectedTrain == null) {
            System.out.println("Invalid Train Number!");
            return;
        }

        int fareRateIndex = "SBAH".indexOf(coachType);
        if (fareRateIndex == -1) {
            System.out.println("Invalid Coach Type!");
            return;
        }

        StringBuilder allocatedSeats = new StringBuilder();
        for (Coach coach : selectedTrain.coaches) {
            if (coach.coachName.startsWith(String.valueOf(coachType))) {
                if (coach.bookSeats(passengers, allocatedSeats)) {
                    break;
                }
            }
        }

        if (allocatedSeats.length() == 0) {
            System.out.println("No Seats Available");
            return;
        }

        int totalFare = selectedTrain.distance * fareRates[fareRateIndex] * passengersCount;
        System.out.println("Total Fare= " + totalFare);
        System.out.print("Booking Confirm (Yes/No): ");
        String confirm = scanner.next();

        if (confirm.equalsIgnoreCase("Yes")) {
            System.out.println("Ticket Booked");
            System.out.println("PNR:" + (pnrCounter++));
            System.out.print("Seat Allocated: " + selectedTrain.number + " " + start + " " + destination + " ");
            System.out.println(allocatedSeats.substring(0, allocatedSeats.length() - 2));
        } else {
            System.out.println("Booking Cancelled");
        }
    }
}