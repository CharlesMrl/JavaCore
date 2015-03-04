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

