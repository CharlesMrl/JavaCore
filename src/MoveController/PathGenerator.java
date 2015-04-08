/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MoveController;

import DataModel.Move;
import java.util.ArrayList;

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
        if(move.get("type").equals("capture"))
        {
            //1er Path : ranger la piece capturée avec Dijkstra
            Position depart = new Position(move.get("pos2"));
            
            /*
            TODO : Determiner la Position ou on range la piece capturée
            */
            
            Position pos_rangement = new Position(0.5,0.5);
            Path capt_path = Dijkstra.getShortestPath(depart,pos_rangement, fen);
            list.add(capt_path);
        }
        
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
        
        return list;
    }
}

