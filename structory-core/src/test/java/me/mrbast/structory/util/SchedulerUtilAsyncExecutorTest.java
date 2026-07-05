package me.mrbast.structory.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerUtilAsyncExecutorTest {
    private SchedulerUtil.AsyncExecutor executor;

    @AfterEach
    void tearDown() {
        if (executor != null) executor.shutdown();
    }

    @Test
    void queuesScheduledWorkUntilInitialization() throws InterruptedException {
        executor = new SchedulerUtil.AsyncExecutor();
        CountDownLatch completed = new CountDownLatch(1);

        executor.scheduleTask("queued", completed::countDown, 0, TimeUnit.MILLISECONDS);
        assertEquals(1, completed.getCount());

        executor.init();
        assertTrue(completed.await(2, TimeUnit.SECONDS));
    }

    @Test
    void replacingANamedTaskCancelsThePreviousOne() throws InterruptedException {
        executor = new SchedulerUtil.AsyncExecutor();
        executor.init();
        CountDownLatch oldTask = new CountDownLatch(1);
        CountDownLatch replacement = new CountDownLatch(1);

        executor.scheduleTask("same-name", oldTask::countDown, 300, TimeUnit.MILLISECONDS);
        executor.scheduleTask("same-name", replacement::countDown, 0, TimeUnit.MILLISECONDS);

        assertTrue(replacement.await(2, TimeUnit.SECONDS));
        assertFalse(oldTask.await(500, TimeUnit.MILLISECONDS));
    }
}
