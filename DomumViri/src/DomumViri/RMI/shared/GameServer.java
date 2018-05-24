/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.RMI.shared;

import java.io.Serializable;
import java.net.InetAddress;

/**
 *
 * @author Jeroen Roovers
 */
public class GameServer implements Serializable {

    private String name;
    private String ipaddress;
    private String port;
    private InetAddress inetaddress;

    public GameServer() {
    }

    public GameServer(String ip, String port, String name) {
        this.ipaddress = ip;
        this.port = port;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPort() {
        return port;
    }

    public String getIPaddress() {
        return ipaddress;
    }

    @Override
    public String toString() {
        return getName();
    }

}
