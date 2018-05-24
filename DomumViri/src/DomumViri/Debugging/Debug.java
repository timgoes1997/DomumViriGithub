/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.Debugging;

import javafx.scene.paint.Color;

/**
 *
 * @author Tim
 */
public class Debug {
    
    private static String[] logs;
    public static final int LOGLENGTH = 10;
    public static final Color color = Color.ORANGE;
    
    private Debug(){ 
    }
    
    public static String[] getLog(){
        return logs;
    }
    
    public static void log(String log){
        System.out.println(log);
        if(logs == null){
            logs = new String[LOGLENGTH];
        }
        
        String[] newLogs = new String[LOGLENGTH];
        newLogs[0] =  log;
        int maxMessage = logs.length-1;
        for(int i = 0; i < maxMessage; i++){
            if(logs[i] != null){
                newLogs[i + 1] = logs[i];
            }
        }
        
        logs = newLogs;
    }
}
