/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModel;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import wechesspresso.WeChesspresso;

/**
 *
 * @author Bastien
 */
public class Game extends DataModel{
    
    static public String START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    String fen=null;
    
    public Game(){
        
    }
    
    public Game(HashMap<String, String> input){
        super(input);
    }
    
    public boolean isOver()
    {
        if(!this.containsField("winner") || this.get("winner")==null) return false;
        return true;
    }
    
    //renvoie tous les objets ou l'attribut field=value
    public static ArrayList find(String field, Object value)
    {
        ArrayList g;
        g=ConnectionManager.find(Game.class,field,value);
        return g;
    }
    
    //renvoie tous les objets ou l'attribut field=value
    public static Game findById(Object id) throws SQLException
    {
        Game g;
        g=(Game)ConnectionManager.findById(Game.class,id);
        return g;
    }
    
    public boolean myTurn(Object my_id)
    {
        this.sync();
        Move m = Move.getLastFromGame(this);
        if(m.get("uid").equals(my_id.toString())){
            return false;
        }
        else return true;
    }
    
    //LOUIS : retourne vrai si il y a echec et mat
    public boolean isCheckmate()
    {
        return WeChesspresso.isCheckMate(this.get("fen"));
    }
    
    //LOUIS : retourne vrai si il y a echec
    public boolean isCheck()
    {
        return WeChesspresso.isCheck(this.get("fen"));
    }
    
    public String assignNewMove(Move m)
    {
        String id = ConnectionManager.insert(Move.class, m.getData());
        m.set("id", id);
        this.set("fen", m.get("fen"));
        return id;
    }
    
}
