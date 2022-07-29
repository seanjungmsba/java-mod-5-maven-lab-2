package org.example.service;

import java.util.Timer;
import java.util.TimerTask;

public class RateLimiterJay {

    private final int PERMITS_PER_SECOND;
    private int occupiedPermits;

    private RateLimiterJay(int permitsPerSecond) {
        PERMITS_PER_SECOND = permitsPerSecond;
        this.occupiedPermits = 0;

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (occupiedPermits > 0) {
                    occupiedPermits--;
                }
            }
        }, 1_000, 1_000 / PERMITS_PER_SECOND);
        // 2nd argument: delay – delay in milliseconds before task is to be executed.
        // 3rd argument: period – time in milliseconds between successive task executions.
            // 1000/10 = 100 milliseconds = 0.1 seconds
            // what does this mean? ==> occupiedPermits that is bigger than 0 is decremented every 0.1 seconds (or 10 times every second)
    }

    public static RateLimiterJay create(int permitsPerSecond) {
        return new RateLimiterJay(permitsPerSecond);
    }

    /**
     * If 'count' number of permits are available, claim them.
     * Else, wait.
     */
    public synchronized void acquire(int count) {
        // TODO
        // if acquire() is trying to claim more permits than it currently has,
        // then delay a thread one second at a time until this condition no longer holds true
        while ( count > PERMITS_PER_SECOND - occupiedPermits ) {
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                System.out.println("ERROR: Can't acquire " + e);
            }
        }
        this.occupiedPermits += count; // finally, update number of occupied permits
    }

    /**
     * If 1 permit is available, claim it.
     * Else, wait.
     */
    public synchronized void acquire() {
        // TODO
        acquire(1);
    }
}
