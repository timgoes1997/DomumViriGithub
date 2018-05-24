/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.SocketServer;

import java.util.Scanner;

/**
 *
 * @author Tim
 */
public class SocketInputThread implements Runnable {

    private SocketServer ss;

    public SocketInputThread(SocketServer server) {
        this.ss = server;
    }

    @Override
    public void run() {
        while (ss.isListening()) {
            System.out.println("Press 0 to exit the server");
            Scanner reader = new Scanner(System.in);
            int input = 13;
            if (reader.hasNextInt()) {
                input = reader.nextInt();
                switch (input) {
                    case 0:
                        ss.stopServer();
                        break;
                }
            }
        }
    }
}
