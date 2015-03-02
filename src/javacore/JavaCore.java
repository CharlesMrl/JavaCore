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

    public Connection con;
    public Game current_game;
    public Player user;
    public final Scanner input = new Scanner(System.in);
    
    //Affiche un echiquier en texte a partir d'une FEN
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
    
    public JavaCore(int user_id, int game_id){
        //login
        this.con = ConnectionManager.getConnection();

        this.user=null;
        this.current_game=null;
        try {
            user = Player.findById(String.valueOf(user_id));
            //selection partie
            current_game = Game.findById(game_id);
            current_game.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void loop() throws Exception{
        String mon_coup=null;
        String newFen=null;
            //mise en place des pieces

            //Si mon tour de jouer
            if (current_game.myTurn(user.get("id") )) 
            {
                current_game.sync();
                printFEN(current_game.get("fen"));
                do {
                    //Attendre la detection d'un coup
                    System.out.println("Your turn, your move : ");
                    mon_coup = input.nextLine();
                    try {
                        //Verifier que le coup est valide
                        newFen = WeChesspresso.getNewFen(current_game.get("fen"), mon_coup);
                        if (newFen.equals("no")) 
                        {
                            System.out.println("Invalid move !");
                            WeChesspresso.printAllMoves(current_game.get("fen"));
                        }
                                
                        
                    } catch (IllegalMoveException ex) {
                        Logger.getLogger(JavaCore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } while (newFen.equals("no"));
                printFEN(newFen);
                //Envoi des infos du coup
                HashMap movedata = new HashMap();
                movedata.put("pid", current_game.get("id"));
                movedata.put("uid", user.get("id"));
                movedata.put("fen", newFen);
                movedata.put("pos1", mon_coup.substring(0, 2));
                movedata.put("pos2", mon_coup.substring(2));
                movedata.put("type", WeChesspresso.getMoveType(current_game.get("fen"),mon_coup)); // LOUIS : determiner type de coup
                movedata.put("time", Move.getTimeStamp());
                Move m = new Move(movedata);
                m.print();
                current_game.assignNewMove(m);
                // LOUIS : Verifier si echec et mat
                if (current_game.isCheckmate()) {
                    //Envoi info victoire
                    System.out.println("You won!");
                    current_game.set("winner", user.get("id"));
                }
            } //Si tour de l'adversaire
            else{
                //Attendre un nouveau coup
                System.out.print("Waiting for the opponent move...");
                current_game.sync();
                while (!current_game.myTurn(user.get("id"))) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(JavaCore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                current_game.sync();
                //Effectuer deplacements du coup
                Move m = Move.getLastFromGame(current_game);
                System.out.print("Move received : ");
                m.print();
                
                /* Code de déplacement des moteurs
                 * pour effectuer le move recu
                 * de l'adversaire.
                */
                
                //Si echec
                if(current_game.isCheck()){
                    System.out.println("CHECK! ... *stress*");
                }
                //Si coup gagnant
                if (current_game.isCheckmate()) {
                    //Informer joueur de la defaite
                    System.out.println("You lost!...loser");
                }
            }
    }

}
