# SOLID Reflection (How it maps to RideWise)

## SRP (Single Responsibility Principle)
- `RiderService`, `DriverService`, and `RideService` each have a focused purpose.
- Strategies encapsulate matching and pricing rules independently from the ride lifecycle.

## OCP (Open/Closed Principle)
- Add a new driver matching algorithm by implementing `RideMatchingStrategy` (no changes required in `RideService`).
- Add a new pricing algorithm by implementing `FareStrategy` (no changes required in `RideService`).

## LSP (Liskov Substitution Principle)
- Any `RideMatchingStrategy` implementation can replace another without breaking the `RideService` contract.
- Any `FareStrategy` implementation can replace another without changing `RideService` behavior expectations (e.g., returns a finite, non-negative fare).

## ISP (Interface Segregation Principle)
- Small, focused interfaces: `RideMatchingStrategy`, `FareStrategy`.

## DIP (Dependency Inversion Principle)
- `RideService` depends on abstractions (`RideMatchingStrategy`, `FareStrategy`) instead of concrete implementations.

## Practical examples
- To add a new matching policy, create `MyNewMatchingStrategy implements RideMatchingStrategy` and select it from the CLI (`Main.chooseMatchingStrategy`).
- To add surge/discount logic, create a `FareStrategy` wrapper (similar to `PeakHourFareStrategy`) and compose it over `DefaultFareStrategy`.


