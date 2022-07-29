package org.example.service;

import java.util.concurrent.*;

public class RateLimiterThomas {

    private final int PERMITS_PER_SECOND;
    private ExecutorService execServ;
    private Semaphore permits;
    private boolean running = true;

    public static RateLimiterThomas create(int permitsPerSecond) {
        return new RateLimiterThomas(permitsPerSecond);
    }

    private RateLimiterThomas(int permitsPerSecond) {

        PERMITS_PER_SECOND = permitsPerSecond;

        //create semaphore with permitsPerSecond amount of permits
        permits = new Semaphore(permitsPerSecond);

        running = true;
        // Create a thread which releases all permits once every second
        execServ = Executors.newSingleThreadExecutor();

        execServ.submit(() -> {
            // While the program is running
            while (true) {
                // Try to sleep for a second
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // re-allocate permits.
                int permitsToRelease = PERMITS_PER_SECOND - permits.drainPermits();
                permits.release(permitsToRelease);
            }
        }, "Permit Clearing Thread");
    }

    public void stopRateLimiter(){
        running = false;
        // Try to let the Future object finish, by using get()
        //NOT, swapped it for an exec service
        try {
            // permitsCleared.get()
            execServ.shutdown();
            System.out.println("Permit Clearing Thread has been killed");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * If 1 permit is available, claim it.
     * Else, wait.
     */
    public void acquire() {
        //use semaphore acquire which will block until a permit is available.
        try {
            permits.acquire();
        } catch (InterruptedException e) {
            System.out.println("RateLimiter got interrupted while trying to acquire a permit!!!!!");
            throw new RuntimeException(e);
        }
    }

    /**
     * If 'count' number of permits are available, claim them.
     * Else, wait.
     */
    public void acquire(int count) {
        // for each permit we need, let's call acquire
        while (count > 0) {
            acquire();
            count--;
        }
    }

}