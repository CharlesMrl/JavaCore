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
    
    public ArrayList<Position> getPathList(Move move)
    {
        ArrayList<Position> list = new ArrayList();
        
        //Ranger la piece capturee en premier
        if(move.get("type")=="capture")
        {
            
        }
        
        //Deplacer la piece qui effectue le deplacement
        if(!move.get("piece").equals("cavalier"))
        {
            
        }
        else{ // generation chemin special du cavalier
            
        }
        
        
        return list;
    }
}

