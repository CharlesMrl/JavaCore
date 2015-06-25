/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MoveController;

import DataModel.Game;
import static MoveController.Dijkstra.correctPath;
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
    
    //private static final String motor_controller_bin = "/Users/Bast/Documents/_CODE/motorstepper/dummy_move";
    private static final String motor_controller_bin = "/home/pi/Desktop/wechess/weChess_Stepper/move";
    
    static public void run(List<Path> path_list){
        String cmd = new String();
        String home = "0.5 0.5";
        for (Path path : path_list) {
            cmd = "";
            int size = path.positions.size();
            for(int i=0; i<size;i++){
                cmd += path.positions.get(i).toString();
                cmd += " ";
                if(i==0) cmd += "x x ";
            }
            cmd += "y y "+home;
            System.out.println("Sending cmd : "+cmd);
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
        //String fen = Game.START_FEN;
        String[] fen = {"r1q1kn2/1p4p1/8/P7/2b5/p1B1P3/5P2/1NQ1K2R w Kq - 0 0",
                        "r1q1kn2/1p4pR/8/P7/2b5/p1B1P3/5P2/1NQ1K3 w Kq - 0 0",
                        "r1q1kn2/1p5R/8/P5p1/2b5/p1B1P3/5P2/1NQ1K3 w Kq - 0 0",
                        "r1q1kn2/1p5R/8/P5p1/2b5/p1B1PP2/8/1NQ1K3 w Kq - 0 0",
                        "r1q1k3/1p5R/4n3/P5p1/2b5/p1B1PP2/8/1NQ1K3 w Kq - 0 0",
                        "r1q1k3/1p5R/4n3/P5p1/2b5/p1B1PP2/3K4/1NQ5 w Kq - 0 0",
                        "r3k3/1p5R/4n3/P1q3p1/2b5/p1B1PP2/3K4/1NQ5 w Kq - 0 0",
                        "r3k3/1p5R/4n3/P1q3p1/2b5/Q1B1PP2/3K4/1N6 w Kq - 0 0"
        };
        /*
        1) h1 h7 : r1q1kn2/1p4pR/8/P7/2b5/p1B1P3/5P2/1NQ1K3 w Kq - 0 0
        2) g7 g5 : r1q1kn2/1p5R/8/P5p1/2b5/p1B1P3/5P2/1NQ1K3 w Kq - 0 0
        3) g2 g3 : r1q1kn2/1p5R/8/P5p1/2b5/p1B1PP2/8/1NQ1K3 w Kq - 0 0
        4) g8 f6 : r1q1k3/1p5R/4n3/P5p1/2b5/p1B1PP2/8/1NQ1K3 w Kq - 0 0
        5) e1 d2 : r1q1k3/1p5R/4n3/P5p1/2b5/p1B1PP2/3K4/1NQ5 w Kq - 0 0
        6) c8 c5 : r3k3/1p5R/4n3/P1q3p1/2b5/p1B1PP2/3K4/1NQ5 w Kq - 0 0
        7) c1 a3 : r3k3/1p5R/4n3/P1q3p1/2b5/Q1B1PP2/3K4/1N6 w Kq - 0 0
        */
        
        List<Path> lpath = new ArrayList<>();
        
        Path p = Dijkstra.getShortestPath(new Position("b1"), new Position("e4"), Game.START_FEN);
        lpath.add(p);
        //System.out.println(p.positions.toString());
        /*
        p = Dijkstra.getShortestPath(new Position("g7"), new Position("g5"), fen[1]);
        lpath.add(p);
        p = Dijkstra.getShortestPath(new Position("g2"), new Position("g3"), fen[2]);
        lpath.add(p);
        p = Dijkstra.getShortestPath(new Position("g8"), new Position("f6"), fen[3]);
        lpath.add(p);
        p = Dijkstra.getShortestPath(new Position("e1"), new Position("d2"), fen[4]);
        lpath.add(p);
        p = Dijkstra.getShortestPath(new Position("c8"), new Position("c5"), fen[5]);
        lpath.add(p);
        p = Dijkstra.getShortestPath(new Position("c1"), new Position("a3"), fen[6]);
        lpath.add(p);
                */
        //p = Dijkstra.getShortestPath(new Position("a2"), new Position("a4"), fen);
        //lpath.add(p);
        run(lpath);
    }
}
