package wechesspresso;

public class PathFinder {
    String oldFen;
    String newFen;
    
    public PathFinder(){
        
    }
        
    public String getOldFen(){
        return this.oldFen;
    }
    
    public String getNewFen(){
        return this.newFen;
    }
    
    public void setOldFen(String fen){
        this.oldFen = fen;
    }
    
    public void setNewFen(String fen){
        this.newFen = fen;
    }
    
    public class Piece {
        char name;
        int source;
        int dest;
        
        public Piece(){
            this.name = 'O';
            this.source = 0;
            this.dest = 0;
        }
        
        public Piece(char n, int s, int d){
            this.name = n;
            this.source = s;
            this.dest = d;
        }
    }
    
    public class Board {
        
    }
    
}
