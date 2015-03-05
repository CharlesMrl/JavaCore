/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MoveController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bast
 */
public class MotorController {
    
    private static final String motor_controller_bin = "/usr/bin/motorstepper";
    
    static public void run(ArrayList<Path> path_list){
        
    }
    
    static public void run(String cmd){
        ProcessBuilder builder = new ProcessBuilder(motor_controller_bin, cmd);
        try {
            builder.start();
        } catch (IOException ex) {
            Logger.getLogger(MotorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
