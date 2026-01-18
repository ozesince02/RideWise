# RideWise Requirements (MVP)

## Scope
RideWise is a **console-only**, **in-memory** ride-sharing simulation focused on demonstrating clean design and the Strategy pattern.

## Functional requirements
- **Register riders**
- **Register drivers**
- **List available drivers**
- **Request a ride**
  - Pick a **matching strategy** (e.g., nearest / least-active)
  - Pick a **fare strategy** (e.g., default / peak-hour wrapper)
  - Choose `VehicleType` (BIKE/AUTO/CAR)
- **Complete a ride**
- **Cancel a ride**
- **View all rides**
- **Track ride status**: `REQUESTED` → `ASSIGNED` → (`COMPLETED` | `CANCELLED`)

## Non-functional requirements
- **Extensible algorithms** (matching, pricing) without modifying core orchestration code
- **Low coupling** between orchestration and policies
- **Readable and maintainable** (small classes, clear responsibilities)

## Assumptions / constraints
- **No persistence**: restarting the app loses all data.
- **Single-process**: no concurrency concerns modeled.
- **CLI input** drives the system (see `com.airtribe.ridewise.Main` menu).

## Domain entities (code truth)
Package: `com.airtribe.ridewise.model`

- **Rider**: `id`, `name`, `location`
- **Driver**: `id`, `name`, `currentLocation`, `available`, `ridesCompleted`
- **Ride**: `id`, `rider`, `driver (optional until assigned)`, `distance`, `status`, `vehicleType`, `requestedAt`, `completedAt`, `fareReceipt`
- **FareReceipt**: `rideId`, `amount`, `generatedAt`
- **Enums**: `RideStatus`, `VehicleType`

## Extension points
- **Matching**: `com.airtribe.ridewise.strategy.RideMatchingStrategy`
- **Fare**: `com.airtribe.ridewise.strategy.FareStrategy`
