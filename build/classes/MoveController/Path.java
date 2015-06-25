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
    
    public void streamline(){
        double b_dx, b_dy, n_dx, n_dy;
        
        
        //Compute vect
            // foreach new vertex
                //compute new vect
                //if new vect eq old vect
                    //remove vertex
                // else old vect = new vect
        
        int size = this.positions.size();
        for(int i=1; i<positions.size()-1;i++){
            n_dx = positions.get(i+1).x-positions.get(i).x;
            n_dy = positions.get(i+1).y-positions.get(i).y;
            
            b_dx = positions.get(i).x-positions.get(i-1).x;
            b_dy = positions.get(i).y-positions.get(i-1).y;
            //System.out.println("Old : "+b_dx+" "+b_dy+" / New : "+n_dx+" "+n_dy);
            if(n_dx/n_dy == b_dx/b_dy){
                System.out.println("Skipping piece step at "+this.positions.get(i).toString());
                this.positions.remove(i);
                
                i--;
            }
        }
    }
}
