/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModel;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    
    public Game startNew(Player black, Player white) throws SQLException{
        HashMap<String, String> game_data = new HashMap();
        game_data.put("uidb", black.get("id"));
        game_data.put("uidw", white.get("id"));
        String id = ConnectionManager.insert(Game.class, game_data);
        Game g = (Game)ConnectionManager.findById(Game.class, id);
        return g;
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
        
        ArrayList<DataModel> liste_move = ConnectionManager.find(Move.class, "gid", this.get("id"));
            if(liste_move.isEmpty())
                if(this.get("uidw").equals(my_id))
                    return true;
        
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
    
    public ArrayList<DataModel> getAllMoves(){
        ArrayList<DataModel> liste_move = ConnectionManager.find(Move.class, "gid", this.get("id"));
        return liste_move;
    }
    
    public String getPreviousFen(){
        String fen;
        
        ArrayList<DataModel> liste_move = this.getAllMoves();
        
        String latest_time = liste_move.get(0).get("time");
        String sec_latest_time = liste_move.get(0).get("time");
        Move m = (Move)liste_move.get(0);
        
        HashMap<Move, String> moves = new HashMap();
        
        for(DataModel move : liste_move){
            moves.put((Move)move, move.get("time"));
        }
        
         Map.Entry<Move, String> max1 = null;
        Map.Entry<Move, String> max2 = null;
        for(Map.Entry<Move, String> en : moves.entrySet()){
          if (max1 == null || en.getValue().compareTo(max1.getValue()) > 0){
              max1 = en;
              }                   
      }
      //searching the second biggest value
      for(Map.Entry<Move, String> en : moves.entrySet()){
          if (max2 == null || en.getValue().compareTo(max2.getValue())>0 && !en.getValue().equals(max1.getValue())){
              max2 = en;
              }                   
      }
      
      return max2.getKey().get("fen");
    }
    
}
