/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MoveController;

import DataModel.Game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bast
 */
public class MotorController {
    
    private static final String motor_controller_bin = "/Users/Bast/Documents/_CODE/motorstepper/dummy_move";
    
    static public void run(List<Path> path_list){
        String cmd = new String();
        for (Path path : path_list) {
            cmd = "";
            int size = path.positions.size();
            for(int i=0; i<size-1;i++){
                cmd += path.positions.get(i).toString();
                cmd += " ";
                if(i==0) cmd += "x x ";
            }
            cmd += "y y ";
            //System.out.println("Sending cmd : "+cmd);
            run(cmd);
        }
        
    }
    
    static private void run(String cmd){
        ProcessBuilder builder = new ProcessBuilder(motor_controller_bin, cmd);
        
        try {
            Process p = builder.start();
            InputStream stdout = p.getInputStream();
            InputStream stderr = p.getErrorStream();
            BufferedReader reader = new BufferedReader( new InputStreamReader( stdout ) );
            
            for ( String line = null; ((line = reader.readLine()) != null); ) {
                // TODO: Do something with the output, maybe.
                System.out.println(line);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MotorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        String fen = Game.START_FEN;
        
        List<Path> lpath = new ArrayList<>();
        
        
        Path p = Dijkstra.getShortestPath(new Position("c6"), new Position("a1"), fen);
        lpath.add(p);
        p = Dijkstra.getShortestPath(new Position("a2"), new Position("a4"), fen);
        lpath.add(p);
        run(lpath);
    }
}
