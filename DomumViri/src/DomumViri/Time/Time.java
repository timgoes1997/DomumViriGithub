/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.Time;

import java.io.Serializable;

/**
 *
 * @author Tim
 */
public class Time implements Serializable {

    public static final long SECONDINNANOS = 1000000000;

    private TimeStatus status;
    private long startTime;
    private long endTime;
    private long remainingSeconds;

    public Time(long startTime, int seconds, TimeStatus status) {
        this.status = status;
        checkRemaining(startTime, seconds);
    }

    private void checkRemaining(long startTime, int seconds) {
        if (startTime >= (System.nanoTime() - (5 * SECONDINNANOS)) && startTime <= (System.nanoTime() + (5 * SECONDINNANOS))) {
            System.out.println("Time remains");
            this.startTime = startTime;
            long remainingSeconds = seconds;
            this.endTime = startTime + (remainingSeconds * SECONDINNANOS);
        } else {
            this.startTime = System.nanoTime();
            long remainingSeconds = seconds;
            this.endTime = startTime + (remainingSeconds * SECONDINNANOS);
        }
    }

    public int getRemainingTime(long currentTime) {
        long remainingTime = endTime - currentTime;
        long remainingSeconds = remainingTime / SECONDINNANOS;
        return (int) remainingSeconds;
    }

    public int getGameTime(long currentTime) {
        long ingameTime = currentTime - startTime;
        long ingameTimeInSeconds = ingameTime / SECONDINNANOS;
        return (int) ingameTimeInSeconds;
    }

    public TimeStatus getStatus() {
        return status;
    }

    public void setGameStatus(long startTime, int seconds, TimeStatus status) {
        this.status = status;
        this.startTime = startTime;
        this.endTime = (startTime + (seconds * SECONDINNANOS));
    }
}
