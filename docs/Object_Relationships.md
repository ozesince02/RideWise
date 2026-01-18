# Object Relationships

## High-level view

```
Rider 1 ──────── * Ride * ──────── 0..1 Driver
                    │
                    └── 0..1 FareReceipt
```

## Associations
- **Rider → Ride**: A Rider can have many rides over time; a Ride has exactly one Rider.
- **Driver → Ride**: A Driver can serve many rides over time; a Ride has at most one assigned Driver.

## Composition
- **Ride → FareReceipt**: FareReceipt is created for a ride and stored on the ride (set by `RideService`).

## Strategy Composition
- **RideService → RideMatchingStrategy**: injected collaborator used to select a driver.
- **RideService → FareStrategy**: injected collaborator used to compute fare.

## Lifecycle notes (simplified)
- **requestRide**: create Ride (`REQUESTED`), select driver, assign driver, mark `ASSIGNED`, compute fare receipt.
- **completeRide**: mark `COMPLETED`, set completion timestamp, free up driver and increment ride counts.
- **cancelRide**: mark `CANCELLED`, free up driver if already assigned.


