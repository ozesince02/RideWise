package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class RiderService {
    private final Map<String, Rider> ridersById = new LinkedHashMap<>();

    public Rider registerRider(String name, Location location) {
        String id = IdGenerator.newId();
        Rider rider = new Rider(id, name, location);
        ridersById.put(id, rider);
        return rider;
    }

    public Optional<Rider> findById(String riderId) {
        if (riderId == null || riderId.isBlank()) return Optional.empty();
        return Optional.ofNullable(ridersById.get(riderId));
    }

    public Rider getByIdOrThrow(String riderId) {
        Objects.requireNonNull(riderId, "riderId is required");
        Rider rider = ridersById.get(riderId);
        if (rider == null) {
            throw new IllegalArgumentException("Rider not found: " + riderId);
        }
        return rider;
    }

    public List<Rider> listRiders() {
        return new ArrayList<>(ridersById.values());
    }
}


