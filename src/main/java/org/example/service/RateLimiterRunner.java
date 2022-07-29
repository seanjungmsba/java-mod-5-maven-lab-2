package org.example.service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.IntStream;

public class RateLimiterRunner {

//    private static final RateLimiter RATE_LIMITER = RateLimiter.create(10); // 10 permits available per second
//    private static final RateLimiterThomas RATE_LIMITER = RateLimiterThomas.create(10); // Thomas's implementation
    private static final RateLimiterJay RATE_LIMITER = RateLimiterJay.create(10); // Jay's implementation
    public static void main(String[] args) throws InterruptedException {
        /*
         * So here's what's going on in the RateLimiterRunner.
         * I have 5 threads that are all "examining" numbers
         * Why? Because I want them to all be very busy and have tons of work to do.
         *
         * HOWEVER, the examineNumber method in this class is using "RateLimiter"
         * RateLimiter has one job: make sure the following lines of code can only be touched x times per second.
         * When RATE_LIMITER.acquire() is called in that method, it permits the thread to proceed if there is an open permit.
         * If there is not an open permit, it forces the thread to wait until there is an open permit.
         *
         * YOUR JOB: You'll need to write the RATE_LIMITER code.
         * TO TEST: Just run this. The output in the console should show roughly ten numbers per second.
         *
         * HINT: Consider using Semaphore and consider using OOP and consider using the other stuff you've learned
         * about threads.
         *
         * GOOD LUCK!
         *
         */
        Thread t1 = new Thread(() -> {
            getWordsToCheck().forEach(RateLimiterRunner::examineNumber);
        }, "t1");
        Thread t2 = new Thread(() -> {
            getWordsToCheck().forEach(RateLimiterRunner::examineNumber);

        }, "t2");
        Thread t3 = new Thread(() -> {
            getWordsToCheck().forEach(RateLimiterRunner::examineNumber);

        }, "t3");
        Thread t4 = new Thread(() -> {
            getWordsToCheck().forEach(RateLimiterRunner::examineNumber);

        }, "t4");
        Thread t5 = new Thread(() -> {
            getWordsToCheck().forEach(RateLimiterRunner::examineNumber);

        }, "t5");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();


    }

    private static void examineNumber(String num) {
        RATE_LIMITER.acquire();
        System.out.println(LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + ": " + num);
    }



    private static List<String> getWordsToCheck() {
        return IntStream.range(-100_000, 1_000).mapToObj(i -> String.valueOf(i)).toList();
    }
}
