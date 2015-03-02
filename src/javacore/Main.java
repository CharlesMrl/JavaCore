/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacore;

import DataModel.Player;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bast
 */
public class Main {
    public static void main(String[] args) {
        
        // Infos
        Player.find("name", "Pascard").get(0).print();
        Player.find("name", "Mariller").get(0).print();
        
        //Setup
        Scanner input = new Scanner(System.in);
        System.out.println("Choose user ID : ");
        String choice_user_id;
        choice_user_id = input.nextLine();
        
        System.out.println("Choose game ID : ");
        String choice_game_id;
        choice_game_id = input.nextLine();
        
        //Debut
        JavaCore javacore = new JavaCore(Integer.valueOf(choice_user_id), Integer.valueOf(choice_game_id));
        
        while (true) {
            try {
                javacore.loop();
            } catch (Exception ex) {
                Logger.getLogger(JavaCore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
