/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MoveController;

/**
 *
 * @author Bastien
 */
public class Position {
    
    public static final float SQUARE_SIDE = 65;
    double x;
    double y;
    
    public Position(double a, double b){
        this.x=a;
        this.y=b;
    }
    
    public String toString(){
        return new String(x+","+y);
    }
}
