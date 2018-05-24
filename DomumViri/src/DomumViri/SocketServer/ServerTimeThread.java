/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.SocketServer;

import DomumViri.Main.GameMode;
import static DomumViri.Time.TimeStatus.Finished;
import static DomumViri.Time.TimeStatus.Ingame;
import static DomumViri.Time.TimeStatus.Waiting;
import static DomumViri.Time.TimeStatus.Warmup;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tim
 */
public class ServerTimeThread implements Runnable {

    private static final int SLEEP_TIME = 50;

    private SocketServer ss;

    public ServerTimeThread(SocketServer ss) {
        this.ss = ss;
    }

    @Override
    public void run() {
        try {
            boolean running = true;
            while (running && !ss.isStopped()) {
                Thread.sleep(1000);
                //System.out.println(ss.getServerWorld().getTime().getRemainingTime(System.nanoTime()));
                if (ss.getServerWorld().getTime().getRemainingTime(System.nanoTime()) <= 0) {
                    switch (ss.getServerWorld().getTime().getStatus()) {
                        case Waiting:
                            switchToWarmup();
                            break;
                        case Warmup:
                            switchToIngame();
                            break;
                        case Ingame:
                            switchToFinished();
                            break;
                        case Finished:
                            switchToWaiting();
                            break;
                        default:
                            System.out.println("Unknown time");
                            break;
                    }
                }

            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerTimeThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void switchToWarmup() {
        if (ss.getMode() == GameMode.MPTeams && ss.getServerWorld().getPlayerCount() >= 4) {
            ss.getServerWorld().getTime().setGameStatus(System.nanoTime(), ss.getWarmupTime(), Warmup);
            System.out.println("TIME:WARMUP");
        } else if (ss.getMode() == GameMode.MPFFA && ss.getServerWorld().getPlayerCount() >= 2) {
            ss.getServerWorld().getTime().setGameStatus(System.nanoTime(), ss.getWarmupTime(), Warmup);
            System.out.println("TIME:WARMUP");
        } else {
            switchToWaiting();
        }
    }

    public void switchToIngame() {
        if (ss.getMode() == GameMode.MPTeams && ss.getServerWorld().getPlayerCount() >= 4) {
            ss.getServerWorld().getTime().setGameStatus(System.nanoTime(), ss.getGameTime(), Ingame);
            System.out.println("TIME:INGAME");
        } else if (ss.getMode() == GameMode.MPFFA && ss.getServerWorld().getPlayerCount() >= 2) {
            ss.getServerWorld().getTime().setGameStatus(System.nanoTime(), ss.getGameTime(), Ingame);
            System.out.println("TIME:INGAME");
        } else {
            switchToWaiting();
        }
    }

    public void switchToFinished() {
        ss.getServerWorld().getTime().setGameStatus(System.nanoTime(), ss.getFinishedTime(), Finished);
        System.out.println("TIME:FINISHED");
    }

    public void switchToWaiting() {
        ss.getServerWorld().getTime().setGameStatus(System.nanoTime(), ss.getWaitingTime(), Waiting);
        System.out.println("TIME:WAITING");
    }
}
