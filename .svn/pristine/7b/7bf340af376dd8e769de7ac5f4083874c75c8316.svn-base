/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Bastien
 */
public class Player extends DataModel{
    
    public Player(HashMap<String, String> input) {
        super(input);
    }

    public static Player findById(String id) throws SQLException {
        Player p=(Player)ConnectionManager.findById(Player.class,id);
        return p;
    }
    
    public static ArrayList<DataModel> find(String field, Object value) {
        ArrayList<DataModel> p=ConnectionManager.find(Player.class,field,value);
        return p;
    }
    
    //renvoi la liste des amis du joueur
    public ArrayList<Player> getFriends(){
        ArrayList<String> id_list = Friendship.friendsOf(this.get("id"));
        //...
        return null;
    }
    
    //renvoi vrai si le joueur uid est un ami du joueur
    public boolean isFriend(int uid){
        //...
        return true;
    }
    
    //renvoi la liste des parties qui impliquent le joueur (il est uidb ou uidw)
    public ArrayList<DataModel> getGames(){
        //...
        return new ArrayList();
    }
    
    //
    public String startNewGame(String adv_id){
        return new String();
    }
}
