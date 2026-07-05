package me.mrbast.structory.format;

import java.util.HashMap;
import java.util.Map;


/***
 * Used to set key:value passed to Formatter
 */
public class Format {

    private Map<String, String> values = new HashMap<>();

    private Format(){



    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public static class FormatArgument{


        private String[] arguments;
        public FormatArgument(String[] parameters) {
            this.arguments = parameters;
        }


        public Format as(String... values){
            Format format = new Format();
            Map<String, String> map = format.getValues();
            for (int i = 0; i < arguments.length; i++) {
                map.put(arguments[i], values[i]);
            }
            return format;

        }
    }

    public static FormatArgument of(String... parameters){
        return new FormatArgument(parameters);
    }

}
