/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModel;

import java.util.ArrayList;

/**
 *
 * @author Bastien
 */
public class Friendship  extends DataModel{
    
    //renvoie les id des amis du joueur uid
    public static ArrayList<String> friendsOf(Object uid)
    {
        ArrayList<DataModel> f1, f2;
        f1=ConnectionManager.find(Friendship.class,"uid1",uid);
        f2=ConnectionManager.find(Friendship.class,"uid2",uid);
        
        ArrayList<String> flist = new ArrayList();
        for(DataModel d : f1) flist.add(d.get("uid2"));
        for(DataModel d : f2) flist.add(d.get("uid1"));
        
        return flist;
    }
    
    
}
