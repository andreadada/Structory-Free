package me.mrbast.structory.util;

/*
public class Debugger {
    enum Type {
        NONE, LOW, HIGH
    }

    public static final Type LOW = Type.LOW;
    public static final Type HIGH = Type.HIGH;

    private static Type debugLevel = Type.NONE;
    private static final String LOG_FILE = "debug.log";
    private static PrintWriter writer = null;

    public static void setDebugLevel(Type level) {
        debugLevel = level;
        if (debugLevel != Type.NONE) {
            try {
                writer = new PrintWriter(new FileWriter(Structory.getPlugin(Structory.class).getDataFolder() + "/" +  LOG_FILE, true), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void log(Type level, String message) {
        if (debugLevel == Type.NONE || level.ordinal() > debugLevel.ordinal()) {
            return;
        }

        executor.submit(() -> {
            LocalDateTime now = LocalDateTime.now();
            String timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            String callingClass = "Unknown";
            if (stackTraceElements.length > 2) {
                callingClass = stackTraceElements[2].getClassName();
            }

            writer.printf("[%s] [%s] %s%n", timestamp, callingClass, message);
            writer.flush();
        });
    }

    public static void close() {
        if (writer != null) {
            writer.close();
        }
        // Shutdown the executor when you're done
        executor.shutdown();
    }

}

 */