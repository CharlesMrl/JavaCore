/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacore;

import DataModel.*;
import chesspresso.move.IllegalMoveException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import wechesspresso.*;

/**
 *
 * @author Bastien
 */
public class JavaCore {

    /**
     * @param args the command line arguments
     */
    static Game current;
    static Player user;
    
    static void printFEN(String fen){
        String[] alines = fen.split("/");
        ArrayList<String> lines = new ArrayList();
        
        for(int i=0;i<alines.length;i++){
            lines.add(alines[i]);
        }
        String space = new String("");
        int lig=8;
        System.out.println(" |abcdefgh|");
        System.out.println("------------");
        for (String s : lines) {
                space = "";
                System.out.print(lig+"|");
                for (int c = 1; c <= 8; c++) {
                    space=space.concat(".");
                    s = s.replace(String.valueOf(c), space);
                }
                
                s = s.substring(0, 8);
                
                System.out.print(s);
                System.out.println("|"+lig--);
        }

        System.out.println("------------");
        System.out.println(" |abcdefgh|");
    }
    
    public static void main(String[] args) {

        //login
        Connection con = ConnectionManager.getConnection();

        String user_id, mon_coup, newFen = null;
        Scanner input = new Scanner(System.in);

        Player.find("name", "Pascard").get(0).print();
        Player.find("name", "Mariller").get(0).print();

        System.out.println("Choose user ID : ");
        user_id = input.nextLine();

        try {
            user = Player.findById(user_id);
        } catch (SQLException ex) {
            Logger.getLogger(JavaCore.class.getName()).log(Level.SEVERE, null, ex);
        }
        //selection partie
        try {
            current = Game.findById(1);
            current.print();
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            //mise en place des pieces

            //Si mon tour de jouer
            if (current.myTurn(user_id)) 
            {
                current.sync();
                printFEN(current.get("fen"));
                do {
                    //Attendre la detection d'un coup
                    System.out.println("Your turn, your move : ");
                    mon_coup = input.nextLine();
                    try {
                        //Verifier que le coup est valide
                        newFen = WeChesspresso.getNewFen(current.get("fen"), mon_coup);
                        if (newFen.equals("no")) 
                        {
                            System.out.println("Invalid move !");
                            WeChesspresso.printAllMoves(current.get("fen"));
                        }
                                
                        
                    } catch (IllegalMoveException ex) {
                        Logger.getLogger(JavaCore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } while (newFen.equals("no"));
                printFEN(newFen);
                //Envoi des infos du coup
                HashMap movedata = new HashMap();
                movedata.put("pid", current.get("id"));
                movedata.put("uid", user.get("id"));
                movedata.put("fen", newFen);
                movedata.put("pos1", mon_coup.substring(0, 2));
                movedata.put("pos2", mon_coup.substring(2));
                movedata.put("type", WeChesspresso.getMoveType(current.get("fen"),mon_coup)); // LOUIS : determiner type de coup
                movedata.put("time", Move.getTimeStamp());
                Move m = new Move(movedata);
                m.print();
                current.assignNewMove(m);
                // LOUIS : Verifier si echec et mat
                if (current.isCheckmate()) {
                    //Envoi info victoire
                    System.out.println("You won!");
                    current.set("winner", user_id);
                    
                }
            } //Si tour de l'adversaire
            else{
                //Attendre un nouveau coup
                System.out.print("Waiting for the opponent move...");
                current.sync();
                while (!current.myTurn(user_id)) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(JavaCore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                current.sync();
                //Effectuer deplacements du coup
                Move m = Move.getLastFromGame(current);
                System.out.print("Move received : ");
                m.print();
                //Si echec
                if(current.isCheck()){
                    System.out.println("CHECK! ... *stress*");
                }
                //Si coup gagnant
                if (current.isCheckmate()) {
                    //Informer joueur de la defaite
                    System.out.println("You lost!...loser");
                }
            }
        }
    }

}
