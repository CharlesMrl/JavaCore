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
        
        //Si capture, ranger la piece capturee en premier
        if(move.get("type").equals("capture"))
        {
            //1er Path : ranger la piece capturée avec Dijkstra
            Position depart = new Position(move.get("pos2"));
            
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
            Path knight_path = Dijkstra.getShortestPath(null, null, fen);
            list.add(knight_path);
        }
        
        return list;
    }
}

