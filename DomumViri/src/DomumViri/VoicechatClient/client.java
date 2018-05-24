/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.VoicechatClient;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author walter
 */
public final class client {
    public int port_server = 8888;
    public String add_server = "127.0.0.1";
    private recorder_thread r;
    
    public static AudioFormat getaudioformat(){
        float sampleRate = 8000.0F;
        int sampleSizeInbits = 16;
        int channel = 2;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInbits, channel, signed, bigEndian);
    }
    
    TargetDataLine audio_in;
    
    public client() {
    }

    public void init_audio(String ip){
        add_server = ip;
        try {
            System.out.println("voice client started");
            AudioFormat format = getaudioformat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if(!AudioSystem.isLineSupported(info)){
                System.out.println("not suport");
                System.exit(0);
            }
            audio_in = (TargetDataLine) AudioSystem.getLine(info);
            audio_in.open(format);
            audio_in.start();
            r = new recorder_thread();
            InetAddress inet = InetAddress.getByName(add_server);
            r.audio_in = audio_in;
            r.dout = new DatagramSocket();
            r.server_ip = inet;
            r.server_port = port_server;
            Client_voice.calling = true;
            r.start();
        } catch (LineUnavailableException | UnknownHostException | SocketException ex) {
            System.out.println("voice client failed");
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ExecuteInterrupt(){
        r.interrupt();
    }
}
