# Class Model (LLD Summary)

This doc summarizes the key classes and their responsibilities as implemented in code.

## `model/` (domain)

### Location (Value Object)
- Fields: `x: double`, `y: double`
- Purpose: uniform coordinate modeling for matching and distance-based fare.

### Rider (Entity)
- Fields: `id: String`, `name: String`, `location: Location`
- Notes: `location` can be updated.

### Driver (Entity)
- Fields: `id: String`, `name: String`, `currentLocation: Location`, `available: boolean`, `ridesCompleted: int`
- Notes: `ridesCompleted` supports Least-Active matching.

### Ride (Entity)
- Fields:
  - `id: String`
  - `rider: Rider`
  - `driver: Driver` (assigned later)
  - `distance: double` (validated as finite, non-negative)
  - `status: RideStatus`
  - `vehicleType: VehicleType`
  - `requestedAt: Instant`, `completedAt: Instant`
  - `fareReceipt: FareReceipt` (set by `RideService` after fare calculation)
- Notes:
  - `RideService` orchestrates the lifecycle and controls status transitions.
  - `Ride` enforces basic invariants (e.g., non-negative distance).

### FareReceipt (Value/Record-like Entity)
- Fields: `rideId`, `amount`, `generatedAt`
- Notes: validates `amount` as finite, non-negative.

### Enums
- `RideStatus`: REQUESTED, ASSIGNED, COMPLETED, CANCELLED
- `VehicleType`: BIKE, AUTO, CAR

## `service/` (application / orchestration)

### RiderService
- Responsibility: register + retrieve riders (in-memory).

### DriverService
- Responsibility: register + retrieve drivers, list available drivers, update availability (in-memory).

### RideService
- Responsibility: ride lifecycle orchestration.
- Collaborators: `RiderService`, `DriverService`, `RideMatchingStrategy`, `FareStrategy`.

## `strategy/` (policies)
### RideMatchingStrategy
- Responsibility: choose the “best” driver for a given rider/location context.
- Implementations: `NearestDriverStrategy`, `LeastActiveDriverStrategy`

### FareStrategy
- Responsibility: compute fare amount given a ride context.
- Implementations: `DefaultFareStrategy`, `PeakHourFareStrategy` (wraps another `FareStrategy`)


