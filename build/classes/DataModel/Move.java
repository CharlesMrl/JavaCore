/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModel;

import MoveController.Position;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Bastien
 */
public class Move extends DataModel
{
    
    public Move(HashMap<String, String> input){
        super(input);
    }
    
    static public Move make(String pid,String uid,String fen,String pos1,String pos2,String type,String time){
        HashMap movedata = new HashMap();
                movedata.put("gid", pid);
                movedata.put("uid", uid);
                movedata.put("fen", fen);
                movedata.put("pos1", pos1);
                movedata.put("pos2", pos2);
                movedata.put("type", type); // LOUIS : determiner type de coup
                movedata.put("time", time);
        return new Move(movedata);
    }
    
    public static String getTimeStamp(){
        //MySQL Datetime : YYYY-MM-DD HH:MM:SS
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return timestamp;
    }
    
    //renvoi le dernier Move de la partie pid
    public static Move getLastFromGame(Game g)
    {
        
        //recuperer (BDD) tous les moves de la partie
        ArrayList<DataModel> liste_move = ConnectionManager.find(Move.class, "gid", g.get("id"));
        
        //chercher le move avec la date la plus recente
        String latest_time = liste_move.get(0).get("time");
        Move m = (Move)liste_move.get(0);
        
        for(DataModel move : liste_move){
            if(move.get("time").compareTo(latest_time)>0){
                latest_time = move.get("time");
                m = (Move)move;
            }
        }
        
        return m;
    }
    
    //vrai si le Move est le dernier qui a été joué actuellement dans la partie
    public boolean isLastFromGame(Game g)
    {
        Move m = Move.getLastFromGame(g);
        if(m.get("id").equals(this.get("id"))) return true;
        else return false;
    }
    
    //renvoie tous les objets ou l'attribut field=value
    public static ArrayList<DataModel> find(String field, Object value)
    {
        ArrayList<DataModel> m;
        m=ConnectionManager.find(Move.class,field,value);
        return m;
    }
    
    //renvoie tous les objets ou l'id est id
    public static Move findById(Object id) throws SQLException
    {
        Move m=(Move)ConnectionManager.findById(Move.class,id);
        return m;
    }
    
}
