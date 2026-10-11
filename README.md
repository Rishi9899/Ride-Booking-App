# Ride Booking (LLD)

Spring Boot 3 / Java 17 console app demonstrating SOLID, Strategy pattern and layered design.

## Run
    mvn spring-boot:run
or
    mvn clean package && java -jar target/ride-booking-1.0.0.jar

## Switch strategies (no code change - OCP)
Edit `src/main/resources/application.properties`:

    ride.matching.strategy=nearest      # or: least-active
    ride.fare.strategy=default          # or: peak

## Structure
    model/      Rider, Driver, Ride, FareReceipt, Location, RideStatus, VehicleType
    strategy/   RideMatchingStrategy (Nearest, LeastActive), FareStrategy (Default, PeakHour), FareCalculator
    service/    RiderService, DriverService, RideService
    exception/  domain exceptions caught by the console
    Main.java   menu (service layer only)

## Fare
base 30 + distance * per-km rate (BIKE 8, AUTO 12, CAR 18); peak = x1.5 during 08-10 and 17-20.
