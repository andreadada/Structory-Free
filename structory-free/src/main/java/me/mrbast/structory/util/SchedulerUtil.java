package me.mrbast.structory.util;

import me.mrbast.structory.Structory;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class SchedulerUtil {

    public static Structory PLUGIN;
    public static AsyncExecutor asyncExecutor;


    public static void init(){
        PLUGIN = Structory.getPlugin(Structory.class);
        asyncExecutor = new AsyncExecutor();
    }

    public static AsyncExecutor getAsyncExecutor() {
        return asyncExecutor;
    }

    public static BukkitTask sync(Runnable runnable) {
        return PLUGIN.getServer().getScheduler().runTask(PLUGIN, runnable);
    }

    public static Future<?> async(Runnable runnable) {
        return asyncExecutor.async(runnable);
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void asyncThenSync(Runnable r1, Runnable r2) {
        asyncExecutor.asyncThenSync(r1, r2);
    }

    public static void safe(Runnable o) {
        PLUGIN.getServer().getScheduler().runTask(PLUGIN, o);
    }

    public static void waitTimedTickThenSync(String name, long time, TimeUnit timeUnit, Runnable r) {
        asyncExecutor.scheduleTask(name, () -> sync(r), timeUnit.toMillis(time), timeUnit);
    }

    public static void waitTimedTickThenSync(String name, long time, Runnable r) {
        asyncExecutor.scheduleTask(name, () -> sync(r), time, TimeUnit.MILLISECONDS);
    }

    public static void waitTimedTickThenSync(long time, TimeUnit timeUnit, Runnable r) {
        asyncExecutor.scheduleTask("waitTimedTickThenSync", () -> sync(r), timeUnit.toMillis(time), TimeUnit.MILLISECONDS);
    }

    public static void waitTickThenSync(long time, Runnable r) {
        asyncExecutor.scheduleTask("waitTickThenSync", () -> sync(r), time, TimeUnit.MILLISECONDS);
    }
    public static void syncThenAsync(Runnable sync, Runnable async) {
        sync.run();
        CompletableFuture.runAsync(async);
    }

    public static AsyncExecutor.TaskChain createChain() {
        return new AsyncExecutor.TaskChain(asyncExecutor);
    }

    public static void scheduleTask(String name, Runnable task, long delay, TimeUnit unit) {
        asyncExecutor.scheduleTask(name, task, delay, unit);
    }

    public static void scheduleRepeatingTask(String name, Runnable task, long initialDelay, long period, TimeUnit unit) {
        asyncExecutor.scheduleRepeatingTask(name, task, initialDelay, period, unit);
    }

    public static void cancelTask(String name) {
        asyncExecutor.cancelTask(name);
    }

    public static void shutdown() {
        asyncExecutor.shutdown();
    }

    public static void onEvery(String name, Runnable task, TimeUnit unit,  long period) {

        asyncExecutor.onEvery(name, task, period, unit);

    }

    public static void bukkitSync(Runnable runnable) {
        Bukkit.getServer().getScheduler().runTask(PLUGIN, runnable);
    }

    public static class AsyncExecutor {
        private int CORE_POOL_SIZE = 1;
        private int MAX_POOL_SIZE = 4;
        private long KEEP_ALIVE_TIME = 60L;

        private final List<Runnable> onInit = new ArrayList<>();

        private ScheduledFuture<?> onEvery;
        private final Map<String, Callable> onEveryTasks = new ConcurrentHashMap<>();
        private final Map<String, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

        private ScheduledThreadPoolExecutor executor;

        public void init(){
            if(executor != null){
                shutdown();
            }
            executor = new ScheduledThreadPoolExecutor(
                    MAX_POOL_SIZE,
                    new ThreadPoolExecutor.CallerRunsPolicy()
            );
            executor.setCorePoolSize(CORE_POOL_SIZE);
            executor.setKeepAliveTime(KEEP_ALIVE_TIME, TimeUnit.SECONDS);
            onInit.forEach(Runnable::run);
            onInit.clear();
        }



        public AsyncExecutor() {

        }

        public void addOnInit(Runnable task) {
            this.onInit.add(task);
        }

        public void scheduleRepeatingTask(String name, Runnable task, long initialDelay, long period, TimeUnit unit) {
            if(executor == null){
                addOnInit(()->{
                    ScheduledFuture<?> future = executor.scheduleAtFixedRate(task, initialDelay, period, unit);
                    tasks.put(name, future);
                });
                return;
            }
            ScheduledFuture<?> future = executor.scheduleAtFixedRate(task, initialDelay, period, unit);
            tasks.put(name, future);

        }

        public void setOnEvery(int initialDelay, int checkPeriod, TimeUnit unit){
            if(onEvery != null) {
                onEvery.cancel(true);
            }
            if(executor == null){addOnInit(()->executor.scheduleAtFixedRate(this::onEvery, initialDelay, 500, TimeUnit.MILLISECONDS));return;}
            onEvery =  executor.scheduleAtFixedRate(this::onEvery, initialDelay, checkPeriod, unit);
        }

        public void onEvery(){
            onEveryTasks.values().forEach(Callable::executeIfReady);
        }

        public void onEvery(String name, Runnable task, long period, TimeUnit unit) {
            onEveryTasks.put(name, new Callable(name, task,  period, unit));
        }


        public Future<?> async(Runnable runnable) {
            return executor.submit(runnable);
        }

        public void shutdown() {
            if(executor == null) return;
            executor.shutdownNow();
            executor.shutdown();

        }

        public void asyncThenSync(Runnable r1, Runnable r2) {
            executor.submit(() -> {
                try {
                    r1.run();
                } catch (Exception ignored) {}
                safe(r2);
            });
        }

        public void scheduleTask(String name, Runnable task, long delay, TimeUnit unit) {
            executor.schedule(task, delay, unit);
        }

        public boolean cancelTask(String name) {
            ScheduledFuture<?> future = tasks.remove(name);
            if (future != null) {
                return future.cancel(false);
            }
            return false;
        }

        public void setCorePoolSize(int CORE_POOL_SIZE) {
            this.CORE_POOL_SIZE = CORE_POOL_SIZE;
        }


        public void setKeepAliveTime(long KEEP_ALIVE_TIME) {
            this.KEEP_ALIVE_TIME = KEEP_ALIVE_TIME;
        }

        public void setMaxPoolSize(int MAX_POOL_SIZE) {
            this.MAX_POOL_SIZE = MAX_POOL_SIZE;
        }

        public int getCorePoolSize() {
            return CORE_POOL_SIZE;
        }

        public int getMaxPoolSize() {
            return MAX_POOL_SIZE;
        }

        public long getKeepAliveTime() {
            return KEEP_ALIVE_TIME;
        }

        public ScheduledThreadPoolExecutor getExecutor() {
            return executor;
        }

        public Map<String, Callable> getOnEveryTasks() {
            return onEveryTasks;
        }

        public static class TaskChain {
            private final AsyncExecutor executor;
            private long cumulativeDelay = 0;

            public TaskChain(AsyncExecutor executor) {
                this.executor = executor;
            }

            public TaskChain then(String name, Runnable task, long delay, TimeUnit unit) {
                cumulativeDelay += unit.toMillis(delay);
                executor.scheduleTask(name, task, cumulativeDelay, java.util.concurrent.TimeUnit.MILLISECONDS);
                return this;
            }

            public TaskChain then(String name, Runnable task) {
                executor.scheduleTask(name, task, cumulativeDelay, TimeUnit.MILLISECONDS);
                return this;
            }
        }

        private static class Callable{

            private final Runnable task;
            private final long wait;
            private final String name;
            private long nextTimeCall = 0;
            private boolean enabled = true;


            public Callable(String name, Runnable task, long period, TimeUnit unit) {
                this.task = task;
                this.name = name;
                this.wait = unit.toMillis(period);
            }

            public boolean isReady(){
                return System.currentTimeMillis() > nextTimeCall;
            }
            public void executeIfReady(){
                if(!enabled || !isReady()) return;
                nextTimeCall = System.currentTimeMillis() + wait;
                task.run();
            }

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }
    }
}