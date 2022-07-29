package org.example.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class RateLimiter {
    private final int PERMITS_PER_SECOND;
    private final Semaphore semaphore;
    private final ExecutorService executorService;
    private boolean running=true;


    private RateLimiter(int permitsPerSecond) {

        PERMITS_PER_SECOND = permitsPerSecond;
        semaphore = new Semaphore(PERMITS_PER_SECOND);
        executorService = Executors.newSingleThreadExecutor();

        Runnable runnable = () -> {
            while (running) {
                try {
                    Thread.sleep(1000);
                    semaphore.release(PERMITS_PER_SECOND - semaphore.drainPermits());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        executorService.submit(runnable);
    }

    public void stopRateLimiter(){
        running=false;
        try{
            executorService.shutdown();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    public static RateLimiter create(int permitsPerSecond) {
        return new RateLimiter(permitsPerSecond);
    }

    /**
     * If 'count' number of permits are available, claim them.
     * Else, wait.
     */
    public void acquire(int count) {
        // TODO
        try {
            semaphore.acquire(count);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * If 1 permit is available, claim it.
     * Else, wait.
     */
    public void acquire() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}