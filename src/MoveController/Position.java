/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MoveController;

import DataModel.Move;
import static java.lang.Math.sqrt;
import java.util.ArrayList;

/**
 *
 * @author Bastien
 */
public class Position{
    
    public static final float SQUARE_SIDE_MM = 65;
    double x;
    double y;
    
    
    public Position(double a, double b){
        this.x=a;
        this.y=b;
    }
    
    public String toString(){
        return new String(x+" "+y);
    }
    
    public Position(int pos){
        this.x=pos/12;
        this.y=pos%12;
    }
    
    public Position(String pos){
        this.x=pos.charAt(0)-'a'+0.5;
        this.y=pos.charAt(1)+0.5;
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

    public double distanceTo(Position o) {
        return sqrt((this.x-o.x)*(this.x-o.x)+(this.y-o.y)*(this.y-o.y));
    }

    private static class NotCenterPositionException extends Exception {

        public NotCenterPositionException() {
        }
    }
    
}
