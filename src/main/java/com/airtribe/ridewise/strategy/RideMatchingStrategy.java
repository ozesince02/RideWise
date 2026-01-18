package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;

import java.util.List;

public interface RideMatchingStrategy {
    /**
     * Finds a suitable driver for the given rider from the provided list.
     * Returns null when no suitable driver exists.
     */
    Driver findDriver(Rider rider, List<Driver> drivers);
}


