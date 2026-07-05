package me.mrbast.structory.util;


import me.mrbast.structory.Structory;

import java.io.File;
import java.io.IOException;

public class FileUtil {



    @SuppressWarnings("all")
    public static File prepare(String directory, boolean create){


        File file = new File(Structory.getPlugin(Structory.class).getDataFolder(), directory);
        if(!file.exists()) {
            if (create) {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                    return file;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return file;
        }

        return file;

    }

}
