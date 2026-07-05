package me.mrbast.structory.util;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class TimeUtil {
    private TimeUtil() {
    }

    public static long toMillis(long duration, TimeUnit unit) {
        requireNonNegative(duration, "duration");
        return Objects.requireNonNull(unit, "unit").toMillis(duration);
    }

    public static long requireNonNegative(long value, String name) {
        if (value < 0) throw new IllegalArgumentException(name + " cannot be negative");
        return value;
    }

    public static long requirePositive(long value, String name) {
        if (value <= 0) throw new IllegalArgumentException(name + " must be positive");
        return value;
    }
}
