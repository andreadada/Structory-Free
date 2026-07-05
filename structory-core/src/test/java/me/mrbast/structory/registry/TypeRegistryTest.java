package me.mrbast.structory.registry;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TypeRegistryTest {

    @Test
    void normalizesNamesAndReturnsReplacedValue() {
        TypeRegistry<String> registry = new TypeRegistry<>();

        assertTrue(registry.register(" Item ", "first").isEmpty());
        assertEquals("first", registry.find("ITEM").orElseThrow());
        assertEquals("first", registry.register("item", "second").orElseThrow());
        assertEquals(Set.of("item"), registry.names());
    }

    @Test
    void rejectsInvalidEntriesAndExposesAnImmutableSnapshot() {
        TypeRegistry<String> registry = new TypeRegistry<>();
        registry.register("item", "value");
        Set<String> names = registry.names();

        assertThrows(IllegalArgumentException.class, () -> registry.register("  ", "value"));
        assertThrows(NullPointerException.class, () -> registry.register("item", null));
        assertThrows(UnsupportedOperationException.class, () -> names.add("other"));
    }
}
