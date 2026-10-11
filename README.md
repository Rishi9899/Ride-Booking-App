# Ride Booking App
A rider requests a ride, a driver is matched using a strategy, and the fare is calculated using another strategy.

## Features
- Register riders and drivers
- View available drivers
- Request a ride and match it to a driver
- Calculate fare
- Track ride status: `REQUESTED`, `ASSIGNED`, `COMPLETED`, `CANCELLED`

## Strategies
Set in `src/main/resources/application.properties`. No code changes needed.


| Property | Options | Behaviour |
|---|---|---|
| `ride.matching.strategy` | `nearest` (default) | Closest available driver |
| | `least-active` | Driver with fewest completed rides |
| `ride.fare.strategy` | `default` (default) | 30 + distance x rate (BIKE 8, AUTO 12, CAR 18) |
| | `peak` | Default fare x 1.5 at 08-10 and 17-20 |

## Project Structure
```
model/      Rider, Driver, Ride, FareReceipt, enums
strategy/   RideMatchingStrategy, FareStrategy and implementations
service/    RiderService, DriverService, RideService
Main.java   Console menu
```

## Design Principles
SOLID, DRY, KISS, YAGNI, Law of Demeter. Services depend on interfaces, and new strategies can be added without changing existing code.

## Run
Requires Java 17+ and Maven.
```
mvn clean package
java -jar target/ride-booking-1.0.0.jar
```

## Menu
```
1. Add Rider            5. Complete Ride
2. Add Driver           6. View Rides
3. View Available Drivers   7. Exit
4. Request Ride
```

## Test Guide (try each option)
Run the app and type the values below when prompted. Locations are x, y points on a grid; a smaller distance means a nearer driver.

| Step | Menu option | Enter | Expected result |
|---|---|---|---|
| 1 | `1` Add Rider | `Asha`, `0`, `0` | Rider registered with id 1 |
| 2 | `1` Add Rider | `Bala`, `10`, `10` | Rider registered with id 2 |
| 3 | `2` Add Driver | `Ravi`, `1`, `1`, `auto` | Driver registered with id 1 |
| 4 | `2` Add Driver | `Meera`, `5`, `5`, `car` | Driver registered with id 2 |
| 5 | `2` Add Driver | `Kiran`, `9`, `9`, `bike` | Driver registered with id 3 |
| 6 | `3` View Available Drivers | (nothing) | All 3 drivers listed |
| 7 | `4` Request Ride | rider id `1`, distance `4` | Assigned to Ravi (nearest to Asha) |
| 8 | `4` Request Ride | rider id `2`, distance `10` | Assigned to Kiran (nearest to Bala) |
| 9 | `3` View Available Drivers | (nothing) | Only Meera is listed |
| 10 | `5` Complete Ride | ride id `1` | Fare `78.00` (30 + 4 x 12) |
| 11 | `5` Complete Ride | ride id `1` | Error: ride is already COMPLETED |
| 12 | `6` View Rides | (nothing) | Ride 1 COMPLETED, ride 2 ASSIGNED |
| 13 | `7` Exit | (nothing) | Goodbye! |

Error checks:
- Menu option `9` gives "Invalid option. Please choose 1-7."
- Typing `abc` where a number is expected gives "Please enter a whole number."
- Request Ride with rider id `99` gives "Rider not found with id 99".
- Request Ride with distance `-5` gives "Distance must be greater than 0".
- Request Ride when every driver is busy gives "No drivers are available right now".
