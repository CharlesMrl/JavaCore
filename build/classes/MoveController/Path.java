/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MoveController;

import java.util.ArrayList;
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
        for(int i=0; i<size-1;i++){
            lenght = lenght + positions.get(i).distanceTo(positions.get(i+1));
        }
    }
    
    public Path(Position p1, Position p2){
        positions = new ArrayList<Position>();
        positions.add(p1);
        positions.add(p2);
        lenght = p1.distanceTo(p2);
    }
    
    @Override
    public String toString(){
        String o = new String();
        
        for(Position pos : positions){
            o = o + pos.toString() + " ";
        }
        
        return o;
    }
}
