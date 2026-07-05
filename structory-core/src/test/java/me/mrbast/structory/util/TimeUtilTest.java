package me.mrbast.structory.util;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilTest {

    @Test
    void convertsUsingTheRequestedUnit() {
        assertEquals(2_000L, TimeUtil.toMillis(2, TimeUnit.SECONDS));
        assertEquals(120_000L, TimeUtil.toMillis(2, TimeUnit.MINUTES));
    }

    @Test
    void validatesDurations() {
        assertThrows(IllegalArgumentException.class, () -> TimeUtil.toMillis(-1, TimeUnit.SECONDS));
        assertThrows(IllegalArgumentException.class, () -> TimeUtil.requirePositive(0, "period"));
        assertThrows(NullPointerException.class, () -> TimeUtil.toMillis(1, null));
    }
}
