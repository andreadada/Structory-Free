package me.mrbast.structory.registry;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class TypeRegistry<T> {
    private final ConcurrentMap<String, T> entries = new ConcurrentHashMap<>();

    public Optional<T> register(String name, T value) {
        return Optional.ofNullable(entries.put(normalize(name), Objects.requireNonNull(value, "value")));
    }

    public Optional<T> find(String name) {
        if (name == null) return Optional.empty();
        return Optional.ofNullable(entries.get(normalize(name)));
    }

    public boolean contains(String name) {
        return find(name).isPresent();
    }

    public Set<String> names() {
        return Collections.unmodifiableSet(new HashSet<>(entries.keySet()));
    }

    public int size() {
        return entries.size();
    }

    private static String normalize(String name) {
        Objects.requireNonNull(name, "name");
        String normalized = name.trim().toLowerCase(Locale.ROOT);
        if (normalized.isEmpty()) throw new IllegalArgumentException("name cannot be blank");
        return normalized;
    }
}
