package MoveController;

/**
 *
 * @author Bastien
 */
public class Position {
    
    public static final float SQUARE_SIDE_MM = 65;
    double x;
    double y;
    
    public Position(double a, double b){
        this.x=a;
        this.y=b;
    }
    
    @Override
    public String toString(){
        return new String(x+","+y);
    }
    
    public Position(int pos){
        this.x=pos/12;
        this.y=pos%12;
    }
    
    public int toInteger() throws NotCenterPositionException{
        if(!this.isCenterOfSquare()) throw new NotCenterPositionException();
        else return (int)x*12+(int)y;
    }
    
    // Les positions aux milieux des cases ont des coordonnées non-entières, ex. (2,5 2,5)
    public boolean isCenterOfSquare(){
        if(x != (int)x && y != (int)y) return true;
        return false;
    }

    private static class NotCenterPositionException extends Exception {

        public NotCenterPositionException() {
        }
    }
}
