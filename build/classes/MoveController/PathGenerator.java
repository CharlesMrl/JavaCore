/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MoveController;

import DataModel.Move;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import wechesspresso.*;

/**
 *
 * @author Bastien
 */
public class PathGenerator {
    
    static public ArrayList<Path> getPathList(Move move, String fen)
    {
        ArrayList<Path> list = new ArrayList();
        
        //Si ROQUE / GRAND ROQUE
        
        //Si En-Passant
        
        //Si Promotion
        
        //Si capture, ranger la piece capturee en premier
        if(WeChesspresso.isMoveCapturing(fen, move.get("pos1"), move.get("pos2")))
        {
            
            try {
                //1er Path : ranger la piece capturée avec Dijkstra
                Position depart = new Position(move.get("pos2"));
                
                /*
                TODO : Determiner la Position ou on range la piece capturée
                */
                /*
                Position pos_rangement = new Position(0.5,0.5);
                Path capt_path = Dijkstra.getShortestPath(depart,pos_rangement, fen);
                System.out.print("[PathGenerator] Captured piece "+move.get("pos2")+" -> 0.5 0.5 - "+capt_path);
                capt_path.streamline();
                System.out.println("    Streamlined : "+capt_path);
                list.add(capt_path);
                */
                
                //BASTIEN : solution bug capture
                Position pos_depart = new Position(move.get("pos2"));
                Position pos_rangement = new Position(0.5,0.5);
                List<Integer> occupied = Dijkstra.getOccupied(fen);
                
                ///// SOLUTION //////
                //occupied.remove(######);
                /////////////////////
                
                List<Position> pos_list = Dijkstra.getShortestPath(pos_depart.toInteger(), pos_rangement.toInteger(), occupied);
                ArrayList<ArrayList<Character>> fen_tab = Dijkstra.getTabFromFen(fen);
                Position capture_pos = new Position(move.get("pos2"));
                
                //fen_tab.get(capture_pos.toInteger()%12).
            } catch (Exception ex) {
                Logger.getLogger(PathGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        /*
        //Deplacer la piece qui effectue le deplacement/capture
        if(!move.get("piece").equals("cavalier"))
        {
            //generation du Path
            Position depart = new Position(move.get("pos1"));
            Position fin = new Position(move.get("pos2"));
            Path path = new Path(depart,fin);
            list.add(path);
        }
        else{ // generation chemin special du cavalier
            //Appel a Dijkstra et generation du Path du cavalier
            Position depart = new Position(move.get("pos1"));
            Position fin = new Position(move.get("pos2"));
            Path knight_path = Dijkstra.getShortestPath(depart,fin, fen);
            list.add(knight_path);
        }
                */
        Position depart = new Position(move.get("pos1"));
        Position fin = new Position(move.get("pos2"));
        Path piece_path = Dijkstra.getShortestPath(depart,fin, fen);
            //System.out.println(knight_path.toString());
         System.out.print("[PathGenerator] Moving piece "+move.get("pos1")+" -> "+move.get("pos2")+" - "+piece_path);
         piece_path.streamline();
         System.out.println("    Streamlined : "+piece_path);
         list.add(piece_path);
        
        return list;
    }
}

