/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MoveController;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Bast
 */
public class Path {
    List<Position> positions;
    double lenght;
    
    public Path(List<Position> lpos){
        positions = lpos;
        
        int size = positions.size();
        lenght = 0;
        for(int i=0; i<size;i++){
            lenght = lenght + positions.get(i).distanceTo(positions.get(i+1));
        }
    }
}
