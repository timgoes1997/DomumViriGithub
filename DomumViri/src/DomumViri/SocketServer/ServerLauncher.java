/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.SocketServer;

/**
 *
 * @author Tim
 */
public class ServerLauncher {
    
    private static SocketServer server;
    
    private ServerLauncher(){
    }
    
    public static void main(String[] args) {
        if (args.length != 1) {
            server = new SocketServer();
        } else {
            int portNumber = Integer.parseInt(args[0]);
            if (portNumber > 49151 || portNumber < 1024) {
                System.out.println("You may only use a port between the 1024-49151 range");
                server = new SocketServer();
            }
            server = new SocketServer(portNumber);
        }
    }
}
