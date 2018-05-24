/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.VoicechatServer;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;

/**
 *
 * @author duong
 */
public final class server {

    public int port = 8888;
    private player_thread p;

    public static AudioFormat getaudioformat() {
        float sampleRate = 8000.0F;
        int sampleSizeInbits = 16;
        int channel = 2;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInbits, channel, signed, bigEndian);
    }
    public SourceDataLine audio_out;

    public server() {
    }

    public void init_audio() {
        try {
            System.out.println("voice server started");
            AudioFormat format = getaudioformat();
            DataLine.Info info_out = new DataLine.Info(SourceDataLine.class, format);
            if (!AudioSystem.isLineSupported(info_out)) {
                System.out.println("not suport");
                System.exit(0);
            }
            audio_out = (SourceDataLine) AudioSystem.getLine(info_out);
            audio_out.open(format);
            audio_out.start();
            p = new player_thread();
            p.din = new DatagramSocket(port);
            p.audio_out = audio_out;
            Server_voice.calling = true;
            p.start();
            //p.interrupt();
            btn_start = new JButton("start");
            btn_start.setEnabled(false);
        } catch (LineUnavailableException | SocketException ex) {
            System.out.println("voice server failed");
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ExecuteInterrupt() {
        p.interrupt();
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btn_start;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration                   
}
