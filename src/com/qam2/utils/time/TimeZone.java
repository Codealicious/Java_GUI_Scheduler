package com.qam2.utils.time;

import java.time.ZoneId;

/**
 * Enum to wrap ZoneIDs for UTC, US East Coast, and the system default.
 * @author Alex Hanson
 */
public enum TimeZone {
    LOCAL(ZoneId.systemDefault()), UTC(ZoneId.of("UTC")), EST(ZoneId.of("US/Eastern"));

    private final ZoneId id;

    TimeZone(ZoneId id) {
        this.id = id;
    }

    /**
     * @return The ZoneId for the given TimeZone instance.
     */
    public ZoneId getID() { return id; }
}
