package me.mrbast.structory.util;

import me.mrbast.structory.version.Version;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class PersistentDataUtil {


    private static PersistentDataUtil INSTANCE;
    public static PersistentDataUtil getInstance(){
        if( INSTANCE == null ){ INSTANCE = new PersistentDataUtil();}
        return INSTANCE;
    }

    private Map<String, PersistentDataType<?,?>> dataTypeMap = new HashMap<>();

    public PersistentDataUtil() {

        dataTypeMap.put("string", PersistentDataType.STRING);
        dataTypeMap.put("boolean", Version.getInstance().persistentDataTypeBoolean());
        dataTypeMap.put("int", PersistentDataType.INTEGER);
        dataTypeMap.put("long", PersistentDataType.LONG);
        dataTypeMap.put("float", PersistentDataType.FLOAT);
        dataTypeMap.put("double", PersistentDataType.DOUBLE);
        dataTypeMap.put("byte", PersistentDataType.BYTE);
        dataTypeMap.put("short", PersistentDataType.SHORT);

    }

    public <T,Z> PersistentDataType<T,Z> getDataType(String type){
        return (PersistentDataType<T, Z>) dataTypeMap.get(type.toLowerCase());
    }


}
