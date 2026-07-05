package me.mrbast.structory.oldblockdata;

import me.mrbast.structory.version.DataType;

public interface ICustomBlockData {

    <T> void set(String key, DataType type, T value);
    <T> boolean has(String key, DataType type);
    <T> T get(String key, DataType type);
}
