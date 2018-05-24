/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.VoicechatServer;


/**
 *
 * @author duong
 */
public class Server_voice {

    public static boolean calling = false;
    public static server server;
    
    public void startVoiceServer() {
        server = new server();
        server.init_audio();
    }
    
    public void stopVoiceServer() {
        server.ExecuteInterrupt();
    }
}
