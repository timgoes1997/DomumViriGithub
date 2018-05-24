/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.VoicechatClient;

/**
 *
 * @author duong
 */
public class Client_voice {

    public static boolean calling = false;
    public static client client;
    
    public void startVoiceClient(String ip) {
        client = new client();
        client.init_audio(ip);
    }
    
    public void stopVoiceClient() {
        client.ExecuteInterrupt();
    }
    
}
