/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.SocketServer;

import DomumViri.Level.MapInfo;
import DomumViri.Level.MapName;
import DomumViri.Main.GameMode;
import DomumViri.RMI.client.*;
import DomumViri.RMI.shared.*;
import DomumViri.Time.Time;
import DomumViri.Time.TimeStatus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tim
 */
public class SocketServer {

    private boolean listening;
    private boolean stopped;
    private boolean local;
    private ExecutorService pool;
    private ServerSocket serverSocket;
    private ServerWorld serverWorld;
    private RMIclient rmiclient;
    private GameMode mode;
    private String externalIP;

    private int playerCap;
    private int waitingTime;
    private int warmupTime;
    private int gameTime;
    private int finishedTime;

    public SocketServer(int portNumber) {
        this.playerCap = 4;
        this.waitingTime = 20; //100
        this.warmupTime = 15; //30
        this.gameTime = 300; //500
        this.finishedTime = 10; //20
        this.listening = true;
        this.rmiclient = new RMIclient(RMIclient.HOSTNAME, RMIclient.PORTNUMBER);
        serverWorld = new ServerWorld(MapName.Default, MapInfo.Default, new Time(System.nanoTime(), waitingTime, TimeStatus.Waiting));
        startServer(portNumber);
    }

    public SocketServer() {
        this.playerCap = 4;
        this.waitingTime = 20; //100
        this.warmupTime = 15; //30
        this.gameTime = 300; //500
        this.finishedTime = 10; //20
        this.listening = true;
        this.rmiclient = new RMIclient(RMIclient.HOSTNAME, RMIclient.PORTNUMBER);
        serverWorld = new ServerWorld(MapName.Default, MapInfo.Default, new Time(System.nanoTime(), waitingTime, TimeStatus.Waiting));
        startServer(1337);
    }

    public SocketServer(GameMode mode) {
        this.playerCap = 4;
        this.waitingTime = 20; //100
        this.warmupTime = 15; //30
        this.gameTime = 300; //500
        this.finishedTime = 10; //20
        this.mode = mode;
        this.listening = true;
        this.rmiclient = new RMIclient(RMIclient.HOSTNAME, RMIclient.PORTNUMBER);
        serverWorld = new ServerWorld(MapName.Default, MapInfo.Default, new Time(System.nanoTime(), waitingTime, TimeStatus.Waiting));
        startServer(1337);
    }

    public GameMode getMode() {
        return mode;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getWarmupTime() {
        return warmupTime;
    }

    public int getGameTime() {
        return gameTime;
    }

    public int getFinishedTime() {
        return finishedTime;
    }

    public int getPlayerCap() {
        return playerCap;
    }

    public ServerWorld getServerWorld() {
        return serverWorld;
    }

    public boolean isStopped() {
        return stopped;
    }

    public boolean isListening() {
        return listening;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public void setListening(boolean listening) {
        this.listening = this.listening;
    }

    private void startServer(int portNumber) {
        this.stopped = false;
        pool = Executors.newCachedThreadPool();
        try {
            InetAddress ip = InetAddress.getLocalHost();
            serverSocket = new ServerSocket(portNumber, 10, ip);
            System.out.println("Server listening on: " + serverSocket.getLocalSocketAddress());
            if (mode == null) {
                setServerGameMode();
            }
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            externalIP = in.readLine(); //you get the IP as a String
            System.out.println(externalIP);
            rmiclient.addServer("mooie server naam", externalIP, portNumber);
            ServerTimeThread stt = new ServerTimeThread(this);
            pool.submit(stt);
            SocketInputThread sit = new SocketInputThread(this);
            pool.submit(sit);
            while (listening && !stopped) {
                SocketServerThread sst = new SocketServerThread(this, serverSocket.accept());
                pool.submit(sst);
            }
        } catch (Exception ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
            //System.exit(-1);
        } finally {
            try {
                pool.shutdownNow();
                rmiclient.deleteServer(externalIP);
                serverSocket.close();
                this.finalize();
            } catch (IOException ex) {
                Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Throwable ex) {
                Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void stopServer() {
        stopped = true; 
        rmiclient.deleteServer(externalIP);
        try {
            this.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0); 
    }

    private void setServerGameMode() {
        System.out.println("Set the servers gamemode, press 0 for FFA or 1 for Teams, default is FFA");
        Scanner reader = new Scanner(System.in);
        int input = 13;
        if (reader.hasNextInt()) {
            input = reader.nextInt();
        } else {
            setGameModeToFFA();
        }

        switch (input) {
            case 0:
                setGameModeToFFA();
                break;
            case 1:
                setGameModeToTeams();
                break;
            default:
                setGameModeToFFA();
                break;
        }
    }

    private void setGameModeToFFA() {
        this.mode = GameMode.MPFFA;
        System.out.println("Setting GameMode To FFA");
    }

    private void setGameModeToTeams() {
        this.mode = GameMode.MPTeams;
        System.out.println("Setting GameMode To Teams");
    }
}
