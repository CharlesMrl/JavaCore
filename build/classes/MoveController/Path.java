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
    double length;
    
    public Path(List<Position> lpos){
        positions = lpos;
        
        int size = positions.size();
        length = 0;
        for(int i=0; i<size-1;i++){
        	length = length + positions.get(i).distanceTo(positions.get(i+1));
        }
    }
    
    public Path(Position p1, Position p2){
        positions = new ArrayList<Position>();
        positions.add(p1);
        positions.add(p2);
        length = p1.distanceTo(p2);
    }
    
    public Position getPositionati(int i){
    	return this.positions.get(i);
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
